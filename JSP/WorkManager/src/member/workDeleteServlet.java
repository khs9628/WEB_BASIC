package member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class workDeleteServlet
 */
@WebServlet("/workDeleteServlet")
public class workDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String WORKTIME_ID = request.getParameter("WORKTIME_ID");
		
		if(WORKTIME_ID == null || WORKTIME_ID.equals(""))
	     	{  
			 request.getSession().setAttribute("messageType", "���� �޼���");
			 request.getSession().setAttribute("messageContent", "��� ������ �Է��Ͻÿ�");
			 response.sendRedirect("index.jsp");
			 return;
				}
		
	    memberDAO memberDAO = new memberDAO();
	    int result = memberDAO.deleteWork(WORKTIME_ID);		
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