package cn.edu.ldu.db.beans;

import java.sql.Timestamp;


public class Member { //�����Աʵ���࣬���������ֶ�����ͬ������Ҳ��ͬ
    private int id; //��Ӧ���ݱ��е�id
    private String name; //��Ӧ���ݱ��е�name
    private String password; //��Ӧ���ݱ��е�password
    private String email; //��Ӧ���ݱ��е�email
    private Timestamp time; //��Ӧ���ݱ��е�time
    private String headImage;//��Ӧ���ݱ��е�headImage
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    } 

    public String getHeadImage() {return headImage;}
    public void setHeadImage(String headImage) {this.headImage = headImage;}
    
}