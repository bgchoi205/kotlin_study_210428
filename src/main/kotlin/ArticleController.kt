// ArticleController 시작
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
// ArticleController 끝