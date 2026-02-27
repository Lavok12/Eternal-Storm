package la.vok.Game.GameContent.HandItems.Items

import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.v

class SpearHandItem(component: HandItemComponent) : HandItem(
    component,
    HandItemDescriptor(
        spriteName = "spear.png",
        spriteSize = 4 v 4,
        useDuration = 60f,
        useStageStep = 5f,
        spriteAngle = Math.PI.toFloat() / 4f,
        animationType = AnimationType.Thrust(
            startOffset = -1f,
            maxOffset = 1.8f,
            peakScale = 0.05f,
            shakeAmplitude = 0.1f
        ),

        rightAction = UseAction.PrintOnStart("Spear block!")
    )
)