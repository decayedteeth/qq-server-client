package cn.edu.ldu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.DigestOutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLSocket;
import javax.swing.SwingWorker;
import javax.xml.bind.DatatypeConverter;

/**
 * RecvFile�࣬�����ļ��ĺ�̨�߳���
 * ���ߣ�����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class RecvFile extends SwingWorker<Integer,Object> {
    private final SSLSocket fileSocket; //�����ļ����׽���
    private ServerUI parentUI; //��������
    private KeyStore tks; //��Կ��
    private KeyStore ks; //˽Կ��    
    private static final int BUFSIZE=8096;//��������С    
    public RecvFile(SSLSocket fileSocket,ServerUI parentUI,KeyStore tks,KeyStore ks) { 
        this.fileSocket=fileSocket;
        this.parentUI=parentUI;
        this.tks=tks;
        this.ks=ks;        
    }
    @Override
    protected Integer doInBackground() throws Exception {        
        String SERVER_KEY_STORE_PASSWORD = "123456"; //server.keystore˽Կ������
        //��ȡ������˽Կ
        PrivateKey privateKey=(PrivateKey)ks.getKey("server",SERVER_KEY_STORE_PASSWORD.toCharArray());
        //��ȡ�ͻ�����Կ
        PublicKey publicKey=(PublicKey)tks.getCertificate("client").getPublicKey();
        //��ȡ�׽���������
        DataInputStream in=new DataInputStream(
                           new BufferedInputStream(
                           fileSocket.getInputStream()));        
        //1.�����ļ������ļ�����
        String filename=in.readUTF(); //�ļ���
        int fileLen=(int)in.readLong(); //�ļ����� 
        parentUI.txtArea.append("1.�յ��ļ�����"+filename+"�ļ����ȣ�"+fileLen+"�ֽ�\n\n");
        //�����ļ������
        File file=new File("./upload/"+filename);       
        //�ļ������
        BufferedOutputStream fout=new BufferedOutputStream(
                                  new FileOutputStream(file));
        //������ϢժҪ�㷨
        MessageDigest sha256=MessageDigest.getInstance("SHA-256");//256λ
        //�����ļ��������ժҪ�㷨������ϢժҪ��
        DigestOutputStream out=new DigestOutputStream(fout,sha256);      
        //2.�����ļ����ݣ��洢Ϊ�ⲿ�ļ�
        byte[] buffer=new byte[BUFSIZE]; //���뻺����
        int numRead=0; //���ζ�ȡ���ֽ���
        int numFinished=0;//������ֽ���
        while (numFinished<fileLen && (numRead=in.read(buffer))!=-1) { //�������ɶ�      
            out.write(buffer,0,numRead);
            numFinished+=numRead; //������ֽ���
        }//end while
        parentUI.txtArea.append("2.�����ļ����ݽ�����\n\n");
        //3.���ռ��ܵ�����ǩ��
        int size=in.readInt();
        byte[] signature=new byte[size];
        int i=in.read(signature);
         parentUI.txtArea.append("3.�յ����ܵ�����ǩ����"+DatatypeConverter.printHexBinary(signature)+"\n\n");
         
        //4.���ռ��ܵ���Կ
        byte[] encryptKey=new byte[128];
        i=in.read(encryptKey);
        parentUI.txtArea.append("4.�յ����ܵ���Կ��"+DatatypeConverter.printHexBinary(encryptKey)+"\n\n"); 
        
        //�÷�����˽Կ������Կ
        Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");//������
        cipher.init(Cipher.DECRYPT_MODE, privateKey); //�÷�����˽Կ��ʼ��������
        byte[] decryptKey=cipher.doFinal(encryptKey);//������Կ
        parentUI.txtArea.append("��Կ���ܣ�"+DatatypeConverter.printHexBinary(decryptKey)+"\n\n");
        
        //����Կ��������ǩ��
        SecretKey secretKey = new SecretKeySpec(decryptKey, "AES");
        Cipher cipher2=Cipher.getInstance("AES");//������
        cipher2.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] decryptSign=cipher2.doFinal(signature);//��������ǩ��
        parentUI.txtArea.append("ǩ�����ܣ�"+DatatypeConverter.printHexBinary(decryptSign)+"\n\n");
        
        //"SHA-256"�㷨�����ժҪΪ256λ����32�ֽ�
        byte[] sourceDigest=new byte[32]; //�յ���ժҪ       
        cipher.init(Cipher.DECRYPT_MODE, publicKey); //�ÿͻ�����Կ��ʼ��������
        sourceDigest=cipher.doFinal(decryptSign); //��ԭ��ϢժҪ
        parentUI.txtArea.append("ȥ��ǩ�����ժҪ��"+DatatypeConverter.printHexBinary(sourceDigest)+"\n\n");        
//������ʾ

        //5.�����ļ���������¼�����ϢժҪ
        byte[] computedDigest=new byte[32];//���¼����ժҪ        
        computedDigest=out.getMessageDigest().digest();
        //��������ʾ��Ϣ
        parentUI.txtArea.append("�����������յ����ļ����¼����ժҪ��"+DatatypeConverter.printHexBinary(computedDigest)+"\n\n");
 
        //�����ַ������
        PrintWriter pw=new PrintWriter(fileSocket.getOutputStream(),true);
        //�Ƚ����¼����ժҪ���յ���ժҪ�Ƿ���ͬ
        if (Arrays.equals(sourceDigest,computedDigest)) {//��֤����ǩ��
            pw.println("M_DONE"); //���ͳɹ���Ϣ
            parentUI.txtArea.append("5."+filename+"  ���ճɹ���\n\n");
        }else {
            pw.println("M_LOST"); //����ʧ����Ϣ
            parentUI.txtArea.append("5."+filename+"  ����ʧ�ܣ�\n\n");
        }//end if        
        //�ر���
        in.close();
        out.close();
        fout.close();
        pw.close();
        fileSocket.close();
        return 100;
    }//end doInBackground    
}