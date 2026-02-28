package la.vok.Game.GameContent.Items.ItemTypes.Blocks

import la.vok.Game.GameContent.Items.Items.UniversalBlockItem
import la.vok.Game.GameContent.Items.Other.AbstractItemType
import la.vok.Game.GameContent.Items.Other.UsingVariants
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.ItemsList
import la.vok.Game.GameContent.TilesList
import la.vok.Game.GameController.GameCycle

class GrassItemType : BaseTileItemType() {
    override val tag: String = ItemsList.grass_block
    override val sprite = "grassTexture.jpg"
    override val usingVariants = UsingVariants.PlaceTile(TilesList.grass_block)
}