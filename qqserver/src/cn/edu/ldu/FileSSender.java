package cn.edu.ldu;
import cn.edu.ldu.security.Cryptography;
import cn.edu.ldu.util.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.SwingWorker;
import javax.xml.bind.DatatypeConverter;

/**
 * FileSender�࣬�����ļ��ĺ�̨�߳���
 * @author ����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class FileSSender extends SwingWorker<List<String>,String>{
    private File file; //�ļ�
    private Message msg;//��Ϣ��
    private ServerUI parentUI; //����
    private SSLSocket fileSocket; //�����ļ����׽���
    private KeyStore tks; //��Կ��
    private KeyStore ks; //˽Կ��
    private static final int BUFSIZE=8096; //��������С
    private int progress=0; //�ļ����ͽ���
    private String lastResults=null; //���ͽ��
    //���캯��
    public FileSSender(SSLSocket fileSocket,File file,ServerUI parentUI,KeyStore tks,KeyStore ks) {
    	this.fileSocket = fileSocket;
        this.file=file;
        this.msg=msg;
        this.parentUI= parentUI;
        this.tks=tks;
        this.ks=ks;
    }
    @Override
    protected List<String> doInBackground() throws Exception {  
        //�����׽��������
        DataOutputStream out=new DataOutputStream(
                             new BufferedOutputStream(
                             fileSocket.getOutputStream()));
        //�����ļ�������
        DataInputStream din=new DataInputStream(
                           new BufferedInputStream(
                           new FileInputStream(file)));
        String SERVER_KEY_STORE_PASSWORD = "123456"; //server.keystore˽Կ������
        //��ȡ������˽Կ
        PrivateKey privateKey=(PrivateKey)ks.getKey("server",SERVER_KEY_STORE_PASSWORD.toCharArray());
        //��ȡ�ͻ�����Կ
        PublicKey publicKey=(PublicKey)tks.getCertificate("client").getPublicKey();        
        //����ժҪ�㷨
        MessageDigest sha256=MessageDigest.getInstance("SHA-256");//256λ         
        //������������ժҪ�㷨������ϢժҪ��
        DigestInputStream in=new DigestInputStream(din,sha256);
        
        long fileLen=file.length();  //�����ļ�����
        //1.�����ļ����ơ��ļ�����
        out.writeUTF(file.getName());
        out.writeLong(fileLen);
        out.flush();
        parentUI.txtArea.append("1.�����ļ����ơ��ļ����ȳɹ���\n");
        //2.�����ļ�����
        int numRead=0; //���ζ�ȡ���ֽ���
        int numFinished=0; //������ֽ���
        byte[] buffer=new byte[BUFSIZE];   
        while (numFinished<fileLen && (numRead=in.read(buffer))!=-1) { //�ļ��ɶ�  
            out.write(buffer,0,numRead);  //����
            out.flush();
            numFinished+=numRead; //������ֽ���
            Thread.sleep(200); //��ʾ�ļ����������
            publish(numFinished+"/"+fileLen+"bytes");
            setProgress(numFinished*100/(int)fileLen);             
        }//end while
        in.close();
        din.close();
        parentUI.txtArea.append("2.�����ļ����ݳɹ���\n");
        byte[] fileDigest=in.getMessageDigest().digest(); //�����ļ�ժҪ
        parentUI.txtArea.append("���ɵ�ժҪ��"+DatatypeConverter.printHexBinary(fileDigest)+"\n\n"); 
        //��˽Կ��ժҪ���ܣ��γ��ļ�������ǩ��
        Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding"); //������
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);//�ø���˽Կ��ʼ������ģʽ
        byte[] signature=cipher.doFinal(fileDigest);//��������ǩ�� 
        
        //������ʾ
        parentUI.txtArea.append("���ɵ�����ǩ����"+DatatypeConverter.printHexBinary(signature)+"\n\n"); 
        
        //����AES�Գ���Կ
        SecretKey secretKey=Cryptography.generateNewKey();
        parentUI.txtArea.append("���ɵ���Կ��"+DatatypeConverter.printHexBinary(secretKey.getEncoded())+"\n\n"); 
        
        //������ǩ������
        Cipher cipher2=Cipher.getInstance("AES");
        cipher2.init(Cipher.ENCRYPT_MODE, secretKey);//��ʼ��������
        byte[] encryptSign=cipher2.doFinal(signature);//���ɼ���ǩ��
        parentUI.txtArea.append("����Կ���ܺ������ǩ����"+DatatypeConverter.printHexBinary(encryptSign)+"\n\n");         
        //����Կ����
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);//�÷�������Կ��ʼ������ģʽ
        byte[] encryptKey=cipher.doFinal(secretKey.getEncoded());//������Կ 
        parentUI.txtArea.append("����Կ���ܣ�"+DatatypeConverter.printHexBinary(encryptKey)+"\n\n");

        //3.���ͼ��ܺ������ǩ��
        out.writeInt(encryptSign.length);
        out.flush();
        out.write(encryptSign);
        out.flush();
        parentUI.txtArea.append("3.���ͼ��ܵ�����ǩ���ɹ���\n");
        
        //4.���ͼ�����Կ
        out.write(encryptKey);//���ĳ���Ϊ128�ֽ�
        out.flush();
        parentUI.txtArea.append("4.���ͼ��ܵ���Կ�ɹ���\n");
        
        //5.���շ�����������Ϣ
        BufferedReader br=new BufferedReader(
                          new InputStreamReader(
                          fileSocket.getInputStream()));
        String response=br.readLine();//��ȡ���ش�        
        if (response.equalsIgnoreCase("M_DONE")) { //�������ɹ�����               
            lastResults= "5."+ file.getName() +"  �������ɹ����գ�\n" ;
        }else if (response.equalsIgnoreCase("M_LOST")){ //����������ʧ��
            lastResults= "5."+ file.getName() +"  ����������ʧ�ܣ�\n" ;
        }//end if
        //�ر���
        br.close();
        out.close();
        fileSocket.close();
        return null;
    } //doInBackground 
}