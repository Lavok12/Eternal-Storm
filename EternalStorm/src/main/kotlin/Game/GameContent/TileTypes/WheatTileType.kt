package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.TileTypes.Crops.AbstractCropTileType
import la.vok.Game.GameContent.TileTypes.Crops.CropConfig

class WheatTileType : AbstractCropTileType(
    CropConfig(
        maxGrowthStages = 3,
        baseTicksPerStage = 5,
        texturePrefix = "seed_",
        getDrops = { growth ->
            if (growth >= 3) {
                listOf(
                    ItemsList.wheat_seeds to 1..2,
                    ItemsList.wheat_item to 2..3
                )
            } else {
                emptyList()
            }
        }
    )
) {
    override val tag: String = TilesList.wheat_block

    override fun canPlace(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController): Boolean {
        val typeBelow = dimension.gameCycle.mapApi.getTileType(dimension, x, y - 1)
        return typeBelow?.tag == TilesList.farmland_block
    }
}
