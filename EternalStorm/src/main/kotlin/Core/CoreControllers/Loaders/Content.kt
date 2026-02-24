package la.vok.Core.CoreControllers.Loaders

class Content<T>(
    var contentLoader: ContentLoader<T>,
    var key: String = ""
) {
    val value: T get() {
        return contentLoader.getValue(key)
    }
}
