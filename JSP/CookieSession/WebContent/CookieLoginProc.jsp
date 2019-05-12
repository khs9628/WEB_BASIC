<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<% 
	request.setCharacterEncoding("euc-kr");
	
	/* 체크면 1 아니면 null
	out.write("아이디 저장 =" + request.getParameter("save"));
	*/
	
	String save = request.getParameter("save");
	//아이디 값 저장
	String id =request.getParameter("id");
	
	if(save != null){
	//쿠키를 사용하려면  - 쿠키클래스를 생성해주어야 함
	Cookie cookie = new Cookie("id", id);//1번째 =KEY ,2번쩨 =VALUE
	cookie.setMaxAge(60*10); //10분간 유호
	
	//사용자에게 쿠키 값을 넘겨줌
	response.addCookie(cookie);
	out.write("쿠키 생성 완료");
	}
	%>
	
	
</body>
</html>