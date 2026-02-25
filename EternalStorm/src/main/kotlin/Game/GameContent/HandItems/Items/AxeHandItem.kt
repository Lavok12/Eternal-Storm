package la.vok.Game.GameContent.HandItems.Items

import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

class AxeHandItem(component: HandItemComponent) : HandItem(
    component,
    HandItemDescriptor(
        spriteName = "axe.png",
        spriteSize = 3 v 3,
        useDuration = 120f,
        useStageStep = 8f,
        animationType = AnimationType.Swing()
    )
)