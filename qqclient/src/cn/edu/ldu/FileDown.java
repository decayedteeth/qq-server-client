package cn.edu.ldu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.SwingWorker;
import javax.xml.bind.DatatypeConverter;

import cn.edu.ldu.util.Message;

/**
 * RecvFile类，接收文件的后台线程类
 * 作者：董相志，版权所有2016--2018，upsunny2008@163.com
 */
public class FileDown extends SwingWorker<Integer,Object> {
    private SSLSocket fileSocket; //接收文件的套接字
    private Message msg;//消息类
    private ClientUI clientUI; //主窗体类
    private KeyStore tks; //公钥库
    private KeyStore ks; //私钥库    
    private static final int BUFSIZE=8096;//缓冲区大小    
    public FileDown(Message msg,ClientUI parentUI) { 
        this.clientUI=clientUI;
        this.msg = msg;
    }
    @Override
    protected Integer doInBackground() throws Exception {
    	InputStream key =ClientUI.class.getResourceAsStream("/cn/edu/ldu/keystore/client.keystore");//私钥库
        InputStream tkey =ClientUI.class.getResourceAsStream("/cn/edu/ldu/keystore/tclient.keystore");//公钥库
        String CLIENT_KEY_STORE_PASSWORD = "123456"; //client.keystore私钥库密码
        String CLIENT_TRUST_KEY_STORE_PASSWORD = "123456";//tclient.keystore公钥库密码
        SSLContext ctx = SSLContext.getInstance("SSL"); //SSL上下文
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); //私钥管理器
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");//公钥管理器
        KeyStore ks = KeyStore.getInstance("JKS");//私钥库对象
        KeyStore tks = KeyStore.getInstance("JKS");//公钥库对象
        ks.load(key, CLIENT_KEY_STORE_PASSWORD.toCharArray());//加载私钥库
        tks.load(tkey, CLIENT_TRUST_KEY_STORE_PASSWORD.toCharArray());//加载公钥库
        kmf.init(ks, CLIENT_KEY_STORE_PASSWORD.toCharArray());//私钥库访问初始化
        tmf.init(tks);//公钥库访问初始化
        //用私钥库和公钥库初始化SSL上下文
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);  
        //用SSLSocket连接服务器
        fileSocket = (SSLSocket)ctx.getSocketFactory().createSocket(msg.getToAddr(),msg.getToPort());
        //获取客户机私钥
        PrivateKey privateKey=(PrivateKey)ks.getKey("client", CLIENT_KEY_STORE_PASSWORD.toCharArray());
        //获取服务器公钥
        PublicKey publicKey=(PublicKey)tks.getCertificate("server").getPublicKey();    
        //获取套接字输入流
        DataInputStream in=new DataInputStream(
                           new BufferedInputStream(
                           fileSocket.getInputStream()));        
        //1.接收文件名、文件长度
        String filename=in.readUTF(); //文件名
        int fileLen=(int)in.readLong(); //文件长度 
        clientUI.txtArea.append("1.收到文件名："+filename+"文件长度："+fileLen+"字节\n\n");
        //创建文件输出流
        File file=new File("./upload/"+filename);       
        //文件输出流
        BufferedOutputStream fout=new BufferedOutputStream(
                                  new FileOutputStream(file));
        //定义消息摘要算法
        MessageDigest sha256=MessageDigest.getInstance("SHA-256");//256位
        //基于文件输出流和摘要算法构建消息摘要流
        DigestOutputStream out=new DigestOutputStream(fout,sha256);      
        //2.接收文件内容，存储为外部文件
        byte[] buffer=new byte[BUFSIZE]; //读入缓冲区
        int numRead=0; //单次读取的字节数
        int numFinished=0;//总完成字节数
        while (numFinished<fileLen && (numRead=in.read(buffer))!=-1) { //输入流可读      
            out.write(buffer,0,numRead);
            numFinished+=numRead; //已完成字节数
            Thread.sleep(200); //演示文件传输进度用
            publish(numFinished+"/"+fileLen+"bytes");
            setProgress(numFinished*100/(int)fileLen);
        }//end while
        clientUI.txtArea.append("2.接收文件内容结束！\n\n");
        //3.接收加密的数字签名
        int size=in.readInt();
        byte[] signature=new byte[size];
        int i=in.read(signature);
         clientUI.txtArea.append("3.收到加密的数字签名："+DatatypeConverter.printHexBinary(signature)+"\n\n");
         
        //4.接收加密的密钥
        byte[] encryptKey=new byte[128];
        i=in.read(encryptKey);
        clientUI.txtArea.append("4.收到加密的密钥："+DatatypeConverter.printHexBinary(encryptKey)+"\n\n"); 
        
        //用服务器私钥解密密钥
        Cipher cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding");//解密器
        cipher.init(Cipher.DECRYPT_MODE, privateKey); //用服务器私钥初始化解密器
        byte[] decryptKey=cipher.doFinal(encryptKey);//解密密钥
        clientUI.txtArea.append("密钥解密："+DatatypeConverter.printHexBinary(decryptKey)+"\n\n");
        
        //用密钥解密数字签名
        SecretKey secretKey = new SecretKeySpec(decryptKey, "AES");
        Cipher cipher2=Cipher.getInstance("AES");//解密器
        cipher2.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] decryptSign=cipher2.doFinal(signature);//解密数字签名
        clientUI.txtArea.append("签名解密："+DatatypeConverter.printHexBinary(decryptSign)+"\n\n");
        
        //"SHA-256"算法计算的摘要为256位，合32字节
        byte[] sourceDigest=new byte[32]; //收到的摘要       
        cipher.init(Cipher.DECRYPT_MODE, publicKey); //用客户机公钥初始化解密器
        sourceDigest=cipher.doFinal(decryptSign); //还原消息摘要
        clientUI.txtArea.append("去掉签名后的摘要："+DatatypeConverter.printHexBinary(sourceDigest)+"\n\n");        
//更新显示

        //5.根据文件输出流重新计算消息摘要
        byte[] computedDigest=new byte[32];//重新计算的摘要        
        computedDigest=out.getMessageDigest().digest();
        //输出相关提示信息
        clientUI.txtArea.append("服务器根据收到的文件重新计算的摘要："+DatatypeConverter.printHexBinary(computedDigest)+"\n\n");
 
        //定义字符输出流
        PrintWriter pw=new PrintWriter(fileSocket.getOutputStream(),true);
        //比较重新计算的摘要与收到的摘要是否相同
        if (Arrays.equals(sourceDigest,computedDigest)) {//验证数字签名
            pw.println("M_DONE"); //回送成功消息
            clientUI.txtArea.append("5."+filename+"  接收成功！\n\n");
        }else {
            pw.println("M_LOST"); //回送失败消息
            clientUI.txtArea.append("5."+filename+"  接收失败！\n\n");
        }//end if        
        //关闭流
        in.close();
        out.close();
        fout.close();
        pw.close();
        fileSocket.close();
        return 100;
    }//end doInBackground    
}
