package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.AbstractWallType
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2

class FallingBlockEntity(
    blockType: IBlockType,
    gameCycle: GameCycle
) : BlockEntity(blockType, AbstractEntityType.FallingBlockEntityType, gameCycle) {

    private var hardened = false

    override fun physicUpdate() {
        super.physicUpdate()
        if (isDead) return

        if (downTrigger?.blocksCollision == true) {
            harden()
        }
    }

    private fun harden() {
        val w = if (blockType is AbstractTileType) blockType.width else 1
        val h = if (blockType is AbstractTileType) blockType.height else 1

        val targetX = Math.round(position.x - w / 2f + 0.5f)
        val targetY = Math.round(position.y - h / 2f + 0.5f)

        var hasFullTileBelow = false
        for (dx in 0 until w) {
            val checkX = targetX + dx
            val checkY = targetY - 1
            val tileBelow = gameCycle.mapApi.getTileType(dimension!!, checkX, checkY)
            if (tileBelow != null && tileBelow.collisionType == la.vok.Game.GameController.CollisionType.FULL) {
                hasFullTileBelow = true
                break
            }
        }

        val canHarden = if (blockType is AbstractTileType) {
            hasFullTileBelow && gameCycle.mapApi.canPlaceTile(dimension!!, blockType, targetX, targetY)
        } else if (blockType is AbstractWallType) {
            gameCycle.mapApi.canPlaceWall(dimension!!, blockType, targetX, targetY)
        } else {
            false
        }

        if (canHarden) {
            hardened = true
            if (blockType is AbstractTileType) {
                gameCycle.mapApi.generateTile(dimension!!, blockType, targetX, targetY)
            } else if (blockType is AbstractWallType) {
                gameCycle.mapApi.generateWall(dimension!!, blockType, targetX, targetY)
            }
        } else {
            hardened = false
        }

        gameCycle.entityApi.killInSystem(dimension!!, this)
    }

    override fun drop() {
        if (!hardened) {
            gameCycle.itemsApi.spawnDropTable(dimension!!, blockType.drop, position, true)
        }
    }
}
