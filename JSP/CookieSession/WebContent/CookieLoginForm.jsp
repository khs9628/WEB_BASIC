<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>

<%
	//����� ��ǻ���� ��Ű ����ҷκ��� ��Ű���� �о�帲 ����� �𸣱⿡ �迭�� �̿��Ͽ� ����
  	Cookie[] cookies = request.getCookies();
	String id ="";	

	//��Ű���� ���� �� �ֱ⿡
	if(cookies != null){
		for(int i = 0 ; i < cookies.length ; i++){
			if(cookies[i].getName().equals("id")){
				cookies[i].getName();
				break; //���ϴ� �����͸� ã�ұ� ������ �ݺ����� ���� ���´�.		
			}
		}
	}
 

%>



<!-- ��Ű�� Ŭ���̾�Ʈ ��ǻ�Ϳ� ����ȴ�. -->
	<center>
	<h2> ��Ű �α��� </h2>
	<form action="CookieLoginProc.jsp" method ="post">
	<table width="400" border="1">
	<tr height ="50">
	<td width ="150"> ���̵� </td>
	<td width ="250"><input type ="text" name="id" value ="<%=id %>"></td>
	</tr>
	<tr height ="50">
	<td width ="150"> �н����� </td>
	<td width ="250"><input type ="password" name="pass"></td>
	</tr>
	<tr height ="50">
	<td colspan="2" align ="center"> <input type ="checkbox" name="save">���̵����� </td>
	</tr>
	<tr height ="50">
	<td colspan="2" align ="center"> <input type ="submit" value="�α���"></td>
	</tr>
	
	</table>
	</form>
	
	</center>
</body>
</html>