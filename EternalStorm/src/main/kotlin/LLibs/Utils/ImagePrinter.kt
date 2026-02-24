package la.vok.LavokLibrary.Utils

import processing.core.PImage
import processing.core.PSurface
import java.awt.Frame
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.print.Printable
import java.awt.print.PrinterJob
import javax.swing.JDialog
import javax.swing.SwingUtilities
import kotlin.concurrent.thread

object ImagePrinter {
    fun print(img: PImage, surface: PSurface) {
        // Копируем картинку сразу в основной поток, пока OpenGL не занят
        val bimg = img.native as? BufferedImage ?: return

        thread {
            try {
                val nativeWindow = surface.native

                // 1. Сворачиваем игру
                SwingUtilities.invokeLater {
                    try {
                        // Для P2D/JOGL используем setVisible или setState
                        val setVisibleMethod = nativeWindow.javaClass.getMethod("setVisible", Boolean::class.java)
                        setVisibleMethod.invoke(nativeWindow, false)
                    } catch (e: Exception) {
                        if (nativeWindow is Frame) nativeWindow.extendedState = Frame.ICONIFIED
                    }
                }

                Thread.sleep(600) // Пауза, чтобы система успела скрыть FullScreen окно

                val job = PrinterJob.getPrinterJob()
                job.setPrintable(Printable { graphics, pageFormat, pageIndex ->
                    if (pageIndex > 0) return@Printable Printable.NO_SUCH_PAGE
                    val g2d = graphics as Graphics2D

                    val scale = Math.min(pageFormat.imageableWidth / bimg.width, pageFormat.imageableHeight / bimg.height)
                    val w = (bimg.width * scale).toInt()
                    val h = (bimg.height * scale).toInt()
                    val x = (pageFormat.imageableWidth - w) / 2
                    val y = (pageFormat.imageableHeight - h) / 2

                    g2d.translate(pageFormat.imageableX + x, pageFormat.imageableY + y)
                    g2d.drawImage(bimg, 0, 0, w, h, null)
                    Printable.PAGE_EXISTS
                })

                // 2. ОТКРЫВАЕМ ДИАЛОГ ЧЕРЕЗ "ЯКОРЬ"
                var isSelected = false
                SwingUtilities.invokeAndWait {
                    // Создаем невидимое окно, которое станет родителем для принтера
                    val anchor = JDialog()
                    anchor.isAlwaysOnTop = true
                    anchor.setUndecorated(true)
                    anchor.setSize(0, 0)
                    anchor.setLocationRelativeTo(null)
                    anchor.isVisible = true // Оно пустое, но оно "есть" на экране

                    // Вызываем диалог печати, привязанный к этому якорю
                    // Это заставляет Windows вывести его поверх всего
                    isSelected = job.printDialog()

                    anchor.dispose() // Закрываем якорь
                }

                if (isSelected) {
                    job.print()
                }

            } catch (e: Exception) {
                System.err.println("Ошибка при печати: ${e.message}")
            } finally {
                // 3. Возвращаем игру назад
                SwingUtilities.invokeLater {
                    try {
                        val nativeWindow = surface.native
                        val setVisibleMethod = nativeWindow.javaClass.getMethod("setVisible", Boolean::class.java)
                        setVisibleMethod.invoke(nativeWindow, true)

                        if (nativeWindow is Frame) {
                            nativeWindow.extendedState = Frame.NORMAL
                            nativeWindow.toFront()
                            nativeWindow.requestFocus()
                        }
                    } catch (e: Exception) {
                        surface.setAlwaysOnTop(true)
                    }
                }
            }
        }
    }
}