import java.lang.NumberFormatException

fun main(){

    println("==simple ssg 시작==")

    while(true){
        print("명령어 : ")
        val cmd = readLineTrim()
        val rq = Rq(cmd)

        when(rq.actionPath){
            "/exit" -> {
                println("프로그램 종료")
                break
            }
            "/article/detail" -> {
                println(rq.getStringParam("title", "1") == "1")
                println(rq.getIntParam("id", -1) == -1)
            }

        }

    }


    println("==simple ssg 끝==")

}

fun readLineTrim() = readLine()!!.trim()

class Rq(cmd : String){

    val actionPath : String
    val paramMap : Map<String, String>

    init{
        val cmdBits = cmd.split("?", limit = 2)
        actionPath = cmdBits[0].trim()
        val queryStr = if(cmdBits.lastIndex == 1 && cmdBits[1].isNotEmpty()){
            cmdBits[1].trim()
        }else{
            ""
        }

        paramMap = if(queryStr.isEmpty()){
            mapOf()
        }else{
            val queryStrBits = queryStr.split("&")
            val paramMapTemp = mutableMapOf<String, String>()

            for(queryStrBit in queryStrBits){
                val queryStrBitBits = queryStrBit.split("=", limit = 2)
                val paramName = queryStrBitBits[0]
                val paramValue = if(queryStrBitBits.lastIndex == 1 && queryStrBitBits[1].isNotEmpty()){
                    queryStrBitBits[1].trim()
                }else{
                    ""
                }
                if(paramValue.isNotEmpty()){
                    paramMapTemp[paramName] = paramValue
                }
            }

            paramMapTemp.toMap()
        }


    }

    fun getStringParam(name: String, default : String): String {

        return if(paramMap[name] == null){
            default
        }else{
            paramMap[name]!!
        }

//        return paramMap[name] ?: default // 엘비스 연산자

//        return try{
//            paramMap[name]!!
//        }catch(e : NullPointerException){
//            default
//        }

    }

    fun getIntParam(name: String, default : Int): Int {
        return if(paramMap[name] == null){
            default
        }else{
            try{
                paramMap[name]!!.toInt()
            }catch(e : NumberFormatException){
                default
            }
        }
    }


}
