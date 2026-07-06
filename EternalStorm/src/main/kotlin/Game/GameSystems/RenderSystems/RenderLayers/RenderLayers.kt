package la.vok.Game.ClientContent.RenderSystem.RenderLayers

object RenderLayers {

    /**
     * Группа определяет, к какому этапу пайплайна рендеринга относится слой.
     * Порядок enum-значений соответствует порядку отрисовки (от самого дальнего к самому ближнему).
     */
    enum class LayerGroup {
        /** Фон — рисуется до тайлов. Параллакс < 1.0 (чем меньше, тем «дальше» от камеры) */
        BACKGROUND,
        /** Игровой бэкграунд — рисуется перед тайлами с нормальной камерой (parallax = 1.0) */
        LEVEL_BACKGROUND,
        /** Игровой форграунд — рисуется поверх тайлов/актёров с нормальной камерой (parallax = 1.0) */
        LEVEL_FOREGROUND,
        /** Передний план — рисуется перед уровнем с обратным параллаксом > 1.0 (объекты «перед» камерой) */
        FOREGROUND,
        /** Оверлей / HUD — рисуется поверх всего */
        OVERLAY,
    }

    /**
     * Единый enum всех слоёв рендеринга.
     *
     * @param group  Группа (этап пайплайна) данного слоя.
     * @param parallax  Множитель параллакса:
     *   - < 1.0  → фон движется медленнее камеры (удалённые объекты).
     *   - = 1.0  → движется синхронно с камерой (стандарт).
     *   - > 1.0  → движется быстрее камеры (объекты «перед» игроком).
     */
    enum class Main(val group: LayerGroup, val parallax: Float = 1.0f) {

        // ============================================================
        //  BACKGROUND — дальний фон (параллакс < 1.0)
        //  Порядок: от самого дальнего к самому ближнему
        // ============================================================

        /** Небо / космос / самый дальний фон. Практически не движется. */
        SKY             (LayerGroup.BACKGROUND, 0.001f),

        /** Звёзды / облака верхнего яруса */
        STARS           (LayerGroup.BACKGROUND, 0.005f),

        /** Горы / огромные дальние структуры */
        FAR_MOUNTAINS   (LayerGroup.BACKGROUND, 0.02f),

        /** Дальние облака (медленные) */
        CLOUDS_FAR_1    (LayerGroup.BACKGROUND, 0.04f),
        CLOUDS_FAR_2    (LayerGroup.BACKGROUND, 0.055f),

        /** Средние облака */
        CLOUDS_MID_1    (LayerGroup.BACKGROUND, 0.08f),
        CLOUDS_MID_2    (LayerGroup.BACKGROUND, 0.10f),

        /** Ближние облака (быстрые) */
        CLOUDS_NEAR_1   (LayerGroup.BACKGROUND, 0.14f),
        CLOUDS_NEAR_2   (LayerGroup.BACKGROUND, 0.18f),

        /** Дальний лес / холмы */
        FOREST_FAR_1    (LayerGroup.BACKGROUND, 0.25f),
        FOREST_FAR_2    (LayerGroup.BACKGROUND, 0.30f),

        /** Средний лес */
        FOREST_MID_1    (LayerGroup.BACKGROUND, 0.40f),
        FOREST_MID_2    (LayerGroup.BACKGROUND, 0.50f),

        /** Ближний лес / кусты / декор позади уровня */
        FOREST_NEAR_1   (LayerGroup.BACKGROUND, 0.62f),
        FOREST_NEAR_2   (LayerGroup.BACKGROUND, 0.75f),

        /** Детали ближнего фона (трава, камни, тени) */
        BG_DETAIL_1     (LayerGroup.BACKGROUND, 0.82f),
        BG_DETAIL_2     (LayerGroup.BACKGROUND, 0.88f),
        BG_DETAIL_3     (LayerGroup.BACKGROUND, 0.94f),

        // ============================================================
        //  LEVEL_BACKGROUND — игровые слои перед тайлами (parallax = 1.0)
        //  Совместимость с предыдущими именами: B1..B5
        // ============================================================
        B1(LayerGroup.LEVEL_BACKGROUND),
        B2(LayerGroup.LEVEL_BACKGROUND),
        B3(LayerGroup.LEVEL_BACKGROUND),
        B4(LayerGroup.LEVEL_BACKGROUND),
        B5(LayerGroup.LEVEL_BACKGROUND),

        // ============================================================
        //  LEVEL_FOREGROUND — игровые слои поверх тайлов/актёров (parallax = 1.0)
        //  Совместимость с предыдущими именами: A1..A5
        // ============================================================
        A1(LayerGroup.LEVEL_FOREGROUND),
        A2(LayerGroup.LEVEL_FOREGROUND),
        A3(LayerGroup.LEVEL_FOREGROUND),
        A4(LayerGroup.LEVEL_FOREGROUND),
        A5(LayerGroup.LEVEL_FOREGROUND),

        // ============================================================
        //  FOREGROUND — передний план (обратный параллакс > 1.0)
        //  Рисуется после жидкостей. Движется БЫСТРЕЕ камеры.
        // ============================================================

        /** Лёгкая дымка / атмосфера */
        FG_HAZE         (LayerGroup.FOREGROUND, 1.08f),

        /** Дальние ветки и листья */
        FG_LEAVES_FAR   (LayerGroup.FOREGROUND, 1.15f),

        /** Средние ветки */
        FG_BRANCHES_MID (LayerGroup.FOREGROUND, 1.25f),

        /** Крупные ближние ветки и листья */
        FG_LEAVES_NEAR  (LayerGroup.FOREGROUND, 1.38f),

        /** Туман / пыль / спецэффекты переднего плана */
        FG_FOG_1        (LayerGroup.FOREGROUND, 1.50f),
        FG_FOG_2        (LayerGroup.FOREGROUND, 1.60f),

        // ============================================================
        //  OVERLAY — поверх всего (HUD, UI, эффекты экрана)
        //  Совместимость с предыдущими именами: C1..C5
        // ============================================================
        C1(LayerGroup.OVERLAY),
        C2(LayerGroup.OVERLAY),
        C3(LayerGroup.OVERLAY),
        C4(LayerGroup.OVERLAY),
        C5(LayerGroup.OVERLAY),
    }
}