<!-- getOneBoard()메소드를 통해 하나의 게시물을 가져와 답글쓰기 글수정하기 글삭제하기를 구현한다. -->

<%@page import="model.BoardBean"%>
<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
 <%
 	int num = Integer.parseInt(request.getParameter("num").trim()); //공백제거후 정수형으로 바뀜
 
 	//데이터 베이스 접근
 	BoardDAO bdao = new BoardDAO();
 	//Boardbean타입으로 하나의 게시글을 리턴
 	BoardBean bean = bdao.getOneBoard(num);
 %>
 
 <center>
 <h2>게시글 보기</h2>
 <table width="600" border="1" bgcolor="skyblue">
 <tr height ="40">
 	<td width ="120" align ="center">글번호</td>
 	<td align ="center" width="180"> <%=bean.getNum() %></td>
 	<td width ="120" align ="center">조회수</td>
 	<td align ="center" width="180"> <%=bean.getReadcount() %></td>		
 </tr>
 
  <tr height ="40">
 	<td width ="120" align ="center">작성자</td>
 	<td align ="center" width="180"> <%=bean.getWriter() %></td>
 	<td width ="120" align ="center">작성일</td>
 	<td align ="center" width="180"> <%=bean.getReg_date() %></td>		
 </tr>
 
 <tr height ="40">
 	<td align ="center" width ="120">이메일</td>
 	<td align ="center" colspan="3"><%=bean.getEmail() %></td>
 </tr>
 
 <tr height ="40">
 	<td align ="center" width ="120">제목</td>
 	<td align ="center" colspan="3"> <%=bean.getSubject() %></td>
 </tr>

 <tr height ="80">
 	<td align ="center" width ="120">글 내용</td>
 	<td align ="center" colspan="3"> <%=bean.getContent() %></td>
 </tr>
 
  <tr height ="40">
  	<td align ="center" colspan="4">
  	<input type ="button" value ="답글쓰기" onclick="location.href ='BoardReWriteForm.jsp?num=<%= bean.getNum() %>&ref=<%=bean.getRef()%>&re_step=<%=bean.getRe_step()%>&re_level=<%=bean.getRe_level()%>'">
  	<input type ="button" value ="수정하기" onclick="location.href='BoardUpdateForm.jsp?num=<%= bean.getNum() %>'">
  	<input type ="button" value ="삭제하기" onclick="location.href='BoardDeleteForm.jsp?num=<%= bean.getNum() %>'">
  	<input type ="button" value ="목록보기" onclick="location.href='BoardList.jsp'">
 </td>
 </tr>
 </table>
 </center>
</body>
</html>