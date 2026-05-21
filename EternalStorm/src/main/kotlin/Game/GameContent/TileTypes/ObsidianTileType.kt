package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Tiles.System.AbstractTileType

class ObsidianTileType : AbstractTileType() {
    override val tag: String = TilesList.obsidian_block
    override val blockStrength: Int = 50
    override val maxHp: Int = 1000
    override val texture: String = "obsidian.png" // Placeholder texture name
}
