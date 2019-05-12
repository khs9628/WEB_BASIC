<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<!-- 세션을 사용할 때?
	request객체  => 한페이지에서만 유효하다.
		 but session은 유효 하기 때문에 (브라우저가 종료되면 사라짐)
	 2. 주소창에 암호와 아이디가 넘어가지 않아서 보안성이 좋다.
	 -->
	
	<center>
	<h2> 세션 로그인 </h2>
	<form action="SessionLoginProc.jsp" method ="post">
	<table width="400" border="1">
	<tr height ="50">
	<td width ="150"> 아이디 </td>
	<td width ="250"><input type ="text" name="id"></td>
	</tr>
	<tr height ="50">
	<td width ="150"> 패스워드 </td>
	<td width ="250"><input type ="password" name="pass"></td>
	</tr>
	<tr height ="50">
	<td colspan="2" align ="center"> <input type ="submit" value="로그인"></td>
	</tr>
	</table>
	</form>
	</center>
</body>
</html>