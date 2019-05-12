 package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class userDAO {
	 
	  DataSource ds;
	  
	  // ������ ���̽��� Ŀ�ؼ� Ǯ�� ����ϵ��� �����ϴ� �޼ҵ�
	  public userDAO() {
		  try {
			  Context initctx = new InitialContext();

			//���� ������ ������ ��Ƴ��� ������ �̵�
			Context envctx = (Context) initctx.lookup("java:comp/env");

			//������ �ҽ� ��ü�� ����
			ds = (DataSource) envctx.lookup("jdbc/WorkManager");


		} catch (Exception e) {
			e.printStackTrace();
		}
	
}
	  
	  public int login(String userID, String userPassword) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  
		  String SQL = "SELECT userPassword FROM MANAGER WHERE userID = ?";
		  
		  try {
			  conn = ds.getConnection();
			  pstmt = conn.prepareStatement(SQL);
			  pstmt.setString(1, userID);
			  rs= pstmt.executeQuery();
			  
			  if(rs.next()) {
				  if(rs.getString("userPassword").equals(userPassword)) {
					  return 1; //�α��� ����
				  }
				  	return 2; //��й�ȣ Ʋ��
			  }else {
						  return 0; //�ش� ����ڰ� �������� ����
					  }
			 
		  
		  } catch(Exception e) {
		  	e.printStackTrace();
		  }finally {
			  try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		  return -1; //������ ���̽� ����
	  }
	  
	  
	  public int registerCheck(String userID) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  
		  String SQL = "SELECT * FROM MANAGER WHERE userID = ?";
		  
		  try {
			  conn = ds.getConnection();
			  pstmt = conn.prepareStatement(SQL);
			  pstmt.setString(1, userID);
			  rs= pstmt.executeQuery();
			  
			  if(rs.next()|| userID.equals("")) {
				 return 0;//�̹��ִ� ���̵�
					  }
			  else {
				  return 1; //���԰����� ���̵�
			  }
		  
		  }catch(Exception e) {
		  	e.printStackTrace();
		  }finally {
			  try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		  return -1; //������ ���̽� ����
	  }
	  
	  
	  public int register(String userID, String userPassword,String userName,String userGender,String userEmail) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  
		  String SQL = "INSERT INTO MANAGER VALUES (?, ?, ?, ?, ?)" ;
		  
		  try {
			  conn = ds.getConnection();
			  pstmt = conn.prepareStatement(SQL);
			  pstmt.setString(1, userID);
			  pstmt.setString(2, userPassword);
			  pstmt.setString(3, userName);
			  pstmt.setString(4, userGender);
			  pstmt.setString(5, userEmail);
			  
			  return pstmt.executeUpdate();
			  
		}catch(Exception e) {
		  	e.printStackTrace();
		  }finally {
			  try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		  return -1; //������ ���̽� ����
	  }
	  
	  
	  //�Ѹ��� ����� ������ �������� �޼ҵ�
	  public userDTO getUser(String userID) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  ;
		  userDTO user = new userDTO();
		  String SQL ="SELECT * FROM MANAGER WHERE userID = ?";
		  try {
			  conn= ds.getConnection();
			  pstmt = conn.prepareStatement(SQL);
			  pstmt.setString(1, userID);
			  rs = pstmt.executeQuery();
			  if(rs.next()) {
				  user.setUserID(userID);
				  user.setUserPassword(rs.getString("userPassword"));
				  user.setUserName(rs.getString("userName"));
				  user.setUserGender(rs.getString("userGender"));
				  user.setUserEmail(rs.getString("userEmail"));
				   }
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			  try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		  return user; 
	  
	  
	  }
	  //������ �����ϴ� �޼ҵ�
	  public int update(String userID, String userPassword,String userName,String userGender,String userEmail) {
		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  ResultSet rs = null;
		  
		  String SQL = "UPDATE MANAGER SET userPassword = ? ,userName = ?, userGender = ?, userEmail = ? WHERE userID =?" ;
		  
		  try {
			  conn = ds.getConnection();
			  pstmt = conn.prepareStatement(SQL);
			  
			  pstmt.setString(1, userPassword);
			  pstmt.setString(2, userName);
			  pstmt.setString(3, userGender);
			  pstmt.setString(4, userEmail);
			  pstmt.setString(5, userID);
			  return pstmt.executeUpdate();
			  
		}catch(Exception e) {
		  	e.printStackTrace();
		  }finally {
			  try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		  return -1; //������ ���̽� ����
	  }
	  
}
