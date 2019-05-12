package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	  private Connection con;
	  private PreparedStatement pstmt;
	  private ResultSet rs;
	  
	  // ������ ���̽��� Ŀ�ؼ� Ǯ�� ����ϵ��� �����ϴ� �޼ҵ�
	  public void getCon() {
		  try {
			 //�ܺο��� �����͸� �о���� �ϱ⿡
			 Context initctx = new InitialContext();
			 
			 //���� ������ ������ ��Ƴ��� ������ �̵�
			 Context envctx = (Context) initctx.lookup("java:comp/env");
			 
			 //������ �ҽ� ��ü�� ����
			 DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
			
			 //������ �ҽ��� �������� Ŀ�ؼ��� ������ �ֽÿ�
			 con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  //�ϳ��� ���ο� �Խñ��� �Ѿ�ͼ� ����Ǵ� �޼ҵ�
	  public void insertBoard(BoardBean bean) {
		  //������ ���̽� ����
		  getCon();
		  //��Ŭ������ �Ѿ���� �ʾҴ� �����͵��� �ʱ�ȭ ���־�� �Ѵ�.
		  int ref = 0; //�۱׷��� �ǹ� = ������ ������Ѽ� ����ū ref���� �������� +1�� �����ָ� ��
		  int re_step = 1; //�����̱⿡ =�θ��
		  int re_level = 1;
		  
		  try {
			//���� ū ref���� �о���� ���� �غ�
			  String refsql = "select max(ref) from board";
			//���� ���� ��ü
			  pstmt = con.prepareStatement(refsql);
			//���������� ����� ����
			  rs = pstmt.executeQuery();
			if(rs.next()) {
				ref = rs.getInt(1)+1; //�ִ밪�� +1�� ���ؼ� �۱׷��� ����
			}
			//������ �Խñ� ��ü���� ���̺� ����
			String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = con.prepareStatement(sql);
			//?�� ���� ����
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
		
			//������ ����
			pstmt.executeUpdate();
			//�ڿ� �ݳ�
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
	  }
	  
	  
	  //��ü �Խù��� �����ϴ� �޼ҵ� �ۼ�
	  public Vector<BoardBean> getAllBoard() {
		  Vector<BoardBean> v = new Vector<>();
		  
		  getCon();
		  
		  try {
			String sql = "select * from board order by ref desc,re_step asc";
			pstmt=con.prepareStatement(sql);
			
			rs= pstmt.executeQuery();
			
			//������ ������ ����� �𸤿� �ݺ����� �̿��Ͽ� �����͸� ����
			while(rs.next()) {
				//�����͸� ��Ű¡
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				v.add(bean);
			}
			con.close();
			  
		} catch (Exception e) {
			// TODO: handle exception
		}
		  return v;
	  }
	  
	  //BoardInfo �ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	  
	  public BoardBean getOneBoard(int num) {
		  BoardBean bean = new BoardBean();
		  getCon();
		  
		  try {
			//��ȸ�� ��������
			  String readsql = "update board set readcount = readcount+1 where num =?";
			  pstmt = con.prepareStatement(readsql);
			  pstmt.setInt(1, num);
			  pstmt.executeUpdate();
			  
			//�����غ�
			  String sql="select * from board where num=?";
		   //�������ఴü
			  pstmt = con.prepareStatement(sql);
			  pstmt.setInt(1, num);
			  //���� ������ ����� ����
			  rs = pstmt.executeQuery();
			  if(rs.next()) {
				  bean.setNum(rs.getInt(1));
				  bean.setWriter(rs.getString(2));
				  bean.setEmail(rs.getString(3));
				  bean.setSubject(rs.getString(4));
				  bean.setPassword(rs.getString(5));
				  bean.setReg_date(rs.getDate(6).toString());
				  bean.setRef(rs.getInt(7));
				  bean.setRe_step(rs.getInt(8));
				  bean.setRe_level(rs.getInt(9));
				  bean.setReadcount(rs.getInt(10));
				  bean.setContent(rs.getString(11));
			  }
			  con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  return bean;
	  }
	  
	  //�亯���� ����Ǵ� �޼ҵ�
	  public void reWriteBoard(BoardBean bean) {
		 //�θ� �� �׷�� �۷��� �۽����� �о�帲
		  int ref = bean.getRef();
		  int re_step = bean.getRe_step();
		  int re_level = bean.getRe_level();
		  
		  getCon();
		  
		  try {
			// ---------- �ٽ��ڵ� ----------------  //
			//�θ� �ۺ��� ū re_level�� ���� ���� 1�� ����������
			 String levelsql = "update board set re_level=re_level+1 where ref=? and re_level > ?";
			 pstmt =con.prepareStatement(levelsql);
			 pstmt.setInt(1, ref);
			 pstmt.setInt(2, re_level);
			 //��������
			 pstmt.executeUpdate();
			 //�亯�� �����͸� ����
			 String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			 pstmt = con.prepareStatement(sql);
			 //?���� ����
			 	pstmt.setString(1, bean.getWriter());
				pstmt.setString(2, bean.getEmail());
				pstmt.setString(3, bean.getSubject());
				pstmt.setString(4, bean.getPassword());
				pstmt.setInt(5, ref); //�θ��� ref���� �־���
				pstmt.setInt(6, re_step+1); //����̱⿡ �θ�ۿ� +1 �� �������
				pstmt.setInt(7, re_level+1); //
				pstmt.setString(8, bean.getContent());
		        //���� ����
				pstmt.executeUpdate();
				//�ڿ��ݳ�
				con.close();
		  } catch (Exception e) {
		e.printStackTrace();
		}
	  }
	  
	  //boardUpadate�� Delete�� �ϳ��� �Խñ��� ����
	  public BoardBean getOneUpdateBoard(int num) {
		  BoardBean bean = new BoardBean();
		  getCon();
		  
		  try {
			//�����غ�
			  String sql="select * from board where num=?";
		   //�������ఴü
			  pstmt = con.prepareStatement(sql);
			  pstmt.setInt(1, num);
			  //���� ������ ����� ����
			  rs = pstmt.executeQuery();
			  if(rs.next()) {
				  bean.setNum(rs.getInt(1));
				  bean.setWriter(rs.getString(2));
				  bean.setEmail(rs.getString(3));
				  bean.setSubject(rs.getString(4));
				  bean.setPassword(rs.getString(5));
				  bean.setReg_date(rs.getDate(6).toString());
				  bean.setRef(rs.getInt(7));
				  bean.setRe_step(rs.getInt(8));
				  bean.setRe_level(rs.getInt(9));
				  bean.setReadcount(rs.getInt(10));
				  bean.setContent(rs.getString(11));
			  }
			  con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  return bean;
	  }
	  
	  //update �� delete�� �ʿ��� �н����带 �������ִ� �޼ҵ�
	  public String getPass(int num) {
		  String pass ="";
		
		  getCon();
		  try {
			String sql="select password from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			//�н����尪 ����
			if(rs.next()) {
				pass = rs.getString(1);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			}
		  return pass;
	  }  
	  
	  //�ϳ��� �Խñ��� �����ϴ� �޼ҵ�
	  public void updateBoard(BoardBean bean) {
		  getCon();
		  
		  try {
			  String sql = "update board set subject=? , content = ? where num=?";
			  pstmt = con.prepareStatement(sql);
			  
			  pstmt.setString(1, bean.getSubject());
			  pstmt.setString(2, bean.getContent());
			  pstmt.setInt(3, bean.getNum());
			  
			  pstmt.executeUpdate();
			  
			  con.close();
		} catch (Exception e) {
			e.printStackTrace();
			}
	  }
	  
	  //�ϳ��� �Խù��� �����ϴ� �޼ҵ�
	  public void deleteBoard(int num) {
		  getCon();
		  try {
			  String sql = "delete from board where num=?";
			  pstmt = con.prepareStatement(sql);
			  //���� ����
			  pstmt.setInt(1, num);
			  //��������
			  pstmt.executeUpdate();
			  //�ڿ��ݳ�
			  con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  }
	  
