package member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/workUpdateServlet")
public class workUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String WORKTIME_ID = request.getParameter("WORKTIME_ID");
		String A_TIME = request.getParameter("A_TIME");
		String E_TIME = request.getParameter("E_TIME");
		if(A_TIME == null || A_TIME.equals("") ||
				E_TIME == null || E_TIME.equals("") ||
				WORKTIME_ID == null || WORKTIME_ID.equals("")) 
	     	{  
			 request.getSession().setAttribute("messageType", "���� �޼���");
			 request.getSession().setAttribute("messageContent", "��� ������ �Է��Ͻÿ�");
			 response.sendRedirect("index.jsp");
			 return;
				}
		String A_TIME1 = A_TIME.substring(0, 10) + A_TIME.substring(11);
		String E_TIME1 = E_TIME.substring(0, 10) + E_TIME.substring(11);
		System.out.println(A_TIME1 +"  " + E_TIME1 + WORKTIME_ID);
		
	    memberDAO memberDAO = new memberDAO();
	    int result = memberDAO.memberWorkUpdate(A_TIME1, E_TIME1, WORKTIME_ID);		
		if(result==1) {
		request.getSession().setAttribute("messageType", "���� �޽���");
		request.getSession().setAttribute("messageContent", "���������� �ٹ��� ���� �Ǿ����ϴ�.");
		response.sendRedirect("workList.jsp");
		return;
		}else {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "������ ���̽� �����Դϴ�.");
			response.sendRedirect("index.jsp");
			return;
		}
	}

}

