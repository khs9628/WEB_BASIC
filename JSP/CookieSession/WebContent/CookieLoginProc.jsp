<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<% 
	request.setCharacterEncoding("euc-kr");
	
	/* üũ�� 1 �ƴϸ� null
	out.write("���̵� ���� =" + request.getParameter("save"));
	*/
	
	String save = request.getParameter("save");
	//���̵� �� ����
	String id =request.getParameter("id");
	
	if(save != null){
	//��Ű�� ����Ϸ���  - ��ŰŬ������ �������־�� ��
	Cookie cookie = new Cookie("id", id);//1��° =KEY ,2���� =VALUE
	cookie.setMaxAge(60*10); //10�а� ��ȣ
	
	//����ڿ��� ��Ű ���� �Ѱ���
	response.addCookie(cookie);
	out.write("��Ű ���� �Ϸ�");
	}
	%>
	
	
</body>
</html>