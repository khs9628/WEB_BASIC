package android;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ReviseInfo_Board{
   private static ReviseInfo_Board reviseinfo_board = new ReviseInfo_Board();

   public static ReviseInfo_Board getReviseInfo_Board() {
      return reviseinfo_board;
   }

   private String returns;
   private Connection con = null;
   private PreparedStatement pstmt = null;
   private ResultSet rs = null;
   DataSource ds;
  
   public String select(String id) {
      try {
        returns ="";
        Context initctx = new InitialContext();

		//���� ������ ������ ��Ƴ��� ������ �̵�
		Context envctx = (Context) initctx.lookup("java:comp/env");

		//������ �ҽ� ��ü�� ����
		ds = (DataSource) envctx.lookup("jdbc/WorkManager");

		con = ds.getConnection();
         String query = "SELECT emp_no FROM EMPLOYEE where emp_id='"+id+"'";
         pstmt = con.prepareStatement(query);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            return rs.getString("emp_no");
            } // end while
      } catch (Exception e) {
         e.printStackTrace();
      } // end try~catch

      finally {
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