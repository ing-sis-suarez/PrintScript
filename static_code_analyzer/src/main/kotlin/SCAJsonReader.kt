import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class SCAJsonReader(val file: File) {
    fun readJson(): Map<String, Boolean> {
        return if (!file.exists()) {
            mapOf()
        } else {
            val result = file.readText()
            val map = Gson().fromJson<Map<String, Boolean>>(result, object : TypeToken<Map<String, Boolean>>() {}.type)
            return map
        }
    }
}
