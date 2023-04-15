class Files {

    companion object {
        fun getResourceAsText(path: String): String? {
            return this::class.java.classLoader.getResource(path)?.readText()
        }
    }
}
