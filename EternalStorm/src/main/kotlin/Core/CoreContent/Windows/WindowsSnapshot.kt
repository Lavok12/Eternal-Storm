package la.vok.Core.CoreContent.Windows

import la.vok.Core.CoreContent.Windows.WindowsStorage.Templates.AbstractWindow

data class WindowsSnapshot(
    var windowsList: ArrayList<AbstractWindow>,
    var destroyList: ArrayList<AbstractWindow>,
    var appendList: ArrayList<AbstractWindow>,
    var blockStack: ArrayDeque<AbstractWindow>) {
}