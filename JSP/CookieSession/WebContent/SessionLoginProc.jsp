<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
	<h2> ���� �α��� ó�� 1</h2>
	
<% 
	request.setCharacterEncoding("euc-kr");
	
	//����ڷκ��� �����͸� �о�帲
	String id = request.getParameter("id");
	String pass = request.getParameter("pass");
	
	//���̵�� �н����带 ����
	session.setAttribute("id", id);
	session.setAttribute("pass", pass);
	
	//������ �����ð� ����
	session.setMaxInactiveInterval(60);
%>
	<h2> ����� ���̵�� <%=id %>�̰� �н������ <%=pass %>�Դϴ�.</h2>
	<a href="SessionLoginProc2.jsp">������������ �̵�</a>
	<%-- ������ ������� �ʰ� url�� �ѱ�� ���. 
	<a href="SessionLoginProc2.jsp?id=<%=id %>&pass=<%=pass%>">������������ �̵�</a>
	 --%>
	</center>	
</body>
</html>