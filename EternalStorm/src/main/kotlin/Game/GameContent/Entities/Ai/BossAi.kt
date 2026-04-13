package la.vok.Game.GameContent.Entities.Ai

import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.ContentList.EntitiesList
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BossAi(entity: Entity, gameCycle: GameCycle) : AbstractAI(entity, gameCycle) {

    // =====================================================================
    // СТАДИИ
    // =====================================================================

    data class Stage(
        val id: Int,
        val durationTicks: Int,          // -1 = бесконечная, пока не вызовешь nextStage()
        val onEnter: BossAi.() -> Unit = {},
        val onExit: BossAi.() -> Unit = {},
        val onTick: BossAi.() -> Unit = {}
    )

    private val stages = mutableListOf<Stage>()
    private var currentStageIndex = 0
    private var stageTick = 0
    var currentStage: Stage? = null
        private set

    init {
        addStage(Stage(
            id = 0,
            durationTicks = 60,
            onEnter = {
                val dir = (targetWorldPos() - entity.position).normalized()
                entity.rigidBody?.addForce(dir*0.25f + (0 v 0.1f))
            },
            onExit = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  0.15f)
            }
        ))
        addStage(Stage(
            id = 1,
            durationTicks = 180,
            onEnter = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.25f v  0.25f)
            },
        ))
        addStage(Stage(
            id = 2,
            durationTicks = 120,
            onExit = {
                val dir = (targetWorldPos() - entity.position).normalized()
                entity.rigidBody?.addForce(dir*0.05f)
            },
        ))
        addStage(Stage(
            id = 3,
            durationTicks = 140,
            onEnter = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.5 v  0.15f)
            },
        ))
        addStage(Stage(
            id = 4,
            durationTicks = 360,
            onTick = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.001 v  0.001f)
                entity.rigidBody!!.useFriction()
            }
        ))
        addStage(Stage(
            id = 5,
            durationTicks = 250,
            onEnter = {
                entity.rigidBody?.speed = Vec2.ZERO
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  1f)
            },
            onExit = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  -1f)
            }
        ))
        addStage(Stage(
            id = 6,
            durationTicks = 225,
            onEnter = {
                entity.rigidBody?.speed = Vec2.ZERO
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  1f)
            },
            onExit = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  -0.5f)
            }
        ))
        addStage(Stage(
            id = 7,
            durationTicks = 200,
            onEnter = {
                entity.rigidBody?.speed = Vec2.ZERO
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  1f)
            },
            onExit = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  -0.5f)
            }
        ))
        addStage(Stage(
            id = 8,
            durationTicks = 150,
            onEnter = {
                entity.rigidBody?.speed = Vec2.ZERO
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  1f)
            },
            onExit = {
                val dirX = (targetWorldPos().x - entity.position.x).coerceIn(-1f, 1f)
                entity.rigidBody?.addForce(dirX*0.3 v  -0.5f)
            }
        ))
        startStages()
    }

    // Регистрация стадий — вызывать в init наследника
    protected fun addStage(stage: Stage) {
        stages += stage
    }

    protected fun startStages() {
        if (stages.isEmpty()) return
        currentStageIndex = 0
        stageTick = 0
        currentStage = stages[0]
        currentStage?.onEnter?.invoke(this)
    }

    // Принудительно перейти на следующую стадию
    protected fun nextStage() {
        currentStage?.onExit?.invoke(this)

        // Используем деление по модулю для зацикливания
        currentStageIndex = (currentStageIndex + 1) % stages.size

        stageTick = 0
        currentStage = stages[currentStageIndex]
        currentStage?.onEnter?.invoke(this)
    }

    // Перейти на конкретную стадию по id
    protected fun goToStage(id: Int) {
        val idx = stages.indexOfFirst { it.id == id }
        if (idx == -1) return
        currentStage?.onExit?.invoke(this)
        currentStageIndex = idx
        stageTick = 0
        currentStage = stages[idx]
        currentStage?.onEnter?.invoke(this)
    }

    // Колбэк когда все стадии закончились
    open fun onAllStagesDone() {}

    // =====================================================================

     fun getTarget(): Entity? {
        return gameCycle.entityApi.getNearestEntity(entity.dimension!!, entity.position, 500f, EntitiesList.player)
    }

    override fun physicUpdate() {
        val stage = currentStage ?: return

        stage.onTick.invoke(this)

        if (stage.durationTicks > 0) {
            stageTick++
            if (stageTick >= stage.durationTicks) nextStage()
        }
    }

    override fun targetScreenPos(): Vec2 = worldToScreen(targetWorldPos())
    override fun targetWorldPos(): Vec2 = getTarget()?.position ?: Vec2.ZERO
    override fun targetMapPos(): LPoint = worldToMap(targetWorldPos())
}