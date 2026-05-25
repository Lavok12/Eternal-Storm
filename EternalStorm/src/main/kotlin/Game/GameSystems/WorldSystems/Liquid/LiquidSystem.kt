package la.vok.Game.GameSystems.WorldSystems.Liquid

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.LiquidTypes.AbstractLiquidInteraction
import la.vok.Game.GameController.CollisionType
import kotlin.math.min

class LiquidSystem(val dimension: AbstractDimension) {
    val width = dimension.width
    val height = dimension.height
    val size = width * height

    private val registration = dimension.gameCycle.gameController.coreController.objectRegistration
    private var interactionTable = arrayOfNulls<AbstractLiquidInteraction>(256 * 256)
    private var interactionsInitialized = false

    var amounts = ByteArray(size)
    var types = ByteArray(size)

    private var nextAmounts = ByteArray(size)
    private var nextTypes = ByteArray(size)

    private var activeCells = IntArray(size)
    private var activeCount = 0
    private var nextActiveCells = IntArray(size)
    private var nextActiveCount = 0

    // Версионные флаги для мгновенного сброса
    private var currentActiveFlags = IntArray(size)
    private var nextActiveFlags = IntArray(size)
    private var tickVersion = 1

    // Список измененных ячеек для разреженной синхронизации буферов
    private var changedIndices = IntArray(size)
    private var changedCount = 0
    private var nextChangedIndices = IntArray(size)
    private var nextChangedCount = 0

    fun isInside(x: Int, y: Int) = x in 0 until width && y in 0 until height
    fun getIndex(x: Int, y: Int) = y * width + x

    private fun initInteractionTable() {
        if (interactionsInitialized) return
        interactionTable.fill(null)
        for (interaction in registration.liquidInteractions) {
            val id1 = interaction.liquid1.toInt() and 0xFF
            val id2 = interaction.liquid2.toInt() and 0xFF
            // Записываем оба направления, так как matches симметричен
            interactionTable[id1 * 256 + id2] = interaction
            interactionTable[id2 * 256 + id1] = interaction
        }
        interactionsInitialized = true
    }

    fun setLiquid(x: Int, y: Int, typeId: Byte, amount: Int) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)

        val bAmount = amount.toByte()
        val bType = if (amount > 0) typeId else 0

        amounts[idx] = bAmount
        types[idx] = bType
        nextAmounts[idx] = bAmount
        nextTypes[idx] = bType

        // Отмечаем как измененное для синхронизации
        markChanged(idx)

        activate(x, y)
        activateNeighbors(x, y)
    }

    fun getAmount(x: Int, y: Int): Int = if (isInside(x, y)) amounts[getIndex(x, y)].toInt() and 0xFF else 0
    fun getTypeId(x: Int, y: Int): Byte = if (isInside(x, y)) types[getIndex(x, y)] else 0

    fun activate(x: Int, y: Int) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        if (currentActiveFlags[idx] != tickVersion) {
            currentActiveFlags[idx] = tickVersion
            if (activeCount < size) {
                activeCells[activeCount++] = idx
            }
        }
    }

    fun activateNeighbors(x: Int, y: Int) {
        // Развертывание цикла 3x3 для скорости
        val xMinus = x - 1
        val xPlus = x + 1
        val yMinus = y - 1
        val yPlus = y + 1

        activate(xMinus, yMinus)
        activate(x, yMinus)
        activate(xPlus, yMinus)
        activate(xMinus, y)
        activate(xPlus, y)
        activate(xMinus, yPlus)
        activate(x, yPlus)
        activate(xPlus, yPlus)
    }

    private fun markChanged(idx: Int) {
        changedIndices[changedCount++] = idx
    }

    fun update() {
        if (activeCount == 0) return

        initInteractionTable()

        // 1. Синхронизируем буфер nextAmounts с изменениями из предыдущего такта
        // Это заменяет дорогой System.arraycopy(size)
        for (i in 0 until changedCount) {
            val idx = changedIndices[i]
            nextAmounts[idx] = amounts[idx]
            nextTypes[idx] = types[idx]
        }
        changedCount = 0

        // 2. Подготовка нового такта
        tickVersion++
        if (tickVersion > 2000000000) {
            tickVersion = 1
            currentActiveFlags.fill(0)
            nextActiveFlags.fill(0)
        }
        nextActiveCount = 0

        for (i in 0 until activeCount) {
            processCell(activeCells[i])
        }

        // 3. Своп буферов данных
        val tempAmounts = amounts
        amounts = nextAmounts
        nextAmounts = tempAmounts

        val tempTypes = types
        types = nextTypes
        nextTypes = tempTypes

        // 4. Своп буферов активности
        val tempActive = activeCells
        activeCells = nextActiveCells
        nextActiveCells = tempActive

        activeCount = nextActiveCount

        val tempFlags = currentActiveFlags
        currentActiveFlags = nextActiveFlags
        nextActiveFlags = tempFlags

        // 5. Перенос накопленных изменений для синхронизации в следующем такте
        val tempChanged = changedIndices
        changedIndices = nextChangedIndices
        nextChangedIndices = tempChanged

        changedCount = nextChangedCount
        nextChangedCount = 0
    }

    private fun processCell(idx: Int) {
        val amount = amounts[idx].toInt() and 0xFF
        if (amount <= 0) return

        val typeId = types[idx]
        val type = registration.getLiquidType(typeId) ?: return

        if (type.viscosity > 1) {
            if (dimension.gameCycle.physicTicks % type.viscosity != 0L) {
                activateNext(idx)
                return
            }
        }

        val x = idx % width
        val y = idx / width

        // 1. Flow Down
        if (y > 0) {
            val belowIdx = idx - width
            if (canFlowInto(x, y - 1, x, y, typeId, amount)) {
                val belowAmount = nextAmounts[belowIdx].toInt() and 0xFF
                if (belowAmount < 255) {
                    val flow = min(amount, 255 - belowAmount)
                    if (flow > 0) {
                        moveLiquid(idx, belowIdx, flow, typeId)
                        activateNextNeighbors(x, y)
                        activateNextNeighbors(x, y - 1)
                        // FIX: читаем из nextAmounts, а не из amounts (старый буфер)
                        if ((nextAmounts[idx].toInt() and 0xFF) <= 0) return
                    }
                }
            } else {
                // FIX: читаем из nextAmounts, а не из amounts (старый буфер)
                if ((nextAmounts[idx].toInt() and 0xFF) <= 0) return
            }
        }

        // 2. Flow Sides (Equalization)
        flowSides(x, y, idx, typeId)
    }

    private fun flowSides(x: Int, y: Int, idx: Int, typeId: Byte) {
        // FIX: всегда читаем из nextAmounts — единственный актуальный буфер во время обновления
        val currentAmount = nextAmounts[idx].toInt() and 0xFF
        if (currentAmount <= 5) return

        val leftX = x - 1
        val rightX = x + 1

        // FIX: передаём currentAmount (из nextAmounts) в обоих вызовах canFlowInto,
        // ранее rightCan ошибочно использовал getAmount(x, y) — старый буфер amounts[]
        val leftCan = isInside(leftX, y) && canFlowInto(leftX, y, x, y, typeId, currentAmount)
        val rightCan = isInside(rightX, y) && canFlowInto(rightX, y, x, y, typeId, currentAmount)

        if (!leftCan && !rightCan) return

        val leftIdx = if (leftCan) getIndex(leftX, y) else -1
        val rightIdx = if (rightCan) getIndex(rightX, y) else -1

        val leftAmount = if (leftCan) nextAmounts[leftIdx].toInt() and 0xFF else 255
        val rightAmount = if (rightCan) nextAmounts[rightIdx].toInt() and 0xFF else 255

        // FIX: remaining берётся из nextAmounts, ранее был getAmount(x, y) — старый буфер.
        // После каждого moveLiquid перечитываем из nextAmounts, чтобы diff был корректным.
        var remaining = currentAmount

        if (leftCan && leftAmount < remaining - 1) {
            val diff = (remaining - leftAmount) / 2
            if (diff > 0) {
                moveLiquid(idx, leftIdx, diff, typeId)
                // Перечитываем после переноса — nextAmounts[idx] уже изменился
                remaining = nextAmounts[idx].toInt() and 0xFF
                activateNextNeighbors(x, y)
                activateNextNeighbors(leftX, y)
            }
        }

        if (rightCan && rightAmount < remaining - 1) {
            val diff = (remaining - rightAmount) / 2
            if (diff > 0) {
                moveLiquid(idx, rightIdx, diff, typeId)
                activateNextNeighbors(x, y)
                activateNextNeighbors(rightX, y)
            }
        }
    }

    private fun canFlowInto(x: Int, y: Int, srcX: Int, srcY: Int, sourceId: Byte, sourceAmount: Int): Boolean {
        val tile = dimension.mapSystem.getTileType(x, y)
        if (tile != null && tile.collisionType == CollisionType.FULL) return false

        val idx = getIndex(x, y)
        val targetId = nextTypes[idx]
        if (targetId != 0.toByte() && targetId != sourceId) {
            // Используем оптимизированную таблицу взаимодействий
            val interaction = interactionTable[(sourceId.toInt() and 0xFF) * 256 + (targetId.toInt() and 0xFF)]
            if (interaction != null) {
                val targetAmount = nextAmounts[idx].toInt() and 0xFF
                if (interaction.onReact(srcX, srcY, x, y, dimension, sourceAmount, targetAmount)) {
                    return false
                }
            }
            return false // Блокируем поток, если жидкости разные и нет реакции (или реакция не потребила их)
        }
        return true
    }

    private fun moveLiquid(fromIdx: Int, toIdx: Int, flow: Int, typeId: Byte) {
        val srcAmount = nextAmounts[fromIdx].toInt() and 0xFF
        val dstAmount = nextAmounts[toIdx].toInt() and 0xFF

        val newSrc = (srcAmount - flow).toByte()
        val newDst = (dstAmount + flow).toByte()

        nextAmounts[fromIdx] = newSrc
        nextAmounts[toIdx] = newDst

        if (newSrc.toInt() == 0) nextTypes[fromIdx] = 0
        nextTypes[toIdx] = typeId

        // Записываем изменения для синхронизации в следующем такте
        nextChangedIndices[nextChangedCount++] = fromIdx
        nextChangedIndices[nextChangedCount++] = toIdx

        activateNext(fromIdx)
        activateNext(toIdx)
    }

    private fun activateNext(idx: Int) {
        if (nextActiveFlags[idx] != tickVersion) {
            nextActiveFlags[idx] = tickVersion
            nextActiveCells[nextActiveCount++] = idx
        }
    }

    private fun activateNextNeighbors(x: Int, y: Int) {
        val xMinus = x - 1
        val xPlus = x + 1
        val yMinus = y - 1
        val yPlus = y + 1

        activateNextIfInside(xMinus, yMinus)
        activateNextIfInside(x, yMinus)
        activateNextIfInside(xPlus, yMinus)
        activateNextIfInside(xMinus, y)
        activateNextIfInside(x, y)
        activateNextIfInside(xPlus, y)
        activateNextIfInside(xMinus, yPlus)
        activateNextIfInside(x, yPlus)
        activateNextIfInside(xPlus, yPlus)
    }

    private fun activateNextIfInside(x: Int, y: Int) {
        if (x in 0 until width && y in 0 until height) {
            activateNext(y * width + x)
        }
    }
}