<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
	<h2> ���� �α��� ó�� 2</h2>

<% 
	request.setCharacterEncoding("euc-kr");
	//������ �̿��Ͽ� �����͸� �ҷ���
	String id =(String) session.getAttribute("id");
	String pass = (String) session.getAttribute("pass");
%>
	<h2> ����� ���̵�� <%=id %>�̰� �н������ <%=pass %>�Դϴ�.</h2>

	</center>
</body>
</html>