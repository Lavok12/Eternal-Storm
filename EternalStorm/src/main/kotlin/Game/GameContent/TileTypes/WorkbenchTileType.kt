package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.State.AppState
import la.vok.Game.GameContent.Items.Other.SingleDrop
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionContext
import la.vok.Game.GameSystems.WorldSystems.Map.BlockInteractionType
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p

class WorkbenchTileType : AbstractTileType() {
    override val texture: String = AppState.res("workbench.png")
    override val width: Int = 3
    override val height: Int = 2
    override val tag: String = TilesList.workbench
    override var collisionType = CollisionType.NONE
    override val blockStrength = 10
    override val maxHp = 50
    override val drop = SingleDrop(ItemsList.workbench)
    override val placeOffset: LPoint = -1 p 0

    override val renderConfig = TileRenderConfig(useSquareRender = true, sizeMultiplier = 1.6f, renderDelta = Vec2(0f, 0.49f), renderWallsBehind = true, AOShadow = false)

    init {
        val reaction = { context: BlockInteractionContext ->
            println("=== Workbench Interaction ===")
            println("Block: ${context.x}, ${context.y}")
            println("Interactor: ${context.interactor?.entityType?.tag ?: "Unknown"}")
        }

        addInteractionReaction(BlockInteractionType.LEFT, reaction)
        addInteractionReaction(BlockInteractionType.RIGHT, reaction)
        addInteractionReaction(BlockInteractionType.MIDDLE, reaction)
    }
}