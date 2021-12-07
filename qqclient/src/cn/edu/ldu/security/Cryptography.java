package cn.edu.ldu.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

/**
 * Cryptography�࣬����ʵ��һЩͨ�õļ��ܽ����㷨���������¹��ܺ���
 * getHash�����ַ������ļ���ժҪ����Ҫ����������ܴ���ͽ�������ժҪ��ʽ���������ݿ�
 * generateNewKey������AES�Գ���Կ
 * @author ����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class Cryptography {
    private static final int BUFSIZE=8192;  //��������С

    /**
     * getHash����ϢժҪ�㷨��ʵ�����ļ��ܹ���
     * @param plainText �������ܵ�����
     * @param hashType ���㷨���ͣ�"MD5"��"SHA-1"��"SHA-256"��"SHA-384"��"SHA-512"
     * @return ���ĵ�16�����ַ���
     */
    public static String getHash(String plainText,String hashType) {
        try {
            MessageDigest md=MessageDigest.getInstance(hashType);//�㷨
            byte[] encryptStr=md.digest(plainText.getBytes("UTF-8"));//ժҪ
            return DatatypeConverter.printHexBinary(encryptStr); //16�����ַ���            
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            return null;
        }
    }
    /**
     * ��AES�ԳƼ����㷨������һ���µ���Կ
     * @return ���ɵ���Կ
     */
    public static SecretKey generateNewKey() {
        try {
            //��Կ������
            KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
            keyGenerator.init(128); //128,192,256
            SecretKey secretKey=keyGenerator.generateKey();//����Կ
            return secretKey;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    private Cryptography() {
    }
}//end class