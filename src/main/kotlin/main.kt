import java.lang.NumberFormatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val articles = mutableListOf<Article>()
var lastId = 0

val members = mutableListOf<Member>()
var memberLastId = 0

var loginedMember : Member? = null


val articleRepository = ArticleRepository()
val memberRepository = MemberRepository()

fun main(){


    val articleController = ArticleController()
    val memberController = MemberController()

    println("==simple ssg 시작==")

    articleRepository.makeTestArticles()
    memberRepository.makeTestMembers()

    while(true){
        val prompt = if(loginedMember == null){
            "명령어 : "
        }else{
            "${loginedMember!!.nickName})"
        }
        print(prompt)
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
                memberController.join()
            }
            "/member/login"-> {
                memberController.login()
            }
            "/member/logout" -> {
                memberController.logout()
            }



        }

    }


    println("==simple ssg 끝==")

}

data class Member(
    val id : Int,
    val loginId : String,
    val loginPw : String,
    val name : String,
    val nickName : String,
    val regDate : String
)

class MemberRepository{
    fun addMember(loginId: String, loginPw: String, name: String, nickName: String) : Int{
        val id = ++memberLastId
        val regDate = Util.getDateNowStr()
        members.add(Member(id, loginId, loginPw, name, nickName, regDate))
        return id
    }

    fun getMemberByLoginId(loginId : String) : Member?{
        for(member in members){
            if(member.loginId == loginId){
                return member
            }
        }
        return null
    }

    fun isJoinableloginId(loginId: String) : Boolean{
        val member = getMemberByLoginId(loginId)

        return member == null
    }

    fun makeTestMembers(){
        for(i in 1..10){
            addMember("user$i", "user$i", "홍길동$i", "사용자$i")
        }
    }

    fun getMemberById(id : Int): Member? {
        for(member in members){
            if(member.id == id){
                return member
            }
        }
        return null
    }
}

class MemberController{
    fun join(){
        print("사용할 아이디 : ")
        val loginId = readLineTrim()
        val isJoinable = memberRepository.isJoinableloginId(loginId)
        if(isJoinable == false){
            println("사용중인 아이디입니다.")
            return
        }
        print("사용할 비밀번호 입력 : ")
        val loginPw = readLineTrim()
        print("이름 입력 : ")
        val name = readLineTrim()
        print("사용할 별명 입력 :")
        val nickName = readLineTrim()
        val id = memberRepository.addMember(loginId, loginPw, name, nickName)
        println("$id 번 회원으로 가입 완료")
    }

    fun login() {
        print("아이디 입력 : ")
        val loginId = readLineTrim()
        val member = memberRepository.getMemberByLoginId(loginId)
        if(member == null){
            println("없는 아이디 입니다.")
            return
        }
        print("비밀번호 입력 : ")
        val loginPw = readLineTrim()
        if(member.loginPw != loginPw){
            println("비밀번호가 틀립니다.")
            return
        }
        println("${member.nickName} 님 환영합니다.")
        loginedMember = member
    }

    fun logout() {
        loginedMember = null
        println("로그아웃")
    }
}



data class Article(
    val id : Int,
    var title : String,
    var body : String,
    val memberId : Int,
    val regDate : String,
    var updateDate : String
)

class ArticleRepository{


    fun addArticle(title:String, body:String, memberId : Int) : Int{
        val id = ++lastId
        val regDate = Util.getDateNowStr()
        val updateDate = Util.getDateNowStr()
        articles.add(Article(id, title, body, memberId, regDate, updateDate))

        return id
    }

    fun makeTestArticles(){
        for(i in 1..50){
            addArticle("제목$i","내용$i", i % 9 + 1)
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
        val member = loginedMember
        if(member == null){
            println("로그인 후 이용해주세요")
            return
        }
        print("제목 : ")
        val title = readLineTrim()
        print("내용 : ")
        val body = readLineTrim()
        val memberId = member.id
        val id = articleRepository.addArticle(title,body, memberId)

        println("$id 번 게시물이 등록되었습니다.")
    }

    fun list(rq : Rq){
        if(loginedMember == null){
            println("로그인 후 이용해주세요")
            return
        }
        val page = rq.getIntParam("page", 1)
        val keyword = rq.getStringParam("keyword", "")

        val filteredArticles = articleRepository.getFilteredArticles(keyword, page, 10)

        for(article in filteredArticles){
            val member = memberRepository.getMemberById(article.memberId)!!
            val nickName = member.nickName
            println("번호 : ${article.id} / 제목 : ${article.title} / 작성자 : ${nickName} / 등록날짜 : ${article.regDate}")
        }
    }

    fun detail(rq : Rq){
        if(loginedMember == null){
            println("로그인 후 이용해주세요")
            return
        }
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
        if(loginedMember == null){
            println("로그인 후 이용해주세요")
            return
        }
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
        if(loginedMember!!.id != article.memberId){
            println("권한이 없습니다.")
            return
        }
        articleRepository.deleteArticle(article)
    }

    fun modify(rq : Rq){
        if(loginedMember == null){
            println("로그인 후 이용해주세요")
            return
        }
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
        if(loginedMember!!.id != article.memberId){
            println("권한이 없습니다.")
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