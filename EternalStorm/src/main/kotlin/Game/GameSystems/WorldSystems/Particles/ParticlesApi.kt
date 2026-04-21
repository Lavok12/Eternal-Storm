package la.vok.Game.GameSystems.WorldSystems.Particles

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.EntityParticle
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.TileParticle
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.WallParticle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState
import processing.core.PImage
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class ParticlesApi(var gameCycle: GameCycle) {
    val gameController: GameController get() = gameCycle.gameController

    fun buildTile(dimension: AbstractDimension, tileType: AbstractTileType) = TileParticleBuilder(this, dimension, tileType)
    fun buildWall(dimension: AbstractDimension, wallType: AbstractWallType) = WallParticleBuilder(this, dimension, wallType)
    fun buildEntity(dimension: AbstractDimension, image: PImage) = EntityParticleBuilder(this, dimension, image)

    fun tileBurst(dimension: AbstractDimension, pos: Vec2) = BurstBuilder(this, dimension, pos, isTile = true)
    fun wallBurst(dimension: AbstractDimension, pos: Vec2) = BurstBuilder(this, dimension, pos, isTile = false)

    fun addParticle(dimension: AbstractDimension, particle: Particle) {
        dimension.particleSystem.addParticle(particle)
    }
}

abstract class ParticleBuilder<T : ParticleBuilder<T>>(protected val api: ParticlesApi, protected val dimension: AbstractDimension) {
    protected var count = 1
    protected var pos = Vec2.ZERO
    protected var baseSpeed = Vec2.ZERO
    protected var randomSpeedScale = 0f
    protected var randomOffsetRadius = 0f

    @Suppress("UNCHECKED_CAST")
    fun at(pos: Vec2): T = apply { this.pos = pos } as T
    
    @Suppress("UNCHECKED_CAST")
    fun atBlock(x: Int, y: Int): T = apply { this.pos = api.gameCycle.mapApi.getBlockPos(dimension, x, y) } as T
    
    @Suppress("UNCHECKED_CAST")
    fun count(c: Int): T = apply { this.count = c } as T
    
    @Suppress("UNCHECKED_CAST")
    fun speed(s: Vec2): T = apply { this.baseSpeed = s } as T
    
    @Suppress("UNCHECKED_CAST")
    fun randomSpeed(scale: Float): T = apply { this.randomSpeedScale = scale } as T
    
    @Suppress("UNCHECKED_CAST")
    fun randomOffset(radius: Float): T = apply { this.randomOffsetRadius = radius } as T

    fun spawn() {
        repeat(count) {
            val offset = if (randomOffsetRadius > 0f) {
                Vec2(AppState.main.random(-randomOffsetRadius, randomOffsetRadius), AppState.main.random(-randomOffsetRadius, randomOffsetRadius))
            } else Vec2.ZERO

            val finalPos = pos + offset
            var finalSpeed = baseSpeed

            if (randomSpeedScale > 0f) {
                val rSpeed = (AppState.main.random(-1f, 1f) v AppState.main.random(-1f, 1f)) * AppState.main.random(0.025f, 0.05f) * randomSpeedScale
                finalSpeed = finalSpeed + rSpeed
            }

            dimension.particleSystem.addParticle(createParticle(finalPos, finalSpeed))
        }
    }

    protected abstract fun createParticle(finalPos: Vec2, finalSpeed: Vec2): Particle
}

open class TileParticleBuilder(api: ParticlesApi, dimension: AbstractDimension, var tileType: AbstractTileType) : ParticleBuilder<TileParticleBuilder>(api, dimension) {
    override fun createParticle(finalPos: Vec2, finalSpeed: Vec2): Particle {
        return TileParticle(api.gameCycle, tileType, finalPos, finalSpeed)
    }
}

open class WallParticleBuilder(api: ParticlesApi, dimension: AbstractDimension, var wallType: AbstractWallType) : ParticleBuilder<WallParticleBuilder>(api, dimension) {
    override fun createParticle(finalPos: Vec2, finalSpeed: Vec2): Particle {
        return WallParticle(api.gameCycle, wallType, finalPos, finalSpeed)
    }
}

open class EntityParticleBuilder(api: ParticlesApi, dimension: AbstractDimension, var image: PImage) : ParticleBuilder<EntityParticleBuilder>(api, dimension) {
    override fun createParticle(finalPos: Vec2, finalSpeed: Vec2): Particle {
        return EntityParticle(api.gameCycle, image, finalPos, finalSpeed)
    }
}

class BurstBuilder(val api: ParticlesApi, val dimension: AbstractDimension, val pos: Vec2, val isTile: Boolean) {
    var count = 1
    var radius = 1f
    var speedScale = 1f
    
    fun count(c: Int) = apply { this.count = c }
    fun radius(r: Float) = apply { this.radius = r }
    fun speedScale(s: Float) = apply { this.speedScale = s }
    
    fun spawn() {
        val map = api.gameCycle.mapApi
        repeat(count) {
            val offset = Vec2(AppState.main.random(-radius, radius), AppState.main.random(-radius, radius))
            val finalPos = pos + offset
            val point = map.getPointFromPos(finalPos)
            if (isTile) {
                val tileType = map.getTileType(dimension, point.x, point.y) ?: return@repeat
                api.buildTile(dimension, tileType)
                    .at(finalPos)
                    .randomSpeed(speedScale)
                    .spawn()
            } else {
                val wallType = map.getWallType(dimension, point.x, point.y) ?: return@repeat
                api.buildWall(dimension, wallType)
                    .at(finalPos)
                    .randomSpeed(speedScale)
                    .spawn()
            }
        }
    }
}