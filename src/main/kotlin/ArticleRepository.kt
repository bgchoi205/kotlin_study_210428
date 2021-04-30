// ArticleRepository 시작
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
// ArticleRepository 끝
