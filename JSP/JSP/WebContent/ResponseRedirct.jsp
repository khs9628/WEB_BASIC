<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<h2>이 페이지는 ResponseRedirct.jsp 페이지 입니다.</h2>
	
	<%
	request.setCharacterEncoding("euc-kr");
	
	String id = request.getParameter("id");
	
	%>
	
	<h3>아이디 = <%=id %></h3>
</body>
</html>