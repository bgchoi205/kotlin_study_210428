import java.lang.NumberFormatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(){
    val articleController = ArticleController()

    println("==simple ssg 시작==")

    articleRepository.makeTestArticles()

    while(true){
        print("명령어 : ")
        val cmd = readLineTrim()
        val rq = Rq(cmd)

        when(rq.actionPath){
            "/exit" -> {
                println("프로그램 종료")
                break
            }
            "/article/write" -> {
                articleController.add()
            }
            "/article/list" -> {
                articleController.list(rq)
            }
            "/article/detail" -> {
                articleController.detail(rq)
            }
            "/article/delete" -> {
                articleController.delete(rq)
            }
            "/article/modify" -> {
                articleController.modify(rq)
            }
            "/member/join" -> {

            }

        }

    }


    println("==simple ssg 끝==")

}



data class Article(
    val id : Int,
    var title : String,
    var body : String,
    val regDate : String,
    var updateDate : String
)

object articleRepository{

    val articles = mutableListOf<Article>()
    var lastId = 0

    fun addArticle(title:String, body:String) : Int{
        val id = ++lastId
        val regDate = Util.getDateNowStr()
        val updateDate = Util.getDateNowStr()
        articles.add(Article(id, title, body, regDate, updateDate))

        return id
    }

    fun makeTestArticles(){
        for(i in 1..50){
            val title = "제목$i"
            val body = "내용$i"
            addArticle(title,body)
        }
    }

    fun getArticleById(id: Int): Article? {
        for(article in articles){
            if(article.id == id){
                return article
            }
        }
        return null
    }

    fun deleteArticle(article: Article) {
        articles.remove(article)
        println("${article.id} 번 게시물이 삭제되었습니다.")
    }

    fun modifyArticle(article : Article, title: String, body: String) {
        article.title = title
        article.body = body
        article.updateDate = Util.getDateNowStr()

    }

    fun getFilteredArticles(keyword : String, page : Int, pageCount : Int): List<Article> {
        val filteredArticles1 = getKeywordArticles(keyword)
        val filteredArticles2 = getPageArticles(filteredArticles1, page, pageCount)
        return filteredArticles2
    }

    private fun getKeywordArticles(keyword : String) : List<Article>{
        val filteredArticles = mutableListOf<Article>()

        for(article in articles){
            if(article.title.contains(keyword)){
                filteredArticles.add(article)
            }
        }
        return filteredArticles
    }

    private fun getPageArticles(filteredArticles1: List<Article>, page: Int, pageCount: Int): List<Article> {
        val filteredArticles2 = mutableListOf<Article>()

        val startIndex = filteredArticles1.lastIndex - ((page - 1) * pageCount)
        var endIndex = startIndex - pageCount + 1
        if(endIndex < 0){
            endIndex = 0
        }
        for(i in startIndex downTo endIndex){
            filteredArticles2.add(filteredArticles1[i])
        }
        return filteredArticles2
    }

}

class ArticleController{
    fun add(){
        print("제목 : ")
        val title = readLineTrim()
        print("내용 : ")
        val body = readLineTrim()
        val id = articleRepository.addArticle(title,body)

        println("$id 번 게시물이 등록되었습니다.")
    }

    fun list(rq : Rq){
        val page = rq.getIntParam("page", 1)
        val keyword = rq.getStringParam("keyword", "")

        val filteredArticles = articleRepository.getFilteredArticles(keyword, page, 10)
        for(article in filteredArticles){
            println("번호 : ${article.id} / 제목 : ${article.title} / 등록날짜 : ${article.regDate}")
        }
    }

    fun detail(rq : Rq){
        val id = rq.getIntParam("id",0)

        if(id == 0){
            println("id를 입력해주세요")
            return
        }

        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("존재하지 않는 게시물입니다.")
            return
        }
        println("번호 : ${article.id}")
        println("제목 : ${article.title}")
        println("내용 : ${article.body}")
        println("등록날짜 : ${article.regDate}")
        println("수정날짜 : ${article.updateDate}")
    }

    fun delete(rq : Rq){
        val id = rq.getIntParam("id",0)

        if(id == 0){
            println("id를 입력해주세요")
            return
        }

        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("존재하지 않는 게시물입니다.")
            return
        }
        articleRepository.deleteArticle(article)
    }

    fun modify(rq : Rq){
        val id = rq.getIntParam("id",0)

        if(id == 0){
            println("id를 입력해주세요")
            return
        }
        val article = articleRepository.getArticleById(id)
        if(article == null){
            println("존재하지 않는 게시물입니다.")
            return
        }
        print("새 제목 : ")
        val title = readLineTrim()
        print("새 내용 : ")
        val body = readLineTrim()
        articleRepository.modifyArticle(article, title, body)
        println("$id 번 게시물이 수정되었습니다.")
    }

}

class Rq(cmd : String){

    val actionPath : String
    private val paramMap : Map<String, String>

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

// 유틸 관련
fun readLineTrim() = readLine()!!.trim()

object Util{
    fun getDateNowStr() : String{
        var now = LocalDateTime.now()
        var getNowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초"))
        return getNowStr
    }
}
// 유틸 끝