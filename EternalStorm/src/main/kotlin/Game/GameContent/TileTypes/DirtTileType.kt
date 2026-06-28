package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.State.AppState

class DirtTileType() : AbstractTileType() {
    override val tag: String = TilesList.dirt_block
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = AppState.res("dirtTexture.jpg")
    override val drop: DropEntry = SingleDrop(ItemsList.dirt_block)
    override val tags: Set<String> = setOf(BlockTags.PLOWABLE)
    override val hasGravity: Boolean = true

    override fun getPollutionStrength(
        targetBlock: la.vok.Game.GameSystems.WorldSystems.Map.IBlockType,
        dimension: la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension,
        x: Int, y: Int
    ): Float {
        if (targetBlock.tag == this.tag) return 0f
        if (targetBlock.tag == TilesList.grass_block) return 0.5f
        if (targetBlock.tag == TilesList.sand_block) return 0.5f
        return 1f
    }
}