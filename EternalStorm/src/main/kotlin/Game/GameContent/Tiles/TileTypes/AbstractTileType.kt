package la.vok.Game.GameContent.Tiles.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.Game.GameController.GameCycle

abstract class AbstractTileType  {
    open val tag: String = ""
    open val blockStrength: Int = 0
    open val hp: Int = 0

    open fun createTile(gameController: GameController) : Tile {
        return Tile(this)
    }
}