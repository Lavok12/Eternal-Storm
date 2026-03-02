package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.HpRender
import la.vok.Game.GameContent.Entities.EntityRender.ItemRenderEntity
import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameController.GameCycle
import la.vok.Game.GameSystems.EntityComponents.Collision.CollisionDetector
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxComponent
import la.vok.Game.GameSystems.EntityComponents.Collision.HitboxTypes
import la.vok.Game.GameSystems.EntityComponents.MobInventory
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.State.AppState

@Suppress("UNCHECKED_CAST")
open class ItemEntity(gameCycle: GameCycle) : Entity(AbstractEntityType.ItemEntityType, gameCycle) {
    override var renderEntity: RenderObjectInterface? = ItemRenderEntity(getRenderLayer())

    private val itemRender get() = renderEntity as? ItemRenderEntity
    override var hpRender: HpRender?
        get() = null
        set(value) {}

    init {
        inventory = MobInventory(this, 1)
        hpBody = null
    }

    val item: Item?
        get() = inventory?.itemContainer?.getItem(0)

    fun setItem(item: Item?) {
        inventory?.itemContainer?.setItem(0, item)
    }

    override fun takeDamage(damage: DamageData, hitboxComponent: HitboxComponent): Boolean {
        return false
    }

    // --- Merge trigger ---

    private val mergeCheckInterval = 120L
    private var mergeTrigger: HitboxComponent? = null
    private var mergeDetector: CollisionDetector? = null

    override fun createCustomHitboxes() {
        mergeTrigger = addHitbox("merge trigger", HitboxTypes.ONLY_TRIGGER, null).also {
            it.size = AppState.itemsMergeSize
            it.ignoreCollision = true
        }
        mergeDetector = CollisionDetector(
            entity = this,
            hitboxComponent = mergeTrigger!!,
            gameCycle = gameCycle,
            tagFilter = TagFilter.HasAll(listOf(EntityTags.item))
        )
    }

    private fun tryMergeWithNearby() {
        val currentItem = item ?: return

        mergeDetector?.activeContacts?.toList()?.forEach { otherHitbox ->
            val other = otherHitbox.entity as? ItemEntity ?: return@forEach
            if (other === this) return@forEach
            if (!gameCycle.entityApi.isExist(other)) return@forEach

            val otherItem = other.item ?: return@forEach
            if (!currentItem.canStackable(otherItem)) return@forEach

            val myCount = currentItem.count
            val otherCount = otherItem.count

            // Более крупная поглощает меньшую
            val (absorber, absorbed) = if (myCount >= otherCount) {
                this to other
            } else {
                other to this
            }

            val absorberItem = absorber.item ?: return@forEach
            val absorbedItem = absorbed.item ?: return@forEach

            val canFit = absorberItem.leftToStack()
            if (canFit <= 0) return@forEach

            val toTransfer = minOf(absorbedItem.count, canFit)
            absorberItem.count += toTransfer
            absorbedItem.count -= toTransfer

            if (absorbedItem.count <= 0) {
                gameCycle.entityApi.killInSystem(absorbed)
            }

            // Если поглотили нас — выходим
            if (absorbed === this) return
        }
    }

    override fun physicUpdate() {
        super.physicUpdate()
        item?.physicUpdate(this)
        mergeDetector?.update()

        if (physicTicks % mergeCheckInterval == 0L) {
            tryMergeWithNearby()
        }
    }

    override fun logicalUpdate() {
        super.logicalUpdate()
        item?.logicalUpdate(this)
    }

    override fun renderUpdate() {
        super.renderUpdate()
        itemRender?.item = item
        item?.renderUpdate(this)
    }

    override fun show() {
        super.show()
    }

    override fun hide() {
        super.hide()
    }
}