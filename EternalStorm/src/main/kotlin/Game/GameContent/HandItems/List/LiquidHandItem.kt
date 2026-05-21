package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

class LiquidHandItem(item: Item, component: HandItemComponent, val liquidTag: String) : HandItem(
    item,
    component,
    HandItemDescriptor(
        spriteName = "texture_64x64 (2).png",
        spriteSize = 1.0f v 1.0f,
        useDuration = 20f,
        animationType = AnimationType.Swing(),
        autoRepeat = true,
        leftAction = UseAction.Custom(
            onEnd = {
                val dim = entity.dimension ?: return@Custom
                val mousePos = gameController.coreController.mouseInput.logicalPosition
                val worldPos = gameController.mainCamera.toWorldPos(mousePos)
                gameCycle.liquidApi.setLiquid(dim, worldPos.x.toInt(), worldPos.y.toInt(), liquidTag, 255)
            }
        )
    )
)
