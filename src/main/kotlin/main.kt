val articles = mutableListOf<Article>()
var lastId = 0

val members = mutableListOf<Member>()
var memberLastId = 0

var loginedMember : Member? = null


val articleRepository = ArticleRepository()
val memberRepository = MemberRepository()
val boardRepository = BoardRepository()

fun main(){


    val articleController = ArticleController()
    val memberController = MemberController()
    val boardController = BoardController()

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
            "/board/list" -> {
                boardRepository.boardList()
            }
        }
    }
    println("==simple ssg 끝==")
}
