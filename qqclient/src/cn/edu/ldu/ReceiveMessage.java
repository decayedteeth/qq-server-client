package cn.edu.ldu;

import cn.edu.ldu.util.Message;
import cn.edu.ldu.util.Translate;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URL;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * ����ReceiveMessage�ͻ���������Ϣ�ʹ�����Ϣ���߳���
 * @author  ����־����Ȩ����2016--2018��upsunny2008@163.com
 */
public class ReceiveMessage extends Thread{
    private DatagramSocket clientSocket; //�Ự�׽���
    private ClientUI parentUI; //����
    private byte[] data=new byte[8096]; //8K�ֽ�����
    private DefaultListModel listModel=new DefaultListModel(); //�б�Model
    //���캯��
    public ReceiveMessage(DatagramSocket socket,ClientUI parentUI) {
        clientSocket=socket; //�Ự�׽���
        this.parentUI=parentUI; //����
    }   
    @Override
    public void run() {
        while (true) { //����ѭ���������յ��ĸ�����Ϣ
          try {
            DatagramPacket packet=new DatagramPacket(data,data.length); //�������ձ���
            clientSocket.receive(packet); //����           
            Message msg=(Message)Translate.ByteToObject(data);//��ԭ��Ϣ����
            String userId=msg.getUserId(); //��ǰ�û�id
            //������Ϣ���ͷ��ദ��
            if (msg.getType().equalsIgnoreCase("M_LOGIN")) { //�������û��ĵ�¼��Ϣ
                playSound("/cn/edu/ldu/sound/fadeIn.wav");//������ʾ��  
                //������Ϣ����
                parentUI.txtArea.append(userId+" ����ͦ�ؽ���������...\n");
                //�������û������б�
                listModel.add(listModel.getSize(), userId);
                parentUI.userList.setModel(listModel);
            }else if (msg.getType().equalsIgnoreCase("M_ACK")) { //�Ƿ�����ȷ����Ϣ
                //��¼�ɹ������Լ������û��б�
                listModel.add(listModel.getSize(), userId);
                parentUI.userList.setModel(listModel);
            }else if (msg.getType().equalsIgnoreCase("M_MSG")) { //����ͨ�Ự��Ϣ
                playSound("/cn/edu/ldu/sound/msg.wav");//��Ϣ��ʾ��  
                //������Ϣ����
                parentUI.txtArea.append(userId+" ˵��"+msg.getText()+"\n");
            }else if (msg.getType().equalsIgnoreCase("M_QUIT")) { //�������û�������Ϣ
                playSound("/cn/edu/ldu/sound/leave.wav");//��Ϣ��ʾ��  
                //������Ϣ����
                parentUI.txtArea.append(userId+" �������뿪������...\n");
                //�����û����б�ɾ��
                listModel.remove(listModel.indexOf(userId));
                parentUI.userList.setModel(listModel);
            }//end if  
          }catch (Exception ex) {
              JOptionPane.showMessageDialog(null, ex.getMessage(),"������ʾ",JOptionPane.ERROR_MESSAGE);
          }//end try
        } //end while
    }//end run
    /**
     * ���������ļ�
     * @param filename �����ļ�·��������
     */
    private void playSound(String filename) {
        URL url = AudioClip.class.getResource(filename);
        AudioClip sound;
        sound = Applet.newAudioClip(url);
        sound.play();
    }
}//end class