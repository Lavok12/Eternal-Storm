package la.vok.Game.GameContent.Items.ItemTypes.Liquid

import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.LiquidList
import la.vok.Game.GameContent.Items.Items.LiquidBucketItemType

class WaterBucketItemType : LiquidBucketItemType(LiquidList.water) {
    override val tag: String = ItemsList.water_bucket
}

class LavaBucketItemType : LiquidBucketItemType(LiquidList.lava) {
    override val tag: String = ItemsList.lava_bucket
}

class AcidBucketItemType : LiquidBucketItemType(LiquidList.acid) {
    override val tag: String = ItemsList.acid_bucket
}
