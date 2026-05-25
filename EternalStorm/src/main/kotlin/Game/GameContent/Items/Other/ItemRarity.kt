package la.vok.Game.GameContent.Items.Other

import la.vok.LavokLibrary.Vectors.LColor

enum class ItemRarity(val label: String, val color: LColor) {
    COMMON("Обычный", LColor(200f, 200f, 200f)),
    UNCOMMON("Необычный", LColor(100f, 255f, 100f)),
    RARE("Редкий", LColor(100f, 150f, 255f)),
    EPIC("Эпический", LColor(200f, 100f, 255f)),
    LEGENDARY("Легендарный", LColor(255f, 200f, 50f))
}
