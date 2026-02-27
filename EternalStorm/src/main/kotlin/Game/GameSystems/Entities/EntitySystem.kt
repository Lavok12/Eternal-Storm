package la.vok.Game.GameSystems.Entities

import la.vok.Game.GameContent.Entities.Entities.Entity
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState

class EntitySystem(var entityController: EntityController) {
    var entities = HashSet<Entity>()
    var idMap = HashMap<Long, Entity>()

    private val addBuffer    = ArrayDeque<Entity>()
    private val deleteBuffer = ArrayDeque<Entity>()
    private val killBuffer   = ArrayDeque<Entity>()

    var ids: Long = 1L

    fun setId(entity: Entity) {
        entity.systemId = ids
        ids++
    }

    // ─── Add ─────────────────────────────────────────────────────────────────

    fun add(entity: Entity, pos: Vec2) {
        AppState.logger.info("Add Entity $entity, $pos")
        setId(entity)
        entity.position = pos.copy()
        entities.add(entity)
        idMap[entity.systemId] = entity
        entityController.entityApi.showEntity(entity)
    }

    fun add(entity: Entity) {
        AppState.logger.info("Add Entity $entity")
        setId(entity)
        entities.add(entity)
        idMap[entity.systemId] = entity
        entityController.entityApi.showEntity(entity)
    }

    fun add(id: Long, entity: Entity, pos: Vec2) {
        AppState.logger.info("Add Entity $entity, $pos, $id")
        entity.systemId = id
        entity.position = pos.copy()
        entities.add(entity)
        idMap[entity.systemId] = entity
        entityController.entityApi.showEntity(entity)
    }

    fun add(id: Long, entity: Entity) {
        AppState.logger.info("Add Entity $entity, $id")
        entity.systemId = id
        entities.add(entity)
        idMap[entity.systemId] = entity
        entityController.entityApi.showEntity(entity)
    }

    // ─── Delete / Kill (буферизованные) ──────────────────────────────────────

    fun delete(entity: Entity) {
        AppState.logger.info("Delete Entity $entity")
        deleteBuffer.add(entity)
    }

    fun delete(id: Long) {
        AppState.logger.info("Delete Entity $id")
        idMap[id]?.let { deleteBuffer.add(it) }
    }

    fun kill(entity: Entity) {
        AppState.logger.info("Kill Entity $entity")
        killBuffer.add(entity)
    }

    fun kill(id: Long) {
        AppState.logger.info("Kill Entity $id")
        idMap[id]?.let { killBuffer.add(it) }
    }

    // ─── Buffer flush ─────────────────────────────────────────────────────────

    fun flushBuffers() {
        var deleted = 0
        var killed = 0

        while (deleteBuffer.isNotEmpty()) {
            val entity = deleteBuffer.removeFirst()
            entityController.entityApi.hideEntity(entity)
            idMap.remove(entity.systemId)
            entities.remove(entity)
            deleted++
        }

        while (killBuffer.isNotEmpty()) {
            val entity = killBuffer.removeFirst()
            entityController.entityApi.hideEntity(entity)
            idMap.remove(entity.systemId)
            entities.remove(entity)
            entity.kill()
            killed++
        }

        if (deleted > 0) AppState.logger.info("Deleted $deleted entities")
        if (killed > 0)  AppState.logger.info("Killed $killed entities")
    }

    // ─── Query ───────────────────────────────────────────────────────────────

    fun isExist(entity: Entity): Boolean = entities.contains(entity)
    fun isExist(id: Long): Boolean = idMap.containsKey(id)
}