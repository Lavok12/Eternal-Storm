package la.vok.Game.GameContent.WallTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.ContentList.WallList
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.WorldSystems.Map.WallContext
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

class DirtWallType() : AbstractWallType() {
    override val tag: String = WallList.dirt_wall
    override val blockStrength: Int = 10
    override val maxHp: Int = 10
    override val texture: String = "dirtTexture.jpg"
    override val drop: DropEntry = SingleDrop(ItemsList.dirt_wall)

    override fun render(
        wallContext: WallContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        lg.setTint(170f)
        super.render(wallContext, lg, position, size, gameController)
        lg.noTint()
    }
}