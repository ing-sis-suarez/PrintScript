import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class SCAJsonReader(path: String){
    val file = if (path == ""){
        "null"
    }else{
        Files.getResourceAsText(path).toString()
    }
    fun readJson(): Map<String, Boolean>{
        return if (file == "null"){
            mapOf()
        }else{
            Gson().fromJson(file, object : TypeToken<Map<String, Boolean>>() {}.type)
        }
    }
}