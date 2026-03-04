package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.WallContext
import la.vok.Game.GameSystems.WorldSystems.Map.WallPlaceType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2

abstract class AbstractWallType : IBlockType {

    override val tag: String = ""
    override val blockStrength: Int = 0
    override val maxHp: Int = 0
    override val texture: String = ""
    override val drop: DropEntry = NothingDrop

    open val placeType: WallPlaceType = WallPlaceType.NEAR_WALL_OR_TILE

    open fun canPlace(context: WallContext): Boolean = true

    open fun render(
        wallContext: WallContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            position,
            size
        )
    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun damage(x: Int, y: Int, damage: Int, wallContext: WallContext, mapController: MapController) {
        mapController.gameCycle.particlesApi.spawnWallParticles(this, mapController.gameCycle.mapApi.getBlockPos(x, y), 3)
    }

    open fun onMined(x: Int, y: Int, mineData: MineData, wallContext: WallContext, mapController: MapController) {
        val pos = mapController.mapApi.getBlockPos(x, y)
        mapController.gameCycle.itemsApi.spawnDropTable(drop, pos, true)
    }

    open fun onRemoved(x: Int, y: Int, wallContext: WallContext, reason: Any? = null) {}
}