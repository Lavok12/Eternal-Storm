package la.vok.Game.GameSystems.WorldSystems.Particles

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.TileParticle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

class ParticlesApi(var particleController: ParticleController) {
    val gameCycle: GameCycle = particleController.gameCycle
    val gameController: GameController get() = gameCycle.gameController

    // ─── Tile Particles ──────────────────────────────────────────────────────

    fun spawnTileParticle(
        tileType: AbstractTileType,
        worldPos: Vec2,
        speed: Vec2 = Vec2.ZERO
    ) {
        particleController.particleSystem.addParticle(
            TileParticle(gameCycle, tileType, worldPos, speed)
        )
    }

    fun spawnTileParticleRandom(
        tileType: AbstractTileType,
        worldPos: Vec2,
        speedScale: Float = 1f
    ) {
        val randomOffset = AppState.main.random(-0.5f, 0.5f) v AppState.main.random(-0.5f, 0.5f)
        val randomSpeed = (AppState.main.random(-1f, 1f) v AppState.main.random(-1f, 1f)) *
                AppState.main.random(0.025f, 0.05f) * speedScale
        spawnTileParticle(tileType, worldPos + randomOffset, randomSpeed)
    }

    fun spawnTileParticles(
        tileType: AbstractTileType,
        worldPos: Vec2,
        count: Int,
        speedScale: Float = 1f
    ) {
        repeat(count) { spawnTileParticleRandom(tileType, worldPos, speedScale) }
    }

    fun spawnTileParticleAt(
        x: Int, y: Int,
        tileType: AbstractTileType,
        count: Int = 1,
        speed: Vec2 = Vec2.ZERO
    ) {
        val worldPos = gameCycle.mapApi.getTilePos(x, y)
        repeat(count) { spawnTileParticleRandom(tileType, worldPos + speed) }
    }

    fun spawnTileParticleBurst(
        worldPos: Vec2,
        count: Int,
        radius: Float = 1f
    ) {
        val map = gameCycle.mapApi
        repeat(count) {
            val offset = Vec2(AppState.main.random(-radius, radius), AppState.main.random(-radius, radius))
            val point = map.getPointFromPos(worldPos + offset)
            val tileType = map.getTileType(point.x, point.y) ?: return@repeat
            val context = map.getTileContext(point.x, point.y) ?: return@repeat
            spawnTileParticleRandom(tileType, worldPos + offset)
        }
    }
}