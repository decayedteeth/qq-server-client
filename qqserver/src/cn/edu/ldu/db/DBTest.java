package cn.edu.ldu.db;

import cn.edu.ldu.db.beans.Member;
import cn.edu.ldu.db.tables.MemberManager;
import cn.edu.ldu.security.Cryptography;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class DBTest {   
    public static void main(String[] args) throws SQLException{                      
        //注册一个新用户，密码为明文
        Member bean=new Member();
        bean.setId(40000);
        bean.setName("好好学习");
        bean.setPassword("123456");
        bean.setEmail("hhxx@sina.com");
        bean.setHeadImage("i9003.jpg");
        bean.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));//当前时间
        MemberManager.registerUser(bean);        
//        注册一个新用户，密码加密
        Member bean2=new Member();
        bean2.setId(60000);
        bean2.setName("小飞侠");
        String encryptPassword=Cryptography.getHash("654321","sha-256");
        bean2.setPassword(encryptPassword);
        bean2.setEmail("xfx@qq.com");
        bean2.setHeadImage("i9005.jpg");
        bean2.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));//当前时间
        MemberManager.registerUser(bean2);        
        MemberManager.displayAllRows();//显示用户列表 
    }//end main
}//end class