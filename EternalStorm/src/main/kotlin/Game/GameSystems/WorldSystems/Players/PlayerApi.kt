package la.vok.Game.GameSystems.WorldSystems.Players

import la.vok.Game.GameContent.Entities.Entities.PlayerEntity
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.Core.CoreContent.Windows.Modules.IUiModule
import la.vok.Game.ClientContent.Windows.WGamePanel
import la.vok.Game.Windows.GameUI.Modules.InventoryModule

class PlayerApi(val gameCycle: GameCycle) {
    private val playerEntities = mutableMapOf<Long, PlayerEntity>()

    /**
     * Registers a player entity in the global registry.
     */
    fun registerPlayer(id: Long, entity: PlayerEntity) {
        playerEntities[id] = entity
    }

    /**
     * Removes a player from the registry.
     */
    fun removePlayer(id: Long) {
        playerEntities.remove(id)
    }

    /**
     * Gets a player entity by their ID across all dimensions.
     */
    fun getPlayer(id: Long): PlayerEntity? {
        return playerEntities[id]
    }

    /**
     * Returns all currently active players.
     */
    fun getAllPlayers(): Collection<PlayerEntity> {
        return playerEntities.values
    }

    /**
     * Finds the nearest player to a point within a max distance.
     */
    fun getNearestPlayer(pos: Vec2, maxDist: Float): PlayerEntity? {
        return playerEntities.values
            .filter { (it.position - pos).length() <= maxDist }
            .minByOrNull { (it.position - pos).length() }
    }

    /**
     * Checks if a player with this ID exists.
     */
    fun isPlayerOnline(id: Long): Boolean {
        return playerEntities.containsKey(id)
    }

    /**
     * Sets player equipment in a specific slot.
     */
    fun setPlayerEquipment(playerId: Long, slot: String, item: la.vok.Game.GameContent.Items.Other.Item?) {
        playerEntities[playerId]?.equipmentModule?.setEquipment(slot, item)
    }

    /**
     * Gets player equipment from a specific slot.
     */
    fun getPlayerEquipment(playerId: Long, slot: String): la.vok.Game.GameContent.Items.Other.Item? {
        return playerEntities[playerId]?.equipmentModule?.getEquipment(slot)
    }

    /**
     * Opens player inventory with animation.
     */
    fun openInventory(playerId: Long) {
        val panel = gameCycle.gameController.wGamePanel ?: return
        val playerControl = gameCycle.gameController.playerControl
        if (!playerControl.isInventoryOpen) {
            panel.moduleManager.getModule<InventoryModule>("inventory")?.toggleInventory()
        }
    }

    /**
     * Opens a specific block interface (e.g. chest, workbench) for the player.
     */
    fun openBlockInterface(playerId: Long, module: IUiModule) {
        val panel = gameCycle.gameController.wGamePanel ?: return
        openInventory(playerId)
        panel.moduleManager.openBlockInterface(module)
    }
}
