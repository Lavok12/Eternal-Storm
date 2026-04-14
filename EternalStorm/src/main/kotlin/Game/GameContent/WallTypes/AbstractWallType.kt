package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.WallPlaceType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

abstract class AbstractWallType : IBlockType {

    override val tag: String = ""
    override val blockStrength: Int = 0
    override val maxHp: Int = 0
    override val drop: DropEntry = NothingDrop
    override val tags: Set<String> = emptySet()

    open fun hasTag(tag: String): Boolean = tag in tags

    open val placeType: WallPlaceType = WallPlaceType.NEAR_WALL_OR_TILE

    open fun canPlace(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController): Boolean = true

    open fun render(
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
        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            positionX, positionY,
            sizeX, sizeY
        )
    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun damage(x: Int, y: Int, damage: Int, dimension: AbstractDimension, mapController: MapController) {
        mapController.dimension.gameCycle.particlesApi.buildWall(dimension, this).atBlock(x, y).count(3).randomSpeed(1f).spawn()
    }

    open fun onMined(x: Int, y: Int, mineData: MineData, dimension: AbstractDimension, mapController: MapController) {
        val pos = mapController.dimension.gameCycle.mapApi.getBlockPos(x, y)
        mapController.dimension.gameCycle.itemsApi.spawnDropTable(dimension, drop, pos, true)
    }

    open fun onRemoved(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController, reason: Any? = null) {}
}