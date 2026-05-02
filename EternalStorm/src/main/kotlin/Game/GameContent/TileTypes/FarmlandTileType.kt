package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Map.MapController
import la.vok.State.AppState

class FarmlandTileType : AbstractTileType() {
    override val tag: String = TilesList.farmland_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = AppState.res("seamless_dirt.png")
    override val drop: DropEntry = SingleDrop(ItemsList.dirt_block)

    override fun onUpdate(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController) {
        super.onUpdate(x, y, dimension, mapController)
        
        // Revert to dirt if covered by a block above that is NOT a plant
        if (dimension.gameCycle.mapApi.tileIsActive(dimension, x, y + 1)) {
            val typeAbove = dimension.gameCycle.mapApi.getTileType(dimension, x, y + 1)
            if (typeAbove?.hasTag(la.vok.Game.GameContent.ContentList.BlockTags.PLANT) != true) {
                val dirtType = dimension.gameCycle.gameController.coreController.objectRegistration.tiles[TilesList.dirt_block]
                dimension.mapSystem.setTileType(x, y, dirtType)
                dimension.mapSystem.setTileHp(x, y, dirtType?.maxHp ?: 10)
            }
        }
    }
}
