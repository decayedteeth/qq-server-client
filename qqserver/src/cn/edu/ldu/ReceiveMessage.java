package cn.edu.ldu;

import cn.edu.ldu.db.beans.Member;
import cn.edu.ldu.db.tables.MemberManager;
import cn.edu.ldu.security.Cryptography;
import cn.edu.ldu.util.Message;
import cn.edu.ldu.util.Translate;
import cn.edu.ldu.util.User;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

/**
 * ReceiveMessage��������������Ϣ�ʹ�����Ϣ���߳���
 * @author ����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class ReceiveMessage extends Thread {
    private DatagramSocket serverSocket; //�������׽���
    private DatagramPacket packet;  //����
    private List<User> userList=new ArrayList<User>(); //�û��б�
    private byte[] data=new byte[8096]; //8K�ֽ�����
    private ServerUI parentUI; //��Ϣ����  
    
    /**
     * ���캯��
     * @param socket �Ự�׽���
     * @param parentUI ����
     */
    public ReceiveMessage(DatagramSocket socket,ServerUI parentUI) {
        serverSocket=socket;
        this.parentUI=parentUI;
    }
    @Override
    public void run() {  
        while (true) { //ѭ�������յ��ĸ�����Ϣ
            try {
            packet=new DatagramPacket(data,data.length);//�������ձ���
            serverSocket.receive(packet);//���տͻ�������
            //�յ�������תΪ��Ϣ����
            Message msg=(Message)Translate.ByteToObject(packet.getData());
            String userId=msg.getUserId();//��ǰ��Ϣ�����û���id            
            if (msg.getType().equalsIgnoreCase("M_LOGIN")) { //��M_LOGIN��Ϣ 
                Message backMsg=new Message();
                //�ٶ�ֻ��2000��3000��8000�����ʺſ��Ե�¼
                //if (!userId.equals("2000") && !userId.equals("3000") && !userId.equals("8000")) {//��¼���ɹ�
                Member bean=new Member();
                bean.setId(Integer.parseInt(userId));
                bean.setPassword(msg.getPassword());
                if (!MemberManager.userLogin(bean)) {
                    backMsg.setType("M_FAILURE");
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//���¼�û����͵ı���
                    serverSocket.send(backPacket); //����                  
                }else { //��¼�ɹ�
                    backMsg.setType("M_SUCCESS");
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//���¼�û����͵ı���
                    serverSocket.send(backPacket); //����   
                    
                    User user=new User();
                    user.setUserId(userId); //�û���
                    user.setPacket(packet); //�����յ��ı���
                    userList.add(user); //�����û������û��б�
                    
                    //���·����������Ҵ���
                    parentUI.txtArea.append(userId+" ��¼��\n");
                    
                    //���������������û�����M_LOGIN��Ϣ�����µ�¼�߷��������û��б�
                    for (int i=0;i<userList.size();i++) { //���������û��б�                                       
                        //�����������û�����M_LOGIN��Ϣ
                        if (!userId.equalsIgnoreCase(userList.get(i).getUserId())){
                            DatagramPacket oldPacket=userList.get(i).getPacket(); 
                            DatagramPacket newPacket=new DatagramPacket(data,data.length,oldPacket.getAddress(),oldPacket.getPort());//�������û����͵ı���
                            serverSocket.send(newPacket); //����
                        }//end if
                        //��ǰ�û�����M_ACK��Ϣ������i���û����뵱ǰ�û����û��б�
                        Message other=new Message();
                        other.setUserId(userList.get(i).getUserId());
                        other.setType("M_ACK");
                        byte[] buffer=Translate.ObjectToByte(other);
                        DatagramPacket newPacket=new DatagramPacket(buffer,buffer.length,packet.getAddress(),packet.getPort());
                        serverSocket.send(newPacket);
                    }//end for                  
                }//end if                           
            }else if (msg.getType().equalsIgnoreCase("M_MSG")) { //��M_MSG��Ϣ
                //������ʾ
                parentUI.txtArea.append(userId+" ˵��"+msg.getText()+"\n");
                //ת����Ϣ
                for (int i=0;i<userList.size();i++) { //�����û�
                    DatagramPacket oldPacket=userList.get(i).getPacket();
                    DatagramPacket newPacket=new DatagramPacket(data,data.length,oldPacket.getAddress(),oldPacket.getPort()); 
                    serverSocket.send(newPacket); //����
                }
            }else if (msg.getType().equalsIgnoreCase("M_QUIT")) { //��M_QUIT��Ϣ
                //������ʾ
                parentUI.txtArea.append(userId+" ���ߣ�\n");
                //ɾ���û�
                for(int i=0;i<userList.size();i++) {
                    if (userList.get(i).getUserId().equals(userId)) {
                        userList.remove(i);
                        break;
                    }
                }//end for
                //�������û�ת��������Ϣ
                for (int i=0;i<userList.size();i++) {
                    DatagramPacket oldPacket=userList.get(i).getPacket();
                    DatagramPacket newPacket=new DatagramPacket(data,data.length,oldPacket.getAddress(),oldPacket.getPort());
                    serverSocket.send(newPacket);
                }//end for 
            }else if (msg.getType().equalsIgnoreCase("M_Register")) {
            	Message backMsg=new Message();
            	Member bean = new Member();
            	int id = MemberManager.lastUser()+1;
            	
            	bean.setId(id);
            	bean.setName(msg.getUserId());
                bean.setPassword(msg.getPassword());
                bean.setEmail(msg.getText());
                bean.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
                
                if (MemberManager.registerUser(bean)==true) {
                	backMsg.setType("M_SUCCESS");
                	backMsg.setUserId(Integer.toString(id));
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//��ע���û����͵ı���
                    serverSocket.send(backPacket); //���� 
                } else {
                	backMsg.setType("M_FAILURE");
                	backMsg.setUserId(msg.getUserId());
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//��ע���û����͵ı���
                    serverSocket.send(backPacket); //���� 
				}
                
            }else if (msg.getType().equalsIgnoreCase("M_FINDPSW")) {
            	Message backMsg=new Message();
            	Member bean = new Member();
            	bean.setId(Integer.parseInt(userId));
            	bean.setEmail(msg.getText());
            	bean.setPassword(msg.getPassword());
            	if (bean.getEmail()!=null&&MemberManager.QueIdandEmail(bean)) {
            		sendQQEmail.sendMail("2868620071@qq.com", "2868620071@qq.com", "trwtxzxxjgcydhfb", "2374834921@qq.com", "��֤��", "111");
            		backMsg.setType("M_SUCCESS");
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//��ע���û����͵ı���
                    serverSocket.send(backPacket); //���� 
            	}else if (bean.getEmail().equalsIgnoreCase("")&&MemberManager.updatePasswrd(bean)) {
                	backMsg.setType("M_SUCCESS");
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//��ע���û����͵ı���
                    serverSocket.send(backPacket); //���� 
                }else {
                	backMsg.setType("M_FAILURE");
                    byte[] buf=Translate.ObjectToByte(backMsg);
                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//��ע���û����͵ı���
                    serverSocket.send(backPacket); //���� 
				}
            }else if (msg.getType().equalsIgnoreCase("M_File")){
            	if (msg.getFileType().equalsIgnoreCase("send")){
            		ServerUI.ftype="Send";
            	}else if (msg.getFileType().equalsIgnoreCase("down")){
//            		System.out.println("3");
            		if (msg.getText()!=null){
//            			System.out.println(msg.getText());
            			ServerUI.fileName=msg.getText();
            			ServerUI.ftype="Down";
            		} else{
            			Message backMsg=new Message();
//            			File file = new File("./upload");
            			backMsg.setType("M_SUCCESS");
//            			backMsg.setFlielist(file.list());
            			byte[] buf=Translate.ObjectToByte(backMsg);
	                    DatagramPacket backPacket=new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());//��ע���û����͵ı���
	                    serverSocket.send(backPacket); //���� 
//	                    System.out.println("5");
            		}
            	}
            }//end if
            
            } catch (IOException | NumberFormatException ex) {  } catch (SQLException ex) {
                Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }//end while
    }//end run
}//end class