package la.vok.Game.GameContent.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.TileData.FrameTileData
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.v

class FrameTileType : AbstractTileType() {
    override val texture: String = AppState.res("frame.png")
    override val width: Int = 2
    override val height: Int = 2
    override val tag: String = TilesList.frame_block
    override var collisionType = CollisionType.NONE
    override val blockStrength = 0
    override val maxHp = 1
    override val drop: DropEntry = SingleDrop(ItemsList.frame)
    override val tags: Set<String> = setOf(BlockTags.NO_SHADOW)
    override val placeType = TilePlaceType.NEAR_TILE_OR_ON_WALL

    override val renderConfig = TileRenderConfig(useSquareRender = true, sizeMultiplier = 1.2f, renderWallsBehind = true)

    init {
        addInteractionReaction(BlockInteractionType.RIGHT) {
            var item = it.mapApi.gameCycle.gameController.playerControl.getPlayerEntity()?.handItemComponent?.currentHandItem?.item
            var frame = it.mapApi.getTileData(it.dimension, it.x, it.y)
            if (frame == null) return@addInteractionReaction
            (frame as FrameTileData).setItem(item)
        }
    }
    override fun createTileData(x: Int, y: Int, dimension: AbstractDimension): AbstractTileData? {
        return FrameTileData(x, y, dimension)
    }

    override fun render(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        super.render(pointX, pointY, lg, positionX, positionY, sizeX, sizeY, dimension, gameController)
        var frame = gameController.gameCycle.mapApi.getTileData(dimension, pointX, pointY)
        if (frame == null) return
        (frame as FrameTileData).getItem()?.cellRender(lg, positionX v positionY, sizeX v sizeY)
    }
}
