import java.io.File

class Files {

    companion object {
        fun getResourceAsText(path: String): String? {
            return this::class.java.classLoader.getResource(path)?.readText()
        }

        fun getResouceAsFile(path: String): File?{
            return this::class.java.classLoader.getResource(path)?.file?.let { File(it) }
        }
    }
}
