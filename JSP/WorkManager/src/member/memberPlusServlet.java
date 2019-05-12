package member;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/memberPlusServlet")
public class memberPlusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 doPost(request,response);
	
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String EMP_NO = request.getParameter("EMP_NO");
		if(EMP_NO == null || EMP_NO.equals("")) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "����� �����Ͻÿ�.");
			response.sendRedirect("memberList.jsp");
			return;
		}
	
		
		memberDAO memberDAO = new memberDAO();
		int result = memberDAO.memberN(EMP_NO);
		if(result == 1) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "���������� �߰��߽��ϴ� ��� �����͸� �Է����ּ���.");
			request.getSession().setAttribute("EMP_NO", EMP_NO);
			response.sendRedirect("memberHireInfoPlus.jsp");
			return;
		}else {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "������ ���̽� �����Դϴ�.");
			response.sendRedirect("memberPlus.jsp");
			return;
		}
		
	}
}

