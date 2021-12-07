package cn.edu.ldu.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Translate����������л��ͷ����л�
 * @author ����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class Translate {
    /**
     * ����ת��Ϊ�ֽ�������ʽ��ʵ�ֶ������л�
     * @param obj ��ת���Ķ���
     * @return �ֽ�����
     */
    public static byte[] ObjectToByte(Object obj) {
       byte[] buffer=null;
        try {
            ByteArrayOutputStream bo=new ByteArrayOutputStream(); //�ֽ����������
            ObjectOutputStream oo=new ObjectOutputStream(bo); //���������
            oo.writeObject(obj); //�������
            buffer=bo.toByteArray(); //�������л�
        }catch(IOException ex) {}
        return buffer;
    } 
    
    /**
     * �ֽ�����ת��ΪObject������ʽ��ʵ�ֶ������л�
     * @param buffer �ֽ�����
     * @return Object����
     */
    public static Object ByteToObject(byte[] buffer) {
        Object obj=null;
        try {
            ByteArrayInputStream bi=new ByteArrayInputStream(buffer); //�ֽ�����������
            ObjectInputStream oi=new ObjectInputStream(bi); //����������
            obj=oi.readObject(); //תΪ����
        }catch(IOException | ClassNotFoundException ex) { }
        return obj;
    } 
}