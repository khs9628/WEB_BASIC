<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
	<h2> 세션 로그인 처리 1</h2>
	
<% 
	request.setCharacterEncoding("euc-kr");
	
	//사용자로부터 데이터를 읽어드림
	String id = request.getParameter("id");
	String pass = request.getParameter("pass");
	
	//아이디와 패스워드를 저장
	session.setAttribute("id", id);
	session.setAttribute("pass", pass);
	
	//세션의 유지시간 설정
	session.setMaxInactiveInterval(60);
%>
	<h2> 당신의 아이디는 <%=id %>이고 패스워드는 <%=pass %>입니다.</h2>
	<a href="SessionLoginProc2.jsp">다음페이지로 이동</a>
	<%-- 세션을 사용하지 않고 url로 넘기는 방법. 
	<a href="SessionLoginProc2.jsp?id=<%=id %>&pass=<%=pass%>">다음페이지로 이동</a>
	 --%>
	</center>	
</body>
</html>