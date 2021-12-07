package cn.edu.ldu.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
  
	
	  private static final String userName = "root";                          //数据库用户名
      private static final String userPwd = "123456";                        //密码
      private static final String dbName = "qqdb";                        //数据库名
      private static final String  url1="jdbc:mysql://localhost:3306/"+dbName;
      private static final String driverName = "com.mysql.jdbc.Driver";         //驱动程序名


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