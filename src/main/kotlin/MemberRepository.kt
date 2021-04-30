// MemberRepository 시작
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
// MemberRepository 끝