package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.Game.GameContent.TileData.ChestTileData
import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Game.Windows.GameUI.Modules.GameInterfaces.ChestUiInterface
import la.vok.Game.Windows.GameUI.Modules.InventoryModule
import la.vok.Game.GameContent.Entities.Entities.PlayerEntity

class ChestTileType : AbstractTileType() {
    override val texture: String = AppState.res("chest.png")
    override val width: Int = 2
    override val height: Int = 2
    override val tag: String = TilesList.chest_block
    override var collisionType = CollisionType.FULL
    override val blockStrength = 10
    override val maxHp = 100
    override val drop = SingleDrop(ItemsList.chest)

    init {
        addInteractionReaction(BlockInteractionType.RIGHT) { context ->
            val data = context.mapApi.getTileData(context.dimension, context.x, context.y) as? ChestTileData
            if (data != null) {
                val playerControl = context.mapApi.gameCycle.gameController.playerControl
                val playerId = (context.interactor as? PlayerEntity)?.systemId ?: 0L
                context.mapApi.gameCycle.playerApi.openBlockInterface(playerId, ChestUiInterface(playerControl, data))
            }
        }
    }

    override fun createTileData(x: Int, y: Int, dimension: AbstractDimension): AbstractTileData? {
        return ChestTileData(x, y, dimension)
    }
}
