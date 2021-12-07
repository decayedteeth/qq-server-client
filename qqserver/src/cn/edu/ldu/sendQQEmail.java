package cn.edu.ldu;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

public class sendQQEmail{
public static void sendMail(String fromMail,String user,String password,String toMail,String mailTitle,String mailContent) throws Exception {  
	Properties props = new Properties(); //���Լ���һ�������ļ�
	// ʹ��SMTP�����ʼ�����Э��
	props.put("mail.smtp.host", "smtp.qq.com");//�洢�����ʼ�����������Ϣ
	props.put("mail.smtp.port", "465");
	props.put("mail.smtp.auth", "true");//ͬʱͨ����֤
	
	MailSSLSocketFactory sf = new MailSSLSocketFactory();
	sf.setTrustAllHosts(true);
	props.put("mail.smtp.ssl.enable", "true");
	props.put("mail.smtp.ssl.socketFactory", sf);
	
	Session session = Session.getInstance(props);//���������½�һ���ʼ��Ự
	MimeMessage message = new MimeMessage(session);//���ʼ��Ự�½�һ����Ϣ����
	message.setFrom(new InternetAddress(fromMail));//���÷����˵ĵ�ַ
	message.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));//�����ռ���,���������������ΪTO
	message.setSubject(mailTitle);//���ñ���
	//�����ż�����
	message.setContent(mailContent, "text/html;charset=gbk"); //����HTML�ʼ���������ʽ�ȽϷḻ
	message.setSentDate(new Date());//���÷���ʱ��
	message.saveChanges();//�洢�ʼ���Ϣ
	
	//�����ʼ�
	Transport transport = session.getTransport();
	transport.connect(user, password);
	transport.sendMessage(message, message.getAllRecipients());//�����ʼ�,���еڶ�����������������õ��ռ��˵�ַ
	transport.close();
}
}