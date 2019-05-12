<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
		<table border ="1" width ="800">
		<tr height ="150">
		<td align ="center" colspan ="2">
		<jsp:include page="Top.jsp"/>
		</td>
		</tr>
		
		<tr height ="400">
		<td align ="center">
		<jsp:include page="left.jsp"/></td>
		
		<td align ="center" width ="600">
		<jsp:include page="center.jsp"/>
		</td>
		</tr>
		
		<tr height ="100">
		<td align ="center" colspan ="2">
		<jsp:include page="bottom.jsp"/></td>
		</tr>
		</table>
	</center>
</body>
</html>