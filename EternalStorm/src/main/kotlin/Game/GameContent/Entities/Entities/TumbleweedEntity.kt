package la.vok.Game.GameContent.Entities.Entities

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.Ai.TumbleweedAI
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.Entities.Special.Entity
import la.vok.Game.GameContent.Entities.EntityRender.TumbleweedRenderEntity
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.LiquidDetectorComponent
import la.vok.Game.GameSystems.EntityComponents.IEntityComponent
import la.vok.Game.GameSystems.EntityComponents.EntityEvent
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import kotlin.random.Random

class TumbleweedEntity(entityType: AbstractEntityType, gameCycle: GameCycle) : Entity(entityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = TumbleweedRenderEntity(getRenderLayer(), this)
    
    var rotationAngle: Float = 0f
    
    init {
        // Random size: scale size between 1.5x and 2.5x of baseSize
        val scale = Random.nextFloat() * 1.0f + 1.5f
        size = scale v scale
        
        hasCollisionDetector = true
        
        // Add LiquidDetectorComponent to detect contact with liquids
        addComponent(LiquidDetectorComponent(this))
        
        // Listen to LiquidContactStart to die
        addComponent(object : IEntityComponent {
            override fun onEvent(event: EntityEvent) {
                if (event is EntityEvent.LiquidContactStart) {
                    dieInLiquid()
                }
            }
        })
    }
    
    override fun spawn() {
        super.spawn()
        ai = TumbleweedAI(this, gameCycle)
    }
    
    private fun dieInLiquid() {
        if (isDead) return
        // Deal damage directly using EntityApi
        gameCycle.entityApi.absoluteDamage(dimension!!, this, DamageData(1, Vec2.ZERO, null, null))
    }
    
    override fun physicUpdate() {
        super.physicUpdate()
        // Rotate proportional to horizontal speed
        val speedX = rigidBody?.speed?.x ?: 0f
        rotationAngle += speedX * 0.2f
    }
}
