package la.vok.Game.GameContent.Tiles.Tiles

import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p

open class Tile(var tileType: AbstractTileType) {
    lateinit var mapSystem: MapSystem
    val coreController: CoreController get() = mapSystem.mapController.gameController.coreController
    val mapApi: MapApi get() = mapSystem.mapController.mapApi

    var position = 0 p 0

    open fun render(lg: LGraphics, position: Vec2, size: Vec2) {}

    var tileHp = tileType.hp
}