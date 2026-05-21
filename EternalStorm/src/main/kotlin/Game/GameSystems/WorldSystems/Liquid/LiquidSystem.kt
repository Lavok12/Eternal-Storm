package la.vok.Game.GameSystems.WorldSystems.Liquid

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameController.CollisionType
import kotlin.math.min

class LiquidSystem(val dimension: AbstractDimension) {
    val width = dimension.width
    val height = dimension.height
    val size = width * height

    var amounts = ByteArray(size)
    var types = ByteArray(size)

    private var nextAmounts = ByteArray(size)
    private var nextTypes = ByteArray(size)

    private var activeCells = IntArray(size)
    private var activeCount = 0
    private var nextActiveCells = IntArray(size)
    private var nextActiveCount = 0
    
    private var currentActiveFlags = BooleanArray(size)
    private var nextActiveFlags = BooleanArray(size)

    fun isInside(x: Int, y: Int) = x in 0 until width && y in 0 until height
    fun getIndex(x: Int, y: Int) = y * width + x

    fun setLiquid(x: Int, y: Int, typeId: Byte, amount: Int) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        
        amounts[idx] = amount.toByte()
        types[idx] = if (amount > 0) typeId else 0
        
        nextAmounts[idx] = amount.toByte()
        nextTypes[idx] = if (amount > 0) typeId else 0
        
        // Always activate the cell and its neighbors when changed
        activate(x, y)
        activateNeighbors(x, y)
        
        // Also ensure they are active for the NEXT tick if update is running
        activateNext(idx)
        activateNextNeighbors(x, y)
    }

    fun getAmount(x: Int, y: Int): Int = if (isInside(x, y)) amounts[getIndex(x, y)].toInt() and 0xFF else 0
    fun getTypeId(x: Int, y: Int): Byte = if (isInside(x, y)) types[getIndex(x, y)] else 0

    fun activate(x: Int, y: Int) {
        if (!isInside(x, y)) return
        val idx = getIndex(x, y)
        if (!currentActiveFlags[idx]) {
            currentActiveFlags[idx] = true
            if (activeCount < size) {
                activeCells[activeCount++] = idx
            }
        }
    }

    fun activateNeighbors(x: Int, y: Int) {
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) continue
                activate(x + dx, y + dy)
            }
        }
    }

    fun update() {
        if (activeCount == 0) return

        System.arraycopy(amounts, 0, nextAmounts, 0, size)
        System.arraycopy(types, 0, nextTypes, 0, size)
        
        nextActiveCount = 0
        nextActiveFlags.fill(false)

        for (i in 0 until activeCount) {
            processCell(activeCells[i])
        }

        // Swap state buffers
        val tempAmounts = amounts
        amounts = nextAmounts
        nextAmounts = tempAmounts

        val tempTypes = types
        types = nextTypes
        nextTypes = tempTypes

        // Swap active buffers
        val tempActive = activeCells
        activeCells = nextActiveCells
        nextActiveCells = tempActive
        
        activeCount = nextActiveCount
        
        // Swap flags
        val tempFlags = currentActiveFlags
        currentActiveFlags = nextActiveFlags
        nextActiveFlags = tempFlags
    }

    private fun processCell(idx: Int) {
        val amount = amounts[idx].toInt() and 0xFF
        if (amount <= 0) return

        val typeId = types[idx]
        val type = dimension.gameCycle.gameController.coreController.objectRegistration.getLiquidType(typeId) ?: return
        
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
                        if (getAmount(x, y) <= 0) return
                    }
                }
            } else {
                // If blocked by reaction, amount might have changed to 0
                if (getAmount(x, y) <= 0) return
            }
        }

        // 2. Flow Sides (Equalization)
        flowSides(x, y, idx, typeId)
    }

    private fun flowSides(x: Int, y: Int, idx: Int, typeId: Byte) {
        val currentAmount = nextAmounts[idx].toInt() and 0xFF
        if (currentAmount <= 5) return

        val leftX = x - 1
        val rightX = x + 1
        
        val leftCan = isInside(leftX, y) && canFlowInto(leftX, y, x, y, typeId, currentAmount)
        val rightCan = isInside(rightX, y) && canFlowInto(rightX, y, x, y, typeId, getAmount(x, y))
        
        if (!leftCan && !rightCan) return
        
        // Update current amount after potential side reactions
        val remainingAfterLeft = getAmount(x, y) 

        val leftIdx = if (leftCan) getIndex(leftX, y) else -1
        val rightIdx = if (rightCan) getIndex(rightX, y) else -1
        
        val leftAmount = if (leftCan) nextAmounts[leftIdx].toInt() and 0xFF else 255
        val rightAmount = if (rightCan) nextAmounts[rightIdx].toInt() and 0xFF else 255
        
        var remaining = remainingAfterLeft
        
        if (leftCan && leftAmount < remaining - 1) {
            val diff = (remaining - leftAmount) / 2
            if (diff > 0) {
                moveLiquid(idx, leftIdx, diff, typeId)
                remaining -= diff
                activateNextNeighbors(x, y)
                activateNextNeighbors(leftX, y)
            }
        }
        
        if (rightCan && rightAmount < remaining - 1) {
            val diff = (remaining - rightAmount) / 2
            if (diff > 0) {
                moveLiquid(idx, rightIdx, diff, typeId)
                remaining -= diff
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
            val registration = dimension.gameCycle.gameController.coreController.objectRegistration
            for (interaction in registration.liquidInteractions) {
                if (interaction.matches(sourceId, targetId)) {
                    val targetAmount = amounts[idx].toInt() and 0xFF
                    if (interaction.onReact(srcX, srcY, x, y, dimension, sourceAmount, targetAmount)) {
                        return false
                    }
                }
            }
            return false
        }
        return true
    }

    private fun moveLiquid(fromIdx: Int, toIdx: Int, flow: Int, typeId: Byte) {
        val srcAmount = nextAmounts[fromIdx].toInt() and 0xFF
        val dstAmount = nextAmounts[toIdx].toInt() and 0xFF

        nextAmounts[fromIdx] = (srcAmount - flow).toByte()
        nextAmounts[toIdx] = (dstAmount + flow).toByte()
        
        if (nextAmounts[fromIdx].toInt() == 0) nextTypes[fromIdx] = 0
        nextTypes[toIdx] = typeId
        
        activateNext(fromIdx)
        activateNext(toIdx)
    }

    private fun activateNext(idx: Int) {
        if (!nextActiveFlags[idx]) {
            nextActiveFlags[idx] = true
            nextActiveCells[nextActiveCount++] = idx
        }
    }

    private fun activateNextNeighbors(x: Int, y: Int) {
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (isInside(x + dx, y + dy)) {
                    activateNext(getIndex(x + dx, y + dy))
                }
            }
        }
    }
}
