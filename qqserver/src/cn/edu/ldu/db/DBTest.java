package cn.edu.ldu.db;

import cn.edu.ldu.db.beans.Member;
import cn.edu.ldu.db.tables.MemberManager;
import cn.edu.ldu.security.Cryptography;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class DBTest {   
    public static void main(String[] args) throws SQLException{                      
        //ע��һ�����û�������Ϊ����
        Member bean=new Member();
        bean.setId(40000);
        bean.setName("�ú�ѧϰ");
        bean.setPassword("123456");
        bean.setEmail("hhxx@sina.com");
        bean.setHeadImage("i9003.jpg");
        bean.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));//��ǰʱ��
        MemberManager.registerUser(bean);        
//        ע��һ�����û����������
        Member bean2=new Member();
        bean2.setId(60000);
        bean2.setName("С����");
        String encryptPassword=Cryptography.getHash("654321","sha-256");
        bean2.setPassword(encryptPassword);
        bean2.setEmail("xfx@qq.com");
        bean2.setHeadImage("i9005.jpg");
        bean2.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));//��ǰʱ��
        MemberManager.registerUser(bean2);        
        MemberManager.displayAllRows();//��ʾ�û��б� 
    }//end main
}//end class