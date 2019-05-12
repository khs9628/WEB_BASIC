package android;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Calendar_Board {
	   private static Calendar_Board calendar_board = new Calendar_Board();

	   public static Calendar_Board getCalendar_Board() {
	      return calendar_board;
	   }

	   private String returns;
	   private Connection con = null;
	   private PreparedStatement pstmt = null;
	   private ResultSet rs = null;
	   DataSource ds;
	  
	   public String select(String userId) {
	      try {
	        returns ="";
	        Context initctx = new InitialContext();

			//���� ������ ������ ��Ƴ��� ������ �̵�
			Context envctx = (Context) initctx.lookup("java:comp/env");

			//������ �ҽ� ��ü�� ����
			ds = (DataSource) envctx.lookup("jdbc/WorkManager");

			con = ds.getConnection();
			String query = "SELECT emp_no FROM employee where emp_id='"+userId+"'";
	        pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        
	       while(rs.next()) {
	        	 return rs.getString("emp_no");
	       }   
	     } catch (Exception e) {
	    	 System.out.println("error");
	        e.printStackTrace();
	     } // end try~catch
	      
	      finally {
	    	  System.out.println("final");
	          if (pstmt != null)
	        	  try {
						if(rs != null) rs.close();
						if(pstmt != null) pstmt.close();
						if(con != null) con.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
	      }
	      return returns;
	   }// end select()

	}