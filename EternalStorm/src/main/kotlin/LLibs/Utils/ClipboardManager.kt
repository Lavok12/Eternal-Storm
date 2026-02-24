package la.vok.LavokLibrary.Utils

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

object ClipboardManager {

    fun copy(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), null)
    }

    fun paste(): String {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        return try {
            clipboard.getData(DataFlavor.stringFlavor) as? String ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}
