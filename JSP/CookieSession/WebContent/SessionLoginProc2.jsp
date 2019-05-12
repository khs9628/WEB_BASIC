<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
	<h2> 세션 로그인 처리 2</h2>

<% 
	request.setCharacterEncoding("euc-kr");
	//세션을 이용하여 데이터를 불러옴
	String id =(String) session.getAttribute("id");
	String pass = (String) session.getAttribute("pass");
%>
	<h2> 당신의 아이디는 <%=id %>이고 패스워드는 <%=pass %>입니다.</h2>

	</center>
</body>
</html>