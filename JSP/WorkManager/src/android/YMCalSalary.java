package android;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class YMCalSalary{
   private static YMCalSalary ymcalsalary = new YMCalSalary();

   public static YMCalSalary getYMCalSalary() {
      return ymcalsalary;
   }

   private String returns;
   private Connection conn = null;
   private PreparedStatement pstmt = null;
   private ResultSet rs = null;
   DataSource ds;
   String sWorkedDay, sStd_salary, sHoliday, sStartTime,sEndTime, sRestDay, sKindDay, sRestATime, sRestETime, sWorkTimeId, sHoliDay, sIsMoreFive, sBeforeWeekly, sEmpId, sRestCount, sHoliCount, sRestTime;
  
   public String select(String id, String year, String month) {
      try {
         
         if(month.length()<2)month="0"+month;
        returns ="";
        Context initctx = new InitialContext();

      //���� ������ ������ ��Ƴ��� ������ �̵�
      Context envctx = (Context) initctx.lookup("java:comp/env");

      //������ �ҽ� ��ü�� ����
      ds = (DataSource) envctx.lookup("jdbc/WorkManager");

      conn = ds.getConnection();

      
      //String nowTime = getCurrentTime("YYYY/MM/dd hh:mm"); //���� �ð� ���
      int total, basic, extend, night, weekly, restDay, holiday, deduct, workedDay; //�� �׸� ���� ����
      int[] absent = new int[6]; //��� �迭
      
      /**************�Ű����� id(�޴�����ȣ)�� �̿��ؼ� EMP_NO ���*******************/
       String query = "SELECT EMP_NO FROM EMPLOYEE WHERE EMP_ID='"+id+"'";
       pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
        while(rs.next()) {
        sEmpId = rs.getString("EMP_NO");
        }
        int emp_id = Integer.parseInt(sEmpId);
        
        /**************�ٷ��ں� �ް� �ð� ���*******************/ 
       query = "SELECT REST_TIME FROM HIREINFO where EMP_NO ='"+emp_id+"'";
       pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
        while(rs.next()) {
        sRestTime = rs.getString("REST_TIME");
        }
        double restTime = Integer.parseInt(sRestTime);
        
        query = "SELECT KIND_DAY FROM WORKTIME where EMP_NO ='"+emp_id+"' AND to_char(A_TIME, 'YYYY/MM')='"+year+"/"+month+"' and E_time IS NOT NULL";
         pstmt = conn.prepareStatement(query);
           rs = pstmt.executeQuery();
           while(rs.next()) {
                sKindDay += rs.getString("KIND_DAY");
           } 
        
      /******** �̹� �� 1�� ���� WORKTIME �� ���Ե� �� �ٹ� �ϼ��� �˾Ƴ��� ���� query�� ********/
      query = "SELECT COUNT(WORKTIME_ID)WORKED_DAY FROM WORKTIME where EMP_NO ='"+emp_id+"' AND to_char(A_TIME, 'YYYY/MM')='"+year+"/"+month+"' and E_time IS NOT NULL";
      pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
        while(rs.next()) {
            sWorkedDay=rs.getString("WORKED_DAY"); 
      } 
        workedDay = Integer.parseInt(sWorkedDay); //�̹� ���� �� ��� �ϼ� workedDay�� ����  
      
       String[] aTimeArray = new String [workedDay]; //��� �ð� �迭
       String[] eTimeArray = new String [workedDay]; //��� �ð� �迭
       String[] plusETimeArray = new String [workedDay]; //��� �ð� + 2400 �迭
       double[] TimeArray = new double [workedDay];  //�ٹ� �ð�(��� - ���)�迭
       double[] aDateTime = new double [workedDay];  //��� �ð� �迭�� double��
       double[] eDateTime = new double [workedDay];  //��� �ð� �迭�� double��
       Date[] aDate = new Date [workedDay];          //��� �ð� �迭�� Date�� 
       Date[] eDate = new Date [workedDay];         //��� �ð� �迭�� Date��
       int i, j;                            //�ݺ��� ����� ���� ���� i,j
       
       /**************����� �ð� �迭(String��)�� DB �� ���� **************/
       i=0;
       query = "SELECT to_char(a_time, 'YYYYMMDDHH24mi')atime, to_char(e_time, 'YYYYMMDDHH24mi')etime FROM WORKTIME where EMP_NO ='"+emp_id+"' AND TO_CHAR(A_TIME,'YYYY/MM')='"+year+"/"+month+"' and E_time IS NOT NULL order by worktime_id";
       pstmt = conn.prepareStatement(query);
       rs = pstmt.executeQuery();  
       while(rs.next()) {
           aTimeArray[i]=rs.getString("atime");
           eTimeArray[i++]=rs.getString("etime");
     } 
       
      /*****************Date Format ���� �� ����� �迭�� Date��, DateTime��(double) �ʱ�ȭ*******************/
       SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHmm");
       double Time;
       long longETime;
       for(i=0;i<workedDay;i++) {
          
          aDate[i] = (Date) dateFormat.parse(aTimeArray[i]);
          aDateTime[i] = aDate[i].getTime();
          
          eDate[i] = (Date) dateFormat.parse(eTimeArray[i]);
          eDateTime[i] = eDate[i].getTime();
          
          Time = eDateTime[i]-aDateTime[i];
         
          if(Time<0) {                                       //Time���� ������ ��츦 ���� ����(�߰� �ٹ� ���� ��, �ڼ��� ����)
             longETime = Long.parseLong(eTimeArray[i]);
             plusETimeArray[i] = Long.toString(longETime+2400);
             eDate[i] = (Date) dateFormat.parse(plusETimeArray[i]);
             eDateTime[i] = eDate[i].getTime();
             Time = eDateTime[i]-aDateTime[i];
          }
          
           TimeArray[i] = Time/60000/60-(restTime/60.0);   //��ȸ�Ⱓ�� �ް� �ð��� ������ ���� �ٹ� �ð� ���� �迭
          
           if(TimeArray[i]<0){
             TimeArray[i]+=restTime/60;
          }
       }
       
      
       /**********���� �ñ� ����***********/
        query = "SELECT std_salary FROM hireinfo h inner join employee e on h.EMP_NO = e.emp_no where e.emp_id ='"+id+"'";
      pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
      
        while(rs.next()) {
           sStd_salary = rs.getString("std_salary");
      } 
        int std_salary = Integer.parseInt(sStd_salary);
        
        /************�⺻�� ���**************/
        basic = 0;
        for(i=0;i<workedDay;i++) {
           basic += (std_salary*TimeArray[i]);
        }
        
        /**************5�� �̻� ���ü ���� ����****************/
        query = "SELECT ISMOREFIVE FROM hireinfo h inner join employee e on h.EMP_NO = e.emp_no where e.emp_id ='"+id+"'";
      pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
        while(rs.next()) {
           sIsMoreFive = rs.getString("ISMOREFIVE");
      } 
        
        /********************����� �迭 Calendar�� �迭************************/
        int[] weekDay = new int[workedDay];
        Calendar[] aTimeCal = new Calendar[workedDay];
        Calendar[] eTimeCal = new Calendar[workedDay];
        double[] weeklyTimeArray = new double [6]; //�ְ� �ٹ� �ð�
        
        /**********������� �� ���� ���° ������ �����Ͽ�, �� ���� ���� �ٹ� �ð� �߰�**********/
        j=0;
        for(i=0;i<workedDay;i++) {
           aTimeCal[i] = getCalendar(aTimeArray[i]);
           eTimeCal[i] = getCalendar(eTimeArray[i]);
           weekDay[i] = aTimeCal[i].get(Calendar.WEEK_OF_MONTH);  //�� ������� ���������� ����
           if(i==0)weeklyTimeArray[j] += (TimeArray[i]);
           if(0<i) {
           if(weekDay[i-1] == weekDay[i]) {      //������ ������ ������ ���� ���
              weeklyTimeArray[j] += (TimeArray[i]); //���� �迭�� �ٹ� �ð� ����
           }
           else if(weekDay[i-1]!=weekDay[i]) {       //������ ������ ������ �ٸ� ���
              if(j<(5))j++;
              weeklyTimeArray[j] += (TimeArray[i]); //���� ������ �迭�� �ٹ� �ð� ����
              }
           }
        }
        
        SimpleDateFormat HHmiFormat = new SimpleDateFormat("HHmm");   //�ð��� DateFormat ����
        SimpleDateFormat MMFormat = new SimpleDateFormat("MM");  //���� DateFormat ����
        
        extend = 0; night = 0;holiday=0; restDay=0;
        double weekly_time = 0;
       
        if(sIsMoreFive.length()>0) { //5�� �̻� ���ü�ϰ��
           
           /***************�߰� ����*********************/
           
           String ten = "2200", six = "0600";      //���� �ð�
           
           Date tenDate = (Date) HHmiFormat.parse(ten);            //Date���� DateTime��(double) ���� �ʱ�ȭ
           double tenDateTime = tenDate.getTime();
          Date sixDate = (Date) HHmiFormat.parse(six);
          double sixDateTime = sixDate.getTime();
          int[] intATimeArray = new int [workedDay];
           int[] intETimeArray = new int [workedDay];
         
          double buffer;                                    //buffer�� ����� ����
          Long bufferTime;
              
           for(i=0;i<workedDay;i++)  {                           //�̹� ���� ��� �ٹ��Ͽ� ���Ͽ�
              intATimeArray[i] = Integer.parseInt(aTimeArray[i].substring(8, 12));
                intETimeArray[i] = Integer.parseInt(eTimeArray[i].substring(8, 12));
              
              six = "0600";                                 //�ؿ��� six ���� ���ϴ� ��찡 �ֱ� ������ ������� ������ ���� �ڵ�
              sixDate = (Date)HHmiFormat.parse(six);
              sixDateTime=sixDate.getTime();
              
              ten = "2200";                                 //�ؿ��� ten ���� ���ϴ� ��찡 �ֱ� ������ ������� ������ ���� �ڵ�
              tenDate = (Date)HHmiFormat.parse(ten);
              tenDateTime=tenDate.getTime();
              
              aDate[i] = (Date) HHmiFormat.parse(aTimeArray[i].substring(8, 12));
              aDateTime[i]=aDate[i].getTime();
              eDate[i] = (Date) HHmiFormat.parse(eTimeArray[i].substring(8, 12));
              eDateTime[i]=eDate[i].getTime();
              
              if(intATimeArray[i]>=Integer.parseInt(ten)||intATimeArray[i]<Integer.parseInt(six)) {                     //��ٽð��� 22�� ���� �϶�   
                 if(intETimeArray[i]<=Integer.parseInt(six)) {                  //��ٽð��� 6�� �����ϰ��
                    night += (int) (std_salary*TimeArray[i]*0.5);   //�߰� ���� = ���ؽñ�*�ٹ��ð�*0.5
                 }
                 else if(intETimeArray[i]>Integer.parseInt(six)&&intETimeArray[i]<Integer.parseInt(ten)) {   //��ٽð��� 06~22�� �� ���
                    
                    buffer=(sixDateTime-aDateTime[i])/60000/60-(restTime/60.0);
                    if(sixDateTime-aDateTime[i]/60000/60<restTime/60.0) buffer+=(restTime/60.0);
                    
                    if(sixDate.compareTo(aDate[i])<0) {                                    //�ð� ��� ���� ������ �Ǵ� ���� ���� ���� �ڵ�
                       bufferTime = Long.parseLong(six);                  //��) ��� �ð��� 23���̰� ��� �ð��� 08���� ��� 08-23 = -15�� �Ǳ� ������
                       six = Long.toString(bufferTime+2400);            //�̸� ���� ���� ��� �ð��� 24�ð��� ����
                       sixDate = (Date) HHmiFormat.parse(six);
                       sixDateTime = sixDate.getTime();
                       buffer=(sixDateTime-aDateTime[i])/60000/60-(restTime/60.0);         //��� �ð�~6�ñ����� �ð���ŭ �߰� �������� ����
                       if(sixDateTime-aDateTime[i]/60000/60<restTime/60.0)buffer+= (restTime/60.0);
                    }
                    night += (int) (std_salary*(buffer)*0.5);
                 }
              }
              else if(intATimeArray[i]<=Integer.parseInt(ten)&&intATimeArray[i]>Integer.parseInt(six)) {                           //��� �ð��� 22�� �����̰�
                 if(intETimeArray[i]<=Integer.parseInt(six)) {                           //��� �ð��� 6�� ������ ���
                    buffer=(eDateTime[i]-tenDateTime)/60000/60-(restTime/60.0);                  //�߰� ���� �ð��� ��� �ð� - 22��
                    if(eDateTime[i]-tenDateTime/60000/60<restTime/60.0)buffer+= (restTime/60.0);
                    if(eDate[i].compareTo(tenDate)<0) {                                       //������ ��� 24�ð� �߰� ����
                       eDate[i] = (Date) HHmiFormat.parse(Integer.toString(Integer.parseInt(eTimeArray[i].substring(8, 12))+2400));
                       eDateTime[i] = eDate[i].getTime();
                       buffer=(eDateTime[i]-tenDateTime)/60000/60-(restTime/60.0);
                       if(eDateTime[i]-tenDateTime/60000/60<restTime/60.0)buffer+= (restTime/60.0);
                    }
                    night += (int) (std_salary*(buffer)*0.5);
                 }
              
                 else if(intETimeArray[i]>Integer.parseInt(six)&&intETimeArray[i]<=Integer.parseInt(ten)&&(!aTimeArray[i].substring(6, 8).equals(eTimeArray[i].substring(6, 8)))) {         //��� �ð��� 6��~22���� ���
                    
                    buffer = 8.0-(restTime/60.0);
                       night += (int) (std_salary*(buffer)*0.5);   //�߰� ���� �ð��� 6��-22��
                       }
                    }
              }
           
           
           /**************���� ����*******************/
           
           Date nowTimeDate = MMFormat.parse(month);

           String FebFir = "02";                              //���� ���� �����
           Date FebFirDate = MMFormat.parse(FebFir);                  //Date������ ��ȯ

           if(nowTimeDate.compareTo(FebFirDate)==0) {  //���� ������ �ų� 2�� 1�Ͽ��� ���
          
              int leftHoli;                     //DB���� ���� ���� �ϼ� ���
              query = "SELECT holiday FROM hireinfo h inner join employee e on h.EMP_NO = e.emp_no where e.emp_id ='"+id+"'";
            pstmt = conn.prepareStatement(query);
              rs = pstmt.executeQuery();
              while(rs.next()) {
                 sHoliday = rs.getString("holiday");
            } 
           leftHoli = Integer.parseInt(sHoliday);
           //��� �������� ���� ��� �ð� �� ��� �ð� ��� 
           query = "SELECT TO_CHAR(START_TIME,'HH24mi')START_TIME,TO_CHAR(END_TIME,'HH24mi')END_TIME  FROM hireinfo h inner join employee e on h.EMP_NO = e.emp_no where e.emp_id ='"+id+"'";
         pstmt = conn.prepareStatement(query);
           rs = pstmt.executeQuery();
           double holiTime=0;
           while(rs.next()) {
              sStartTime = rs.getString("START_TIME");
              sEndTime = rs.getString("END_TIME");
              
              Date startDate = (Date) HHmiFormat.parse(sStartTime);
              Date endDate = (Date) HHmiFormat.parse(sEndTime);
              double startDateTime = startDate.getTime();
              double endDateTime = endDate.getTime();
              if(endDateTime-startDateTime<0) {
                 long longEndTime = Long.parseLong(sEndTime);
                 sEndTime = Long.toString(longEndTime+2400);
              }
              holiTime = (endDateTime-startDateTime)/60000/60;      //1�� ��� �ٹ� �ð�
         } 
           
           holiday += leftHoli*holiTime*std_salary;               //���� ���� �� * 1�� ��� �ӱ� 
          }
           
           /***************���� ����********************/ 
           double restADateTime, restEDateTime, restDateTime, restWorkedTime;
           Date restADate, restEDate;
           int restCount, holiCount;
           long longRestETime;
           
           query = "SELECT rest_day FROM hireinfo where EMP_NO ='"+emp_id+"'"; //�ش� ���̵���  ������ �������� 
         pstmt = conn.prepareStatement(query);
           rs = pstmt.executeQuery();
           while(rs.next()) {
              sRestDay = rs.getString("rest_day");
           }
           query = "SELECT count(worktime_id)count_rest FROM worktime where EMP_NO ='"+emp_id+"'and kind_day='"+sRestDay+"' and a_time=TO_DATE('"+year+"/"+month+"','YYYY/MM') and E_time IS NOT NULL"; 
           pstmt = conn.prepareStatement(query);                                                      //���Ͽ� �ٹ��� Ƚ�� ��������
             rs = pstmt.executeQuery();
             while(rs.next()) {   
                sRestCount = rs.getString("count_rest");
             }
             restCount = Integer.parseInt(sRestCount);
   
              sWorkTimeId=""; i=0;
              String[] workTimeId = new String [restCount];
              query = "SELECT worktime_id FROM worktime where EMP_NO ='"+emp_id+"' and kind_day='"+sRestDay+"'and a_time=TO_DATE('"+year+"/"+month+"','YYYY/MM') and E_time IS NOT NULL"; 
              pstmt = conn.prepareStatement(query);                                                      //�ٹ����� �����ϰ� ���� ���, �ش�  worktime_id�� ��������
                rs = pstmt.executeQuery();
                while(rs.next()) {   
                   sWorkTimeId = rs.getString("worktime_id");
                   workTimeId[i] = sWorkTimeId;
                   if(i<(restCount-1))i++;
                }
              
                   for(i=0;i<restCount;i++) {
                       query = "SELECT TO_CHAR(a_time,'HH24mi')A_TIME, TO_CHAR(e_time,'HH24mi')E_TIME FROM worktime where EMP_NO ='"+emp_id+"' and worktime_id='"+workTimeId[i]+"'and a_time=TO_DATE('"+year+"/"+month+"','YYYY/MM') and E_time IS NOT NULL";
                      pstmt = conn.prepareStatement(query);                                                //worktime_id�� �̿��� �����Ͽ� �ٹ��� ���� ����� �ð� ��������
                        rs = pstmt.executeQuery();
                        while(rs.next()) {                        //����� �ð� DateTime��(double)���� ��ȯ
                           sRestATime = rs.getString("A_TIME");
                           sRestETime = rs.getString("E_TIME");
                           restADate = (Date) HHmiFormat.parse(sRestATime);
                           restEDate = (Date) HHmiFormat.parse(sRestETime);
                           restADateTime = restADate.getTime();
                            restEDateTime = restEDate.getTime();
                           
                           restDateTime = restEDateTime-restADateTime; 
                           if(restDateTime<0) {
                              longRestETime = Long.parseLong(sRestETime);
                              sRestETime = Long.toString(longRestETime+2400);
                              restEDate = (Date) HHmiFormat.parse(sRestETime);
                              restEDateTime = restEDate.getTime();
                              restDateTime = restEDateTime-restADateTime;
                          }
                           restWorkedTime = restDateTime/60000/60-(restTime/60.0);
                           if((restDateTime/60000/60)<(restTime/60.0))restWorkedTime+=restTime/60.0;
                           restDay += std_salary*restWorkedTime*0.5;         //���� ���� +=���� �ñ� *�ٹ� �ð� *0.5
                      }          
                   }   
                   
           sHoliDay=""; sWorkTimeId=""; 
           query = "SELECT count(holi_date)holi_count FROM holiday where to_char(holi_date,'MM')='"+month+"'";       //���� �� ���� �系 ���� ������ ����(�ٷ����� �� ����) ��������
         pstmt = conn.prepareStatement(query);
           rs = pstmt.executeQuery();
           while(rs.next()) {
              sHoliCount = rs.getString("holi_count");
           }
           holiCount = Integer.parseInt(sHoliCount);
           
           i=0;
           String[] holiDate= new String[holiCount]; 
           query = "SELECT to_char(holi_date,'MM/DD')holi_date FROM holiday where to_char(holi_date,'MM')='"+month+"'";      //DB���� �系 ���� ����(�ٷ����� �� ����) ��������
         pstmt = conn.prepareStatement(query);
           rs = pstmt.executeQuery();
           while(rs.next()) {
              sHoliDay = rs.getString("holi_date");
              holiDate[i] = sHoliDay;
              if(i<(holiCount))i++;
           }
          
           workTimeId = new String[holiCount];
           for(i=0;i<holiCount;i++) {
              query = "SELECT worktime_id FROM worktime where EMP_NO ='"+emp_id+"'and to_char(a_time,'MM/DD')='"+holiDate[i]+"' and to_char(a_time,'YYYY/MM')='"+year+"/"+month+"' and E_time IS NOT NULL"; 
              pstmt = conn.prepareStatement(query);                                                      //���Ͽ� �ٹ��� worktime_id ��������
                rs = pstmt.executeQuery();
                while(rs.next()) {   
                   sWorkTimeId = rs.getString("worktime_id");
                }
                workTimeId[i] = sWorkTimeId;
              }
           
           for(i=0;i<holiCount;i++) {      //�系 ���� �����̸鼭 �����Ͽ� �ٹ��� ����� �ߺ� ����
           query = "SELECT kind_day FROM worktime where EMP_NO ='"+emp_id+"'and worktime_id='"+workTimeId[i]+"' and E_time IS NOT NULL"; 
           pstmt = conn.prepareStatement(query);                                                      
             rs = pstmt.executeQuery();
             while(rs.next()) {   
                if(rs.getString("kind_day").equals(sRestDay))workTimeId[i]="-1";
                }
           }
    
           for(i=0;i<holiCount;i++) {
                     query = "SELECT TO_CHAR(a_time,'HH24mi')A_TIME, TO_CHAR(e_time,'HH24mi')E_TIME FROM worktime where EMP_NO ='"+emp_id+"' and worktime_id='"+workTimeId[i]+"' and E_time IS NOT NULL";
                     pstmt = conn.prepareStatement(query);                                 //worktime_id�� �̿��� �����Ͽ� �ٹ��� ���� ����� �ð� ��������
                       rs = pstmt.executeQuery();
                       while(rs.next()) {
                          sRestATime = rs.getString("A_TIME");
                          sRestETime = rs.getString("E_TIME");
                           
                          restADate = (Date) HHmiFormat.parse(sRestATime);
                           restEDate = (Date) HHmiFormat.parse(sRestETime);
                           restADateTime = restADate.getTime();
                           restEDateTime = restEDate.getTime();
                           
                           restDateTime=restEDateTime-restADateTime;
                           
                          if(restDateTime<0) {
                              longRestETime = Long.parseLong(sRestETime);
                              sRestETime = Long.toString(longRestETime+2400);
                              restEDate = (Date) HHmiFormat.parse(sRestETime);
                              restEDateTime = restEDate.getTime();
                              restDateTime = restEDateTime-restADateTime;
                          }
                           restWorkedTime = restDateTime/60000/60-(restTime/60.0);
                           if((restDateTime/60000/60)<(restTime/60.0))restWorkedTime+=restTime/60.0;
                           restDay += std_salary*restWorkedTime*0.5;         //���� ���� +=���� �ñ� *�ٹ� �ð� *0.5
                     }          
                  } 
                }
           
          
        
        
        
        
        /************������ ��� Ƚ���� ���ϱ� ���� ���� ���� �� �ʱ�ȭ****************/
        String hireWorkDay="";                        //�ٹ� ����
        String sRecordWorkDay = new String();   //���� ����� ���� ����
        String[] weeklyRecordWorkDay = new String[6];      //������ ����� ������ ���Ϲ迭
        for(j=0;j<6;j++) weeklyRecordWorkDay[j]="";         //�迭 �ʱ�ȭ
        
        /*******************�ٹ� ���� DB���� ���*************************/
        query = "SELECT WORK_DAY FROM hireinfo h inner join employee e on h.EMP_NO = e.emp_no where e.emp_id ='"+id+"'";
      pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
        while(rs.next()) {
           hireWorkDay = rs.getString("work_day");
      } 
        /**************���� ����� ���� ���� DB���� ���**********************/
        query = "SELECT kind_day FROM worktime w inner join employee e on w.EMP_NO = e.emp_no where to_char(a_time,'yyyy/mm')='"+year+"/"+month+"' and e.emp_id ='"+id+"' and E_time IS NOT NULL order by worktime_id";
      pstmt = conn.prepareStatement(query);
        rs = pstmt.executeQuery();
        i = 0;
        while(rs.next()) {
           sRecordWorkDay += rs.getString("kind_day");
      }
        String recordWorkDay="";
        for(i=0;i<(sRecordWorkDay.length()-1);i++) {
           recordWorkDay += dayList(sRecordWorkDay.substring(i,i+1));
        }
        /****************�������� ��� ���� ���ڿ��� �߰�********************/
        Calendar cal; 
        for(i=0;i<(workedDay);i++) {
           cal = getCalendar(aTimeArray[i]);
           j = cal.get(Calendar.WEEK_OF_MONTH)-1;
           if(i==0) weeklyRecordWorkDay[j] += sRecordWorkDay.substring(i,i+1);
           
           if(i>0) {
           if(weekDay[i-1] == weekDay[i]) {               //������ ������ ������ ������
             weeklyRecordWorkDay[j] += sRecordWorkDay.substring(i,i+1);      //���� �迭�� ���� �߰�
          }
          else if(weekDay[i-1]!=weekDay[i]) {               //������ ������ ������ �ٸ���
            /* if(j<5)j++;*/
             weeklyRecordWorkDay[j] += sRecordWorkDay.substring(i,i+1);   //���� ���� �迭�� ���� �߰�
             }
           }
        }
        
        /*************�ٹ� ���ϰ� ��� ���� �迭�� ���ϱ� ���� Calendar********************/
        String sWeeklyFirstWorkDay = hireWorkDay.substring(0,1);
        int weeklyFirstWorkDay = Integer.parseInt(dayList(sWeeklyFirstWorkDay)); //������ ù���� �ǹ� �ٹ� ���� ���
        String sWeeklyLastWorkDay = hireWorkDay.substring((hireWorkDay.length()-1),hireWorkDay.length());
        int weeklyLastWorkDay = Integer.parseInt(dayList(sWeeklyLastWorkDay));   //������ ������ �ǹ� �ٹ� ���� ���
        cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        int firstDay = cal.get(Calendar.DAY_OF_WEEK);                   //�̹��� 1���� ����
        if(firstDay==1)firstDay=7;
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDay = cal.get(Calendar.DAY_OF_WEEK);                  //�̹��� ������ ���� ����
        //if(lastDay>hireWorkDay.length())lastDay=hireWorkDay.length();
        //if(lastDay == 1)lastDay=7;
        String lastWeekDay="";      
        if(lastDay==1)lastWeekDay="��";               //�������� �ٹ� ������ ����
        else if(lastDay==2)lastWeekDay="�Ͽ�";
        else if(lastDay==3)lastWeekDay="�Ͽ�ȭ";
        else if(lastDay==4)lastWeekDay="�Ͽ�ȭ��";
        else if(lastDay==5)lastWeekDay="�Ͽ�ȭ����";
        else if(lastDay==6)lastWeekDay="�Ͽ�ȭ�����";
        else lastWeekDay="�Ͽ�ȭ�������";
        
        int n=1;
        boolean isAllDay;
        for(i=0;i<hireWorkDay.length();i++) {         //�������ֿ� �ٹ� ������ ���� ������ true �ƴϸ� false
           n*=lastWeekDay.indexOf(hireWorkDay.substring(i,i+1));
        }
        if(n>0)isAllDay=true;
        else isAllDay=false;
        
        String lastWeekWorkDay="";
        /********(�ٹ������߿���) ù° ���� ���� ���ڿ��� ������ ���� ���� ���ڿ��� ����********///�ٹ����� ��ȭ������� ��
        
        String firstWeekWorkDay= "�Ͽ�ȭ�������".substring(firstDay-1,weeklyLastWorkDay);    //ù°�ִ�, 11�� �������� ���
        if(weeklyLastWorkDay>lastDay) lastWeekWorkDay= "�Ͽ�ȭ�������".substring((weeklyFirstWorkDay-1), lastDay);   //�� ���� ������ ���� ������ �� ���� ������ �ǹ� �ٹ����� ���Ϻ��� ������ ������ �ִ� �� ���� ������ ������ �ٹ���
        else lastWeekWorkDay="�Ͽ�ȭ�������".substring((weeklyFirstWorkDay-1), weeklyLastWorkDay);   //������ ���� ������ �� ���� ������ �ǹ� �ٹ����� ���Ϻ��� ũ��, ������ �ִ� �� ���� ������ �ٹ��ϱ��� �ٹ���
        cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, Calendar.MONTH+1, cal.getActualMaximum(Calendar.DATE));   // ���� ������ ��¥�� �־ �������ش�.
      int lastWeek = cal.get(Calendar.WEEK_OF_MONTH);
      for(i=0;i<firstWeekWorkDay.length();i++) {
         if(weeklyRecordWorkDay[0].indexOf(firstWeekWorkDay.substring(i,i+1))==-1)absent[0]++;
      }
        
        /***************2~6���� ��� Ƚ�� ���*******************/
        for(j=1;j<6;j++) {
           if(lastWeek-1 != j) {
              for(i=0;i<hireWorkDay.length();i++) {
                 if(weeklyRecordWorkDay[j].indexOf(hireWorkDay.substring(i,i+1))==-1)absent[j]++;
              }
           }
           else {
              for(i=0;i<lastWeekWorkDay.length();i++) {
                 if(weeklyRecordWorkDay[j].indexOf(lastWeekWorkDay.substring(i, i+1))==-1) absent[j]++;
              }
           }
        }
        
        /*****************���� ����**********************/
       
        weekly=0; sBeforeWeekly="0";
        String SQL, firWorkedDay="", xFirWorkedDay="";
        int thisMonth=0, beforeMonth=0;
        query = "SELECT MIN(to_char(a_time,'DD'))xFirWorkedDay from worktime where EMP_NO='"+emp_id+"' and to_char(a_time,'YYYYMM')='"+Integer.toString(Integer.parseInt(year+month)-1)+"' and E_time IS NOT NULL";
      pstmt = conn.prepareStatement(query);      //�̹� �޿� ���� �� ù ���� ��¥�� ���
        rs = pstmt.executeQuery(); 
        while(rs.next()) {
           
           xFirWorkedDay +=rs.getString("xFirWorkedDay");
      }
        if(xFirWorkedDay.length()<2)xFirWorkedDay="0"+xFirWorkedDay; //ù ���� 1�� �ڸ��� ���̰ų� ������ �ٹ� ���� �ʾ��� ���, 0�� �տ� ���� ex)1->01
        
        
        query = "SELECT MIN(to_char(a_time,'DD'))firWorkedDay from worktime where EMP_NO='"+emp_id+"' and to_char(a_time,'YYYYMM')='"+Integer.toString(Integer.parseInt(year+month))+"' and E_time IS NOT NULL";
      pstmt = conn.prepareStatement(query);      //�̹� �޿� ���� �� ù ���� ��¥�� ���
        rs = pstmt.executeQuery(); 
        while(rs.next()) {
           
           firWorkedDay +=rs.getString("firWorkedDay");
      }
       
        if(firWorkedDay.length()<2)firWorkedDay="0"+firWorkedDay; //ù ���� 1�� �ڸ��� ���� ���, 0�� �տ� ���� ex)1->01
        
        for(j=0;j<6;j++) {
           weekly_time = weeklyTimeArray[j];      //�ְ� �ٹ� �ð�
           if(j==0) {
              cal = Calendar.getInstance();
              cal.set(Calendar.YEAR, Calendar.MONTH+1, cal.getActualMaximum(Calendar.DATE));   // ���� ������ ��¥�� �־ �������ش�.
             lastWeek = cal.get(Calendar.WEEK_OF_MONTH);
             cal = Calendar.getInstance();
             cal.add(Calendar.MONTH, -1); 
             beforeMonth = cal.get(Calendar.MONTH)+1;
  
             query = "SELECT before_weekly FROM worktime where EMP_NO='"+emp_id+"' and to_char(a_time, 'MMdd')='"+Integer.toString(beforeMonth)+firWorkedDay+"' and E_time IS NOT NULL";
             pstmt = conn.prepareStatement(query);
               rs = pstmt.executeQuery();
               
               while(rs.next()) {
                  sBeforeWeekly = rs.getString("before_weekly");
             }
               weekly_time +=Integer.parseInt(sBeforeWeekly);             
           }
           
           if(weekly_time>=15&&absent[j]==0) {      //�ְ� �ٹ� �ð��� 15�ð� �̻��̰�, ���ֿ� ���� �Ͽ��� ��
              if(lastWeek-1 != j){   //������ �ְ� ������ ������ �ƴϸ� 
                       weekly += (int) (std_salary*(weekly_time/40.0)*8);   //���� ���� ����                       
              }
              else if(lastWeek-1 == j&&!isAllDay) {         //�̹��ְ� ������ ���̰�, ���ֿ� �ٹ����� ���� ���� ���� ���
                 cal = Calendar.getInstance();
                 cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                 thisMonth = cal.get(Calendar.MONTH)+1;                                          //WORKTIME�� �ش� ������� �̹��� ���� ù° �� Į���� �� ���� ������ �� �ٹ� �ð��� �����Ѵ�.
                 SQL = "update worktime set before_weekly='"+weekly_time+"' where to_char(a_time, 'MMdd')='"+Integer.toString(thisMonth)+firWorkedDay+"' and EMP_NO ='"+emp_id+"'";
                 pstmt = conn.prepareStatement(SQL);
              }
              else {                                 //������ ��������, �� ���� ���� �ٷ��ϸ�ŭ ���� ������ ���
                 weekly += (int) (std_salary*(weekly_time/40.0)*8);   //���� ���� ����  
              }
           }
           /***********���� ����************/
           
           if(sIsMoreFive.length()>0){               //5�� �̻� ���ü�� ���
             
             int[] startDay = new int [6];         //�� ���� ù° ��¥(��)�� �����ϴ� �迭
             int[] endDay= new int [6];             //�� ���� ������ ��¥(��)�� �����ϴ� �迭
             
             for(n=0;n<startDay.length;n++) {startDay[n]=-1;}  //�迭 ���� -1�� �ʱ�ȭ
              startDay[0] = 0;                  //ù�� ° ���� ù ���� �׻� 1
             for(i=1, n=1;i<workedDay-1;i++) {
                 if(weekDay[i]!=weekDay[i-1]) {      //i���� �� ���� ù° ���̸�
                    startDay[n] = i;         //(i+1)���� ����
                    if(n<5)n++;   
                 }
             }
             
               for(n=0;n<endDay.length;n++) {endDay[n]=-1;}  //�迭 ���� -1�� �ʱ�ȭ
                  for(i=0, n=0;i<workedDay-1;i++) {            
                     if(weekDay[i]!=weekDay[i+1]) { //i���� �� ���� ������ ���̸�
                        endDay[n] = i;         //(i+1)���� ����
                        if(n<5)n++;               
                     }
                  }
                  endDay[n] = workedDay;             //���� ������ ��¥(������ ���� ������ ��) ����
                  
                 
                  if(lastWeek-1 == j && lastDay != 7) { //�̹��ְ� ������ ���̰� ������ ���� �������(������ ��) �ƴ� ���, �̹� �� �ٹ� �ð��� DB�� ����
                     cal = Calendar.getInstance();
                     cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                     thisMonth = cal.get(Calendar.MONTH)+1;                                          //WORKTIME�� �ش� ������� �̹��� ������ �� Į���� ������ ������ �� �ٹ� �ð��� �����Ѵ�.
                     SQL = "update worktime set before_extend='"+weekly_time+"' where to_char(a_time, 'MMdd')='"+Integer.toString(thisMonth)+"01"+"' and EMP_NO ='"+emp_id+"'";
                     pstmt = conn.prepareStatement(SQL);
                  }
                  else if(weekly_time>40.0) {               //�ְ� �ٹ� �ð��� 40�ð� �̻��Ͻ�
                extend += (weekly_time-40.0)*std_salary*0.5;   //���� ����
             }
                  else {
                     if(startDay[j]!=-1) {
                        for(i=startDay[j];i<endDay[j];i++) {
                           if(TimeArray[i]>8.0) {
                           extend += (TimeArray[i]-8.0)*std_salary*0.5;
                           }
                        }
                     } 
                  }
             }
           }
        
        /*************���� ����(4�뺸�� + ��õ��)*****************/
       deduct = 0;
       int preTotal = basic + extend + night + weekly + restDay + holiday; //�⺻ �ް� ������ ��� ���� ��
       deduct += preTotal * (0.0844+0.033);      //����
       
       /***************���� ���� ����******************/
       total = preTotal - deduct;      //1�� �ڸ� �ø�
       if(total%10!=0) total = total/10*10+10;
       
       /***************�� �׸� Ȯ�ο� ���********************/
       System.out.print(year+"/"+month+":\t");
       returns = total+"-"+basic+"-"+extend+"-"+night+"-"+weekly+"-"+restDay+"-"+holiday+"-"+deduct;
       
       pstmt.executeUpdate();          
      } catch (Exception e) {
         e.printStackTrace();
      } // end try~catch

      finally {
          if (pstmt != null)
             try {
               if(rs != null) rs.close();
               if(pstmt != null) pstmt.close();
               if(conn != null) conn.close();
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
            Integer.parseInt(str.substring(6,8)), Integer.parseInt(str.substring(8,10)),
                  Integer.parseInt(str.substring(10,12))); 
      return cal;
   }
   public static String dayList(String str) {
         String list="";
         if(str.equals("��"))list="1";
       else if(str.equals("��"))list="2";
       else if(str.equals("ȭ"))list="3";
       else if(str.equals("��"))list="4";
       else if(str.equals("��"))list="5";
       else if(str.equals("��"))list="6";
       else if(str.equals("��"))list="7";
         
         return list;
      
  }

}