package la.vok.Game.GameContent.Tiles.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Tiles.Tiles.DirtTile
import la.vok.Game.GameContent.Tiles.Tiles.Tile
import la.vok.Game.GameController.GameCycle
import la.vok.State.AppState

class DirtTileType() : AbstractTileType() {
    override val tag: String = AppState.addNamespace("dirt")
    override val blockStrength: Int = 50
    override val hp: Int = 0

    override fun createTile(gameController: GameController) : Tile {
        return DirtTile(this)
    }
}