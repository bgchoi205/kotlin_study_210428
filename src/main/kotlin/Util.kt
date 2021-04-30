import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun readLineTrim() = readLine()!!.trim()

object Util{
    fun getDateNowStr() : String{
        var now = LocalDateTime.now()
        var getNowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초"))
        return getNowStr
    }
}