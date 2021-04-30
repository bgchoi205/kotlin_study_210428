// BoardRepository 시작
class BoardRepository{

    val boards = mutableListOf(
        Board(1, "공지사항", "notice"),
        Board(2, "자유", "free")
    )
    var boardId = 2

    fun addBoard(name : String, code : String) : Int{
        val id = ++boardId
        boards.add(Board(id, name, code))
        return id
    }

    fun boardList(){
        for(board in boards){
            println("게시판번호 : ${board.id} / 게시판이름 : ${board.name} / 게시판코드 : ${board.code}")
        }
    }

}