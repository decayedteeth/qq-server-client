package cn.edu.ldu.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
  
	
	  private static final String userName = "root";                          //���ݿ��û���
      private static final String userPwd = "123456";                        //����
      private static final String dbName = "qqdb";                        //���ݿ���
      private static final String  url1="jdbc:mysql://localhost:3306/"+dbName;
      private static final String driverName = "com.mysql.jdbc.Driver";         //����������


    public static Connection getConnection() throws SQLException {
    	try {
			Class.forName(driverName);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return DriverManager.getConnection(url1, userName, userPwd);
    }//end getConnection
}//end class