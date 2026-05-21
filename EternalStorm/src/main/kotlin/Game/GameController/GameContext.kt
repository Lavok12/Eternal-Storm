package la.vok.Game.GameController

import la.vok.Game.GameSystems.WorldSystems.Entities.EntitySpawner
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameSystems.WorldSystems.Crafts.CraftApi
import la.vok.Game.GameSystems.WorldSystems.Dimensions.DimensionsApi
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageSystem
import la.vok.Game.GameSystems.WorldSystems.Entities.EntityApi
import la.vok.Game.GameSystems.WorldSystems.Entities.TeleportService
import la.vok.Game.GameSystems.WorldSystems.Entities.WorldQuery
import la.vok.Game.GameSystems.WorldSystems.Items.ItemsApi
import la.vok.Game.GameSystems.WorldSystems.Particles.ParticlesApi
import la.vok.Game.GameSystems.WorldSystems.Players.PlayerApi
import la.vok.Game.GameSystems.WorldSystems.VfxObjects.VfxObjectsApi

/**
 * Context that holds references to all major game systems and APIs.
 * Used to break circular dependencies and provide a unified access point.
 */
class GameContext(
    val gameController: GameController,
    val gameCycle: GameCycle
) {
    lateinit var entityApi: EntityApi
    lateinit var entitySpawner: EntitySpawner
    lateinit var damageSystem: DamageSystem
    lateinit var worldQuery: WorldQuery
    lateinit var teleportService: TeleportService
    lateinit var playerApi: PlayerApi
    
    lateinit var mapApi: MapApi
    lateinit var particlesApi: ParticlesApi
    lateinit var vfxObjectsApi: VfxObjectsApi
    lateinit var itemsApi: ItemsApi
    lateinit var craftApi: CraftApi
    lateinit var dimensionsApi: DimensionsApi
    lateinit var liquidApi: la.vok.Game.GameSystems.WorldSystems.Liquid.LiquidApi
}
