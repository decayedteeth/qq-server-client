package cn.edu.ldu;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.omg.CORBA.FREE_MEM;

import cn.edu.ldu.security.Cryptography;
import cn.edu.ldu.util.Message;
import cn.edu.ldu.util.Translate;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JPasswordField;

@SuppressWarnings({ "serial" })
public class remeber extends javax.swing.JFrame{

	private JFrame frame;
	private JTextField textReID;
	private JTextField textReMail;
	private JButton btnRemeber;
	private JLabel rem;
	private JTextField textRem;
	private JButton btnRem;
	private JLabel label;
	
	private DatagramSocket RemberSocket;
	private Message msg;
	private InetAddress remoteAddr;
	private int remotePort;
	private JLabel LaPassword1;
	private JLabel LaPassword2;
	private JPasswordField password1;
	private JPasswordField password2;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					remeber window = new remeber();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public remeber() {
		initComponents();
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width)/2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height)/2;
        this.setLocation(x, y);
	}
	public remeber(InetAddress remoteAddr, int remotePort, Message msg) throws UnknownHostException {
		this();
		this.setTitle("?һ?????");
		this.remoteAddr = remoteAddr;
		this.remotePort = remotePort;
		this.msg = msg;
	}
	
	protected void btnRemberActionPerformed(ActionEvent e){
		msg.setType("M_FINDPSW");
		msg.setToAddr(remoteAddr); //Ŀ????ַ
        msg.setToPort(remotePort); //Ŀ???˿?
        msg.setUserId(textReID.getText());
        msg.setPassword(Cryptography.getHash(String.valueOf(password1.getPassword()),"sha-256"));
        msg.setText("");
        try {
	        RemberSocket = new DatagramSocket();
	        byte[] data=Translate.ObjectToByte(msg); //??Ϣ???????л?
	        DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,remotePort); //??????¼????
			RemberSocket.send(packet); //???͵?¼????
	        DatagramPacket backPacket=new DatagramPacket(data,data.length);
	        RemberSocket.receive(backPacket); 
	        RemberSocket.setSoTimeout(0);//ȡ????ʱʱ??
	        Message backMsg=(Message)Translate.ByteToObject(data);
	      //?????޸Ľ???
	        if (backMsg.getType().equalsIgnoreCase("M_SUCCESS")) { //??¼?ɹ?
	        	btnRem.setText("??ȡ??֤??");
	        	JOptionPane.showMessageDialog(null, "?޸ĳɹ?"+"\n", "",JOptionPane.ERROR_MESSAGE);
	        }else { //?޸?ʧ??
	            JOptionPane.showMessageDialog(null, "?˺Ż?Ԥ?????䲻????"+"\n", "?????ԣ?",JOptionPane.ERROR_MESSAGE);           
	        }
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        LoginUI loging = new LoginUI();
        loging.setVisible(true);
        this.dispose();
        RemberSocket.close();
	}
	
	protected void btnRemActionPerformed(ActionEvent e) {
		if (password1.getPassword().equals(password2.getPassword())) {
			JOptionPane.showMessageDialog(null, "??????????????\n", "???????룡", JOptionPane.ERROR_MESSAGE);
			password1.setText("");
			password2.setText("");
		}
		msg.setType("M_FINDPSW");
		msg.setToAddr(remoteAddr); //Ŀ????ַ
        msg.setToPort(remotePort); //Ŀ???˿?
        msg.setUserId(textReID.getText());
        msg.setText(textReMail.getText());
        try {
	        RemberSocket = new DatagramSocket();
	        byte[] data=Translate.ObjectToByte(msg); //??Ϣ???????л?
	        DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,remotePort); //??????¼????
			RemberSocket.send(packet); //???͵?¼????
	        DatagramPacket backPacket=new DatagramPacket(data,data.length);
	        RemberSocket.receive(backPacket); 
	        RemberSocket.setSoTimeout(0);//ȡ????ʱʱ??
	        Message backMsg=(Message)Translate.ByteToObject(data);
	      //?????޸Ľ???
	        if (backMsg.getType().equalsIgnoreCase("M_SUCCESS")) { //??¼?ɹ?
	        	btnRem.setText("?ѷ?????֤??");
	        }else { //?޸?ʧ??
	        	btnRem.setText("??ȡ??֤??");
	            JOptionPane.showMessageDialog(null, "?˺Ż?Ԥ?????䲻????"+"\n", "?????ԣ?",JOptionPane.ERROR_MESSAGE);           
	        }
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        LoginUI loging = new LoginUI();
        loging.setVisible(true);
        this.dispose();
        RemberSocket.close(); //?ر??׽???
    }//GEN-LAST:event_formWindowClosing

	/**
	 * Initialize the contents of the frame.
	 */
	private void initComponents() {
		frame = new JFrame();
		frame.setBounds(100, 100, 409, 331);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width)/2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height)/2;
        this.setLocation(x, y);
        this.setSize(400,150);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridwidth = 2;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 3;
		gbc_label.gridy = 0;
		frame.getContentPane().add(label, gbc_label);
		JLabel reID = new JLabel("\u627E\u56DE\u8D26\u53F7");
		GridBagConstraints gbc_reID = new GridBagConstraints();
		gbc_reID.anchor = GridBagConstraints.EAST;
		gbc_reID.insets = new Insets(0, 0, 5, 5);
		gbc_reID.gridx = 1;
		gbc_reID.gridy = 1;
		frame.getContentPane().add(reID, gbc_reID);
		
		textReID = new JTextField();
		GridBagConstraints gbc_textReID = new GridBagConstraints();
		gbc_textReID.gridwidth = 3;
		gbc_textReID.insets = new Insets(0, 0, 5, 5);
		gbc_textReID.fill = GridBagConstraints.HORIZONTAL;
		gbc_textReID.gridx = 2;
		gbc_textReID.gridy = 1;
		frame.getContentPane().add(textReID, gbc_textReID);
		textReID.setColumns(10);
		
		btnRemeber = new JButton("\u627E\u56DE\u5BC6\u7801");
		btnRemeber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemberActionPerformed(e);
			}
		});
		
		LaPassword1 = new JLabel("\u65B0\u7684\u5BC6\u7801");
		GridBagConstraints gbc_LaPassword1 = new GridBagConstraints();
		gbc_LaPassword1.anchor = GridBagConstraints.EAST;
		gbc_LaPassword1.insets = new Insets(0, 0, 5, 5);
		gbc_LaPassword1.gridx = 1;
		gbc_LaPassword1.gridy = 3;
		frame.getContentPane().add(LaPassword1, gbc_LaPassword1);
		
		password1 = new JPasswordField();
		GridBagConstraints gbc_password1 = new GridBagConstraints();
		gbc_password1.gridwidth = 3;
		gbc_password1.insets = new Insets(0, 0, 5, 5);
		gbc_password1.fill = GridBagConstraints.HORIZONTAL;
		gbc_password1.gridx = 2;
		gbc_password1.gridy = 3;
		frame.getContentPane().add(password1, gbc_password1);
		
		LaPassword2 = new JLabel("\u518D\u6B21\u8F93\u5165\u5BC6\u7801");
		GridBagConstraints gbc_LaPassword2 = new GridBagConstraints();
		gbc_LaPassword2.anchor = GridBagConstraints.EAST;
		gbc_LaPassword2.insets = new Insets(0, 0, 5, 5);
		gbc_LaPassword2.gridx = 1;
		gbc_LaPassword2.gridy = 5;
		frame.getContentPane().add(LaPassword2, gbc_LaPassword2);
		
		password2 = new JPasswordField();
		GridBagConstraints gbc_password2 = new GridBagConstraints();
		gbc_password2.gridwidth = 3;
		gbc_password2.insets = new Insets(0, 0, 5, 5);
		gbc_password2.fill = GridBagConstraints.HORIZONTAL;
		gbc_password2.gridx = 2;
		gbc_password2.gridy = 5;
		frame.getContentPane().add(password2, gbc_password2);
		
		JLabel reEmail = new JLabel("\u9884\u7559\u90AE\u7BB1");
		GridBagConstraints gbc_reEmail = new GridBagConstraints();
		gbc_reEmail.anchor = GridBagConstraints.EAST;
		gbc_reEmail.insets = new Insets(0, 0, 5, 5);
		gbc_reEmail.gridx = 1;
		gbc_reEmail.gridy = 7;
		frame.getContentPane().add(reEmail, gbc_reEmail);
		
		textReMail = new JTextField();
		GridBagConstraints gbc_textReMail = new GridBagConstraints();
		gbc_textReMail.gridwidth = 3;
		gbc_textReMail.insets = new Insets(0, 0, 5, 5);
		gbc_textReMail.fill = GridBagConstraints.HORIZONTAL;
		gbc_textReMail.gridx = 2;
		gbc_textReMail.gridy = 7;
		frame.getContentPane().add(textReMail, gbc_textReMail);
		textReMail.setColumns(10);
		
		btnRem = new JButton("\u83B7\u53D6\u9A8C\u8BC1\u7801");
		btnRem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRemActionPerformed(e);
			}
		});
		GridBagConstraints gbc_btnRem = new GridBagConstraints();
		gbc_btnRem.insets = new Insets(0, 0, 5, 0);
		gbc_btnRem.gridx = 5;
		gbc_btnRem.gridy = 7;
		frame.getContentPane().add(btnRem, gbc_btnRem);
		
		rem = new JLabel("\u9A8C\u8BC1\u7801");
		GridBagConstraints gbc_rem = new GridBagConstraints();
		gbc_rem.anchor = GridBagConstraints.EAST;
		gbc_rem.insets = new Insets(0, 0, 5, 5);
		gbc_rem.gridx = 1;
		gbc_rem.gridy = 9;
		frame.getContentPane().add(rem, gbc_rem);
		
		textRem = new JTextField();
		GridBagConstraints gbc_textRem = new GridBagConstraints();
		gbc_textRem.gridwidth = 3;
		gbc_textRem.insets = new Insets(0, 0, 5, 5);
		gbc_textRem.fill = GridBagConstraints.HORIZONTAL;
		gbc_textRem.gridx = 2;
		gbc_textRem.gridy = 9;
		frame.getContentPane().add(textRem, gbc_textRem);
		textRem.setColumns(10);
		GridBagConstraints gbc_btnRemeber = new GridBagConstraints();
		gbc_btnRemeber.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemeber.gridx = 4;
		gbc_btnRemeber.gridy = 10;
		frame.getContentPane().add(btnRemeber, gbc_btnRemeber);
		
		frame.setVisible(true);
	}

}
