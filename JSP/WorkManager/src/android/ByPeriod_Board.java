package android;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ByPeriod_Board {
	   private static ByPeriod_Board byperiod_board = new ByPeriod_Board();

	   public static ByPeriod_Board getByPeriod_Board() {
	      return byperiod_board;
	   }

	   private String returns;
	   private Connection con = null;
	   private PreparedStatement pstmt = null;
	   private ResultSet rs = null;
	   DataSource ds;
	   public String select(String id, String startDate, String endDate) {
		   String sEmpId="", sResult="", sSelectDay="", sRestTime="", kindDay="", sWorkDay="",sJoinDate="", everyDayList="" ,sAbsent="", sLate="", sEarly="", sTodayATime="", sTodayETime="";
		   int emp_id=0,selectDay=0, daysCount=0, totalWorkDay=0;;
		   double restTime;
	      try {
	        returns ="";
	        Context initctx = new InitialContext();

			//���� ������ ������ ��Ƴ��� ������ �̵�
			Context envctx = (Context) initctx.lookup("java:comp/env");

			//������ �ҽ� ��ü�� ����
			ds = (DataSource) envctx.lookup("jdbc/WorkManager");

			con = ds.getConnection();
			/**************�Ű����� id(�޴�����ȣ)�� �̿��ؼ� EMP_NO ���*******************/
			 String query = "SELECT EMP_NO FROM EMPLOYEE WHERE EMP_ID='"+id+"'";
			 pstmt = con.prepareStatement(query);
		     rs = pstmt.executeQuery();
		     while(rs.next()) {
		     sEmpId = rs.getString("EMP_NO");
		     }
		    emp_id = Integer.parseInt(sEmpId);
	        query = "select COUNT(WORKTIME_ID)SELECT_DAY FROM WORKTIME WHERE EMP_NO='" +emp_id+ "' AND TO_CHAR(A_TIME, 'YYYY/MM/DD')>='"+startDate+"' AND TO_CHAR(A_TIME, 'YYYY/MM/DD')<='"+endDate+"' and E_time IS NOT NULL order by worktime_id";
			pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	sSelectDay = rs.getString("SELECT_DAY");
	        }
	        selectDay = Integer.parseInt(sSelectDay);
	        sResult += (selectDay+"-");
	        query = "SELECT workplace, saltype, rest_time, work_day, to_char(joindate,'YYYYMMDD')joindate from hireInfo where EMP_NO='"+emp_id+"'";
			pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	           sResult += rs.getString("workplace");
	           sResult += "-";
	           sResult += rs.getString("saltype");
	           sResult += "-";
	           sRestTime = rs.getString("rest_time");
	           sWorkDay = rs.getString("work_day");
	           sJoinDate = rs.getString("joindate");
	         } 
	        
	        restTime = Double.parseDouble(sRestTime);
	        
	        String[] aTimeArray = new String [selectDay]; //��� �ð� �迭
	        String[] eTimeArray = new String [selectDay]; //��� �ð� �迭
	        String[] plusETimeArray = new String [selectDay]; //��� �ð� + 2400 �迭
	        double[] TimeArray = new double [selectDay];  //�ٹ� �ð�(��� - ���)�迭
	        double[] aDateTime = new double [selectDay];  //��� �ð� �迭�� double��
	        double[] eDateTime = new double [selectDay];  //��� �ð� �迭�� double��
	        Date[] aDate = new Date [selectDay];			 //��� �ð� �迭�� Date�� 
	        Date[] eDate = new Date [selectDay];  		 //��� �ð� �迭�� Date��
	        int i, j;									 //�ݺ��� ����� ���� ���� i,j
	        
	        i=0;
	        query = "select TO_CHAR(A_TIME,'YYYYMMDDHH24mi')A_TIME, TO_CHAR(E_TIME, 'YYYYMMDDHH24mi')E_TIME, KIND_DAY FROM WORKTIME WHERE EMP_NO='" +emp_id+ "' AND TO_CHAR(A_TIME, 'YYYY/MM/DD')>='"+startDate+"' AND TO_CHAR(A_TIME, 'YYYY/MM/DD')<='"+endDate+"' and E_time IS NOT NULL order by worktime_id";
			pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	aTimeArray[i]=rs.getString("A_TIME");
	        	eTimeArray[i++]=rs.getString("E_TIME");
	        	kindDay +=rs.getString("KIND_DAY");
	        }
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHmm");
	        double Time;
	        long longETime;
	        for(i=0;i<selectDay;i++) {
	     	   
	 	       aDate[i] = (Date) dateFormat.parse(aTimeArray[i]);
	 	       aDateTime[i] = aDate[i].getTime();
	 	       
	 	      
	 	       eDate[i] = (Date) dateFormat.parse(eTimeArray[i]);
	 	       eDateTime[i] = eDate[i].getTime();
	 	       
	 	       Time = eDateTime[i]-aDateTime[i];
	 	      
	 	       if(Time<0) {													//Time���� ������ ��츦 ���� ����(�߰� �ٹ� ���� ��, �ڼ��� ����)
	 	    	   longETime = Long.parseLong(eTimeArray[i]);
	 	    	   plusETimeArray[i] = Long.toString(longETime+2400);
	 	    	   eDate[i] = (Date) dateFormat.parse(plusETimeArray[i]);
	 		       eDateTime[i] = eDate[i].getTime();
	 		       Time = eDateTime[i]-aDateTime[i];
	 	       }
	 	       TimeArray[i] = (Time/60000/60)-(restTime/60.0);   //��ȸ�Ⱓ�� �ް� �ð��� ������ ���� �ٹ� �ð� ���� �迭
	 	       if(TimeArray[i]<0) {
	 	    	   TimeArray[i] += (restTime/60.0);
	 	       }
	 	   }
	        
	        double totalTime=0.0;
	        for(i=0;i<selectDay;i++) {
	        	totalTime+=TimeArray[i];
	        }
	       
	        String[] sWorkDayToken=new String[sWorkDay.length()];
	        int [] workDayToken = new int[sWorkDay.length()];
	        int []restDay = {1,2,3,4,5,6,7};
	        int restDayCount = 0;
	        
	        for(i=0;i<sWorkDay.length();i++){	//���� �ٹ����� �ϳ��� ������, '��', 'ȭ', '��', '��', '��'
	        	sWorkDayToken[i]=sWorkDay.substring(i, i+1);
	        	workDayToken[i] = dayList(sWorkDayToken[i]); //'2', '3', '4', '5', '6'
	        }
	        
	        for(i=0;i<7;i++) {
	        	for(j=0;j<sWorkDay.length();j++) {
	        		if(restDay[i]==workDayToken[j]) {
	        			restDay[i]=0;
	        		}
	        	}
	        }
	        int[] kindDayList=new int[selectDay];
	        for(i=0;i<selectDay;i++) {
	        	kindDayList[i] = dayList(kindDay.substring(i,i+1));
	        }
	        
	        for(i=0;i<selectDay;i++) {
	        	for(j=0;j<7;j++) {
	        		if(kindDayList[i]==restDay[j])restDayCount++;
	        	}
	        }
	        String nowTime = getCurrentTime("YYYYMMdd");
	        Calendar aCal = getCalendar(startDate.replace("/", ""));
	        Calendar eCal = getCalendar(endDate.replace("/", ""));
	        
	        if(Integer.parseInt(startDate.replace("/",""))<Integer.parseInt(sJoinDate))
	        	aCal = getCalendar(sJoinDate);
	        
	        if(Integer.parseInt(endDate.replace("/",""))>Integer.parseInt(nowTime))
	        	eCal = getCalendar(nowTime);
	       
	        eCal.add(Calendar.DATE, 1);
	        
	        while(aCal.compareTo(eCal)!=0) {
	        	everyDayList+=Integer.toString(aCal.get(Calendar.DAY_OF_WEEK));
	        	aCal.add(Calendar.DATE,1);
	        	daysCount++;
	        }
	        query = "select to_char(a_time,'YYYYMMDDHH24mi')a_time ,to_char(e_time,'YYYYMMDDHH24mi')e_time from worktime where to_char(a_time,'YYYYMMDD')='"+nowTime+"' and emp_no='"+emp_id+"'";
			pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	sTodayATime+=rs.getString("a_time");
	        	sTodayETime+=rs.getString("e_time");
	        }
	        if((sTodayATime.length()>0)&&(sTodayETime.equals("null")))daysCount--; 	//������ ��� �ð��� ��ϵǾ����� ���� ���(���� ��� ���� ���� ���) ��ȸ �Ⱓ���� ���� ��¥ ����
	        for(i=0;i<daysCount;i++) {
	        	for(j=0;j<sWorkDay.length();j++) {
	        		if(Integer.parseInt(everyDayList.substring(i, i+1))==workDayToken[j])totalWorkDay++;
	        	}
	        }
	
	        sAbsent = Integer.toString((totalWorkDay+restDayCount)-selectDay);
	        System.out.println("total: "+totalWorkDay+"\trestDay: "+restDayCount+"\tselectDay: "+selectDay);
	        if(Integer.parseInt(sAbsent)<0)sAbsent=Integer.toString(0);
	        
	        query = "select count(a_time)LATE from worktime w inner join hireinfo h on w.EMP_NO=h.EMP_NO where to_char(w.a_time,'hh24mi')>to_char(h.start_time,'hh24mi') and w.EMP_NO='"+emp_id+"' and to_char(a_time,'yyyy/mm/dd')>='"+startDate+"' and to_char(a_time,'yyyy/mm/dd')<='"+endDate+"' and E_time IS NOT NULL order by worktime_id";
			pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	sLate=rs.getString("LATE");
	        }
	        query = "select count(e_time)EARLY from worktime w inner join hireinfo h on w.EMP_NO=h.EMP_NO where to_char(w.e_time,'hh24mi')<to_char(h.end_time,'hh24mi') and w.EMP_NO='"+emp_id+"' and to_char(a_time,'yyyy/mm/dd')>='"+startDate+"' and to_char(a_time,'yyyy/mm/dd')<='"+endDate+"' and E_time IS NOT NULL order by worktime_id";
			pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	sEarly=rs.getString("EARLY");
	        }

	        sResult += (startDate+"-"+endDate+"-"+(Double.toString(totalTime)+"-")+ sLate + "-" + sAbsent + "-" + sEarly +"-");
	        
	        for(i=0;i<selectDay;i++) {
	        	sResult += (aTimeArray[i].substring(4, 8)+"-"); 
	        }
	        for(i=0;i<selectDay;i++) {
	        	sResult += (kindDay.substring(i, i+1)+"-");
	        }
	        for(i=0;i<selectDay;i++) {
	        	sResult += (aTimeArray[i].substring(8, 12)+"-");
	        }
	        for(i=0;i<selectDay;i++) {
	        	sResult += (eTimeArray[i].substring(8, 12)+"-");
	        }
	        for(i=0;i<selectDay;i++) {
	        	sResult += (Double.toString(TimeArray[i])+"-");
	        }
	        
	        System.out.println("success");

	        return sResult;
	         
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
	   public static String getCurrentTime(String timeFormat) {
	       return new SimpleDateFormat(timeFormat).format(System.currentTimeMillis());
	    }
	   public static Calendar getCalendar(String str) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(str.substring(0, 4)), Integer.parseInt(str.substring(4,6))-1,
					Integer.parseInt(str.substring(6,8))); 
			return cal;
		}
	   public static int dayList(String str) {
		   	int list=0;
		   	if(str.equals("��"))list=1;
	        else if(str.equals("��"))list=2;
	        else if(str.equals("ȭ"))list=3;
	        else if(str.equals("��"))list=4;
	        else if(str.equals("��"))list=5;
	        else if(str.equals("��"))list=6;
	        else if(str.equals("��"))list=7;
		   	
		   	return list;
		   
	   }
	}
