// BoardController 시작
class BoardController{
    fun add(){
        print("게시판 이름 : ")
        val name = readLineTrim()
        print("게시판 코드 : ")
        val code = readLineTrim()
        val id = boardRepository.addBoard(name, code)
        println("$id 번 게시판 생성완료")
    }
}
// BoardController 끝