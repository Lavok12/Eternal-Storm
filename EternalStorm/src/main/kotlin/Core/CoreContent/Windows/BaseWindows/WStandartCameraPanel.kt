package la.vok.Core.CoreContent.Windows.WindowsStorage.Templates

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.CoreContent.Camera.SoftCamera
import la.vok.Core.CoreControllers.CoreContent.Windows.DrawElements.GridLayer
import la.vok.Core.CoreControllers.CoreContent.Windows.DrawElements.SceneDrawElements
import la.vok.Core.CoreControllers.MainRender
import la.vok.Core.CoreControllers.WindowsManager
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.Windows.Message.WindowMessage
import processing.event.MouseEvent

open class WStandartCameraPanel(windowsManager: WindowsManager) : WStandartPanel(windowsManager) {
    
    open var camera: Camera = SoftCamera(smoothness = 0.99999f)

    open var DEFAULT_GRID = listOf(
        GridLayer(512f, 40f, 3f),
        GridLayer(256f, 40f, 2f),
        GridLayer(128f, 40f, 1f),
        GridLayer(64f,  34f, 1f),
        GridLayer(32f,  22f, 1f),
        GridLayer(16f,  14f, 1f)
    )
    override fun draw(mainRender: MainRender) {
        super.draw(mainRender)
        SceneDrawElements.drawComplexGrid(
            lg,
            Vec2(0f),
            logicalSize,
            camera,
            autoStepRange = true,
            layers = DEFAULT_GRID
        )
    }

    override fun preUpdate() {
        camera.updateCamera()
        super.preUpdate()
    }

    override fun postDraw(mainRender: MainRender) {
        super.postDraw(mainRender)
    }

    override fun rightDragCaptured(position: Vec2, oldPosition: Vec2, start: Vec2) {
        camera.moveCamera(position - oldPosition)
    }

    override fun mouseWheelCaptured(position: Vec2, event: MouseEvent) {
        camera.zoomRelative(event.count.toFloat() * 1.5f, position)
    }

    override fun handleMessage(windowMessage: WindowMessage) {}
}