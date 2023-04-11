class Variables {
    fun variable1(): MutableMap<String, Pair<String, String?>> {
        val variables: MutableMap<String, Pair<String,String?>> = HashMap()
        variables["val1"] = Pair("Number", null)
        return variables
    }
    fun variable2(): MutableMap<String, Pair<String, String?>> {
        val variables: MutableMap<String, Pair<String,String?>> = HashMap()
        variables["val1"] = Pair("Number", "10")
        return variables
    }
    fun variable3(): MutableMap<String, Pair<String, String?>> {
        val variables: MutableMap<String, Pair<String,String?>> = HashMap()
        variables["val1"] = Pair("String", "1010")
        return variables
    }
    fun variable4(): MutableMap<String, Pair<String, String?>> {
        val variables: MutableMap<String, Pair<String,String?>> = HashMap()
        variables["val1"] = Pair("String", "101")
        return variables
    }
}