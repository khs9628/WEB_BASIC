<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<h2>�� �������� �α��� ������ �Ѿ���� ������ �Դϴ�.</h2>
	
	<%request.setCharacterEncoding("euc-kr");
	
	String id = request.getParameter("id");
	
	response.sendRedirect("ResponseRedirct.jsp");//�帧����
	
	%>

    
	<h3>���̵� = <%=id %></h3>
</body>
</html>