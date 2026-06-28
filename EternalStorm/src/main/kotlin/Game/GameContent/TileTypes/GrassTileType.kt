package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.State.AppState

class GrassTileType() : AbstractTileType() {
    override val tag: String = TilesList.grass_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 12
    override val texture: String = AppState.res("grassTexture.jpg")
    override val drop: DropEntry = SingleDrop(ItemsList.grass_block)
    override val tags: Set<String> = setOf(BlockTags.PLOWABLE)

    override fun getPollutionStrength(
        targetBlock: la.vok.Game.GameSystems.WorldSystems.Map.IBlockType,
        dimension: AbstractDimension,
        x: Int, y: Int
    ): Float {
        if (targetBlock.tag == this.tag) return 0f
        if (targetBlock.tag == TilesList.dirt_block) return 0.5f
        if (targetBlock.tag == TilesList.sand_block) return 0.5f
        return 1f
    }

    override fun onUpdate(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController) {
        super.onUpdate(x, y, dimension, mapController)
        val api = dimension.gameCycle.mapApi
        if (api.hasFullBlockAbove(dimension, x, y)) {
            api.setTileType(dimension, TilesList.dirt_block, x, y)
            repeat(4) {
                spawnTileParticle(x, y, dimension, mapController)
            }
        }
    }
}
