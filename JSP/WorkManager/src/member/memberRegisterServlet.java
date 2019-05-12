package member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class memberRegisterServlet
 */
@WebServlet("/memberRegisterServlet")
public class memberRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	String EMP_ID = request.getParameter("EMP_ID");
	String EMP_PW = request.getParameter("EMP_PW");
	String EMP_NAME = request.getParameter("EMP_NAME");
	String EMP_EMAIL = request.getParameter("EMP_EMAIL");
	
	//�Է��Ѱ��� ������ �˸�â�� ����ִ� �ڵ�
	if(EMP_ID == null || EMP_ID.equals("") ||
	   EMP_EMAIL == null || EMP_EMAIL.equals("") ||
	   EMP_PW == null || EMP_PW.equals("") ||
	   EMP_NAME == null || EMP_NAME.equals("")) {
	   request.getSession().setAttribute("messageType", "���� �޼���");
		request.getSession().setAttribute("messageContent", "��� ������ �Է��Ͻÿ�");
		response.sendRedirect("index.jsp");
		return;
	}
	
	System.out.println(EMP_ID+EMP_EMAIL+EMP_PW+EMP_NAME);
	
	memberDAO memberDAO = new memberDAO();
	int result = memberDAO.memberRegister(EMP_ID, EMP_PW, EMP_NAME, EMP_EMAIL);	
	if(result==1) {
	request.getSession().setAttribute("messageType", "���� �޽���");
	request.getSession().setAttribute("messageContent", "��� ���Խ�û�� �����߽��ϴ�.");
	response.sendRedirect("index.jsp");
	return;
	}else {
		request.getSession().setAttribute("messageType", "���� �޼���");
		request.getSession().setAttribute("messageContent", "������ ���̽� �����Դϴ�.");
		response.sendRedirect("index.jsp");
		return;
	}
}
}
