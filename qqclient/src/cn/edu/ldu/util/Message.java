package cn.edu.ldu.util;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Message����Ϣ�࣬����Ự��Ϣ�ṹ���涨�ỰЭ�顣
 * @author ����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class Message implements Serializable {
    private String userId=null; //�û�id
    private String password=null; //����
    private String type=null; //��Ϣ���ͣ�M_LOGIN:�û���¼��Ϣ��M_SUCCESS:��¼�ɹ���M_FAULURE:��¼ʧ�ܣ�M_ACK:�������Ե�¼�û��Ļ�Ӧ��Ϣ��M_MSG:�Ự��Ϣ��M_QUIT:�û��˳���Ϣ
    private String text=null; //��Ϣ��
    private InetAddress toAddr=null; //Ŀ���û���ַ
    private int toPort; //Ŀ���û��˿�
    private String targetId=null; //Ŀ���û�id
    private String fileType=null;
    private String[] flielist=null;
//    private 
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public InetAddress getToAddr() {
        return toAddr;
    }
    public void setToAddr(InetAddress toAddr) {
        this.toAddr = toAddr;
    }
    public int getToPort() {
        return toPort;
    }
    public void setToPort(int toPort) {
        this.toPort = toPort;
    }
    public String getTargetId() {
        return targetId;
    }
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String[] getFlielist() {
		return flielist;
	}
	public void setFlielist(String[] flielist) {
		this.flielist = flielist;
	}   
}