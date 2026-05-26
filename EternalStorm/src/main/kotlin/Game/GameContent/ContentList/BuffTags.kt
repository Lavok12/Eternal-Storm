package la.vok.Game.GameContent.ContentList

import la.vok.State.AppState

object BuffTags {
    val SPEED = AppState.tag("speed")
    val REGENERATION = AppState.tag("regeneration")
    val RAGE = AppState.tag("rage")
}

object StatTags {
    val MAX_HP = AppState.tag("stat_max_hp")
    val DAMAGE = AppState.tag("stat_damage")
    val SPEED = AppState.tag("stat_speed")
    val JUMP_POWER = AppState.tag("stat_jump_power")
    val DIGGING_TILE = AppState.tag("stat_digging_tile")
    val DIGGING_WALL = AppState.tag("stat_digging_wall")
    val RANGED_SPEED = AppState.tag("stat_ranged_speed")
    val MELEE_SPEED = AppState.tag("stat_melee_speed")
    val PLACING_BLOCK = AppState.tag("stat_placing_block")
    val PLACING_WALL = AppState.tag("stat_placing_wall")
    val WEAPON_SIZE = AppState.tag("stat_weapon_size")
    val KB_RES = AppState.tag("stat_kb_res")
    val REGEN = AppState.tag("stat_regen")
    val GRAVITY = AppState.tag("stat_gravity")
    val RESISTANCE = AppState.tag("stat_resistance")
}
