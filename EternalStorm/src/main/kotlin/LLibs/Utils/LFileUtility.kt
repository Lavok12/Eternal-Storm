    package la.vok.LavokLibrary.Utils

import java.io.File

class LFileUtility(private val rootPath: String, private val useAbsolutePath: Boolean = false) {

    private val rootDir = File(rootPath)

    init {
        if (!rootDir.exists() || !rootDir.isDirectory) {
            println(rootDir)
            throw IllegalArgumentException("Указанный путь не существует или не является папкой: $rootPath")
        }
    }

    private fun formatPath(file: File): String {
        return if (useAbsolutePath) file.absolutePath else file.name
    }

    fun getAllFolders(): List<String> {
        return rootDir.listFiles { _, name -> File(rootDir, name).isDirectory }
            ?.map { formatPath(it) }
            ?: emptyList()
    }

    fun getAllFiles(): List<String> {
        return rootDir.listFiles { _, name -> File(rootDir, name).isFile }
            ?.map { formatPath(it) }
            ?: emptyList()
    }

    fun getAllFilesAndFolders(): List<Pair<String, Boolean>> {
        return rootDir.listFiles()
            ?.map { Pair(formatPath(it), it.isDirectory) }
            ?: emptyList()
    }

    fun getAllFilesRecursive(): List<String> {
        return rootDir.walk()
            .filter { it.isFile }
            .map { formatPath(it) }
            .toList()
    }

    fun getAllFoldersRecursive(): List<String> {
        return rootDir.walk()
            .filter { it.isDirectory && it != rootDir }
            .map { formatPath(it) }
            .toList()
    }

    fun getAllFilesAndFoldersRecursive(): List<Pair<String, Boolean>> {
        return rootDir.walk()
            .filter { it != rootDir }
            .map { Pair(formatPath(it), it.isDirectory) }
            .toList()
    }
}