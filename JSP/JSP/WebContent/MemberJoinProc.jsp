<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
	<h2>ȸ�� ���� ����</h2>
	<%
	request.setCharacterEncoding("euc-kr");
	%>
	<jsp:useBean id="mbean" class ="bean.MemberBean">
	<jsp:setProperty name="mbean" property="*" /></jsp:useBean> 
	
	<h2> ����� ���̵�� <jsp:getProperty property="id" name="mbean"/></h2>
	<h2> ����� �н������ <jsp:getProperty property="password" name="mbean"/></h2>
	<h2> ����� �̸����� <jsp:getProperty property="email" name="mbean"/></h2>
	<h2> ����� �ּҴ� <jsp:getProperty property="address" name="mbean"/></h2>
	<h2> ����� ��ȭ��ȣ�� <jsp:getProperty property="tel" name="mbean"/></h2>
	</center>
</body>
</html>