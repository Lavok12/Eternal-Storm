package la.vok.Game.ClientContent.RenderSystem.RenderLayers

object RenderLayers {

    enum class Main {
        // Небольшой параллакс
        X, XM1, XM2, XM3, XM4, XM5,

        // Базовые слои
        B1, B2, B3, B4, B5,

        // Игрок
        PLAYER,

        // Передние слои
        F1, F2, F3, F4, F5,

        // Финальный слой
        Y
    }


    enum class ColorMap {
        X, C1, C2, C3, C4, C5
    }

    enum class GlobalLight {
        R5, R4, R3, R2, R1, C
    }

    enum class BackGround {
        A1, A2, A3,
        B1, B2, B3,
        C1, C2, C3,
    }

    enum class BackGroundColorMap {
        C1, C2, C3, C4, C5
    }
}