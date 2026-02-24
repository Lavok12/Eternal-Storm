package la.vok.Windows.Message

import la.vok.LavokLibrary.Utils.MetaStorage

class WindowMessage(
    var msgType: String,
    var sender: Long,
    onCreate: WindowMessage.() -> Unit = {}
) : MetaStorage() {

    var targetId: Long = 0L
    var targetTag: String = ""

    init {
        this.onCreate()
    }

    constructor(
        msgType: String,
        sender: Long,
        targetId: Long,
        onCreate: WindowMessage.() -> Unit = {}
    ) : this(msgType, sender, onCreate) {
        this.targetId = targetId
        this.targetTag = ""
    }

    constructor(msgType: String, onCreate: WindowMessage.() -> Unit = {})
            : this(msgType, -1L, onCreate)

    constructor(
        msgType: String,
        sender: Long,
        targetTag: String,
        onCreate: WindowMessage.() -> Unit = {}
    ) : this(msgType, sender, onCreate) {
        this.targetId = 0L
        this.targetTag = targetTag
    }

    fun isTargetId(): Boolean = targetId != 0L
    fun isTargetTag(): Boolean = targetTag.isNotEmpty()

    fun targetType(): TargetType =
        when {
            isTargetId() -> TargetType.ID
            isTargetTag() -> TargetType.TAG
            else -> TargetType.NONE
        }

    enum class TargetType { NONE, ID, TAG }

    fun toPrintString(): String {
        return "MESSAGE msgType{$msgType}, sender{$sender}"
    }
}
