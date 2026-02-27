package la.vok.Game.GameContent.Tiles.Tiles

import la.vok.Core.CoreControllers.CoreController
import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameContent.Tiles.TileTypes.AbstractTileType
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.Entities.EntityApi
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p

open class Tile(var tileType: AbstractTileType) {
    open fun render(lg: LGraphics, position: Vec2, size: Vec2, gameController: GameController) {}

    var tileHp = tileType.hp
}