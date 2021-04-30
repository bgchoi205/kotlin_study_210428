// MemberController 시작
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
// MemberController 끝