package la.vok.Game.GameContent.Items.Other

sealed class UsingVariants {
    object Custom : UsingVariants()
    data class PlaceTile(val tileTag: String) : UsingVariants()
}