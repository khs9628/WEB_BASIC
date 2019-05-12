package member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/memberHireInfoPlusServlet")
public class memberHireInfoPlusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String workPlace = request.getParameter("workPlace");
		String salType = request.getParameter("salType");
		String STD_salary = request.getParameter("STD_salary");
		String EMP_NO = request.getParameter("EMP_NO");
		String WORK_DAY = request.getParameter("WORK_DAY");
		String REST_DAY = request.getParameter("REST_DAY");
		String START_TIME = request.getParameter("START_TIME");
		String END_TIME = request.getParameter("END_TIME");
		String SALDATE = request.getParameter("SALDATE");
		//�Է��Ѱ��� ������ �˸�â�� ����ִ� �ڵ�
		if( workPlace == null || workPlace.equals("") ||
		   salType == null || salType.equals("") ||
		   STD_salary == null || STD_salary.equals("") ||
		   EMP_NO == null || EMP_NO.equals("")||
		   REST_DAY == null || REST_DAY.equals("")||
		   START_TIME == null || START_TIME.equals("")||
		   END_TIME == null || END_TIME.equals("")||
		   SALDATE == null || SALDATE.equals("")) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "��� ������ �Է��Ͻÿ�");
			response.sendRedirect("memberList.jsp");
			return;
		}
		
		memberDAO memberDAO = new memberDAO();
		int result = memberDAO.memberHirePlus(workPlace, salType, STD_salary, WORK_DAY, REST_DAY , START_TIME, END_TIME, EMP_NO ,SALDATE);
		if(result == 1) {
				request.getSession().setAttribute("messageType", "���� �޼���");
				request.getSession().setAttribute("messageContent", "�����Դϴ�.");
				response.sendRedirect("memberList.jsp");
				return;
			}else {
				request.getSession().setAttribute("messageType", "���� �޼���");
				request.getSession().setAttribute("messageContent", "������ ���̽� �����Դϴ�.");
				response.sendRedirect("index.jsp");
				return;
			}
		}
		}