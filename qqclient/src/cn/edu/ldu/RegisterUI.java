package cn.edu.ldu;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JPasswordField;

import cn.edu.ldu.security.Cryptography;
import cn.edu.ldu.util.Message;
import cn.edu.ldu.util.Translate;

import java.awt.Color;
import java.awt.GridLayout;


@SuppressWarnings({ "unused", "serial" })
public class RegisterUI extends javax.swing.JFrame{

	private JFrame frame;
	private DatagramSocket RegisterSocket;
	private String remoteName;
	private int remotePort;
	private Message msg;
	private InetAddress remoteAddr;
	private JTextField textName;
	private JTextField textEmail;
	private JLabel LaID;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RegisterUI window = new RegisterUI();
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
	public RegisterUI() {
		initComponents();
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width)/2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height)/2;
        this.setLocation(x, y);
	}
	
	public RegisterUI(InetAddress remoteAddr, int remotePort, Message msg) throws UnknownHostException {
		this();
		this.setTitle("?û?ע??");
		this.remoteAddr = remoteAddr;
		this.remotePort = remotePort;
		this.msg = msg;
	}
	
	@SuppressWarnings("resource")
	protected void btnRegisterActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (passwordField1.getPassword().equals(passwordField2.getPassword())) {
			JOptionPane.showMessageDialog(null, "??????????????\n", "???????룡", JOptionPane.ERROR_MESSAGE);
			passwordField1.setText("");
			passwordField2.setText("");
		}
		try {
			String Name = textName.getText();
			String Password = String.valueOf(passwordField1.getPassword());
			String Email = textEmail.getText();
			
			msg.setType("M_Register");
			msg.setUserId(Name);
			msg.setPassword(Cryptography.getHash(Password, "SHA-256"));
			msg.setText(Email);
			msg.setToAddr(remoteAddr); //Ŀ????ַ
	        msg.setToPort(remotePort); //Ŀ???˿?
	        RegisterSocket = new DatagramSocket();
	        byte[] data=Translate.ObjectToByte(msg); //??Ϣ???????л?
	        DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,remotePort); //??????¼????
	        RegisterSocket.send(packet); //???͵?¼????
	        DatagramPacket backPacket=new DatagramPacket(data,data.length);
	        RegisterSocket.receive(backPacket); 
	        RegisterSocket.setSoTimeout(0);//ȡ????ʱʱ??
	        Message backMsg=(Message)Translate.ByteToObject(data);
	        //??????¼????
	        if (backMsg.getType().equalsIgnoreCase("M_SUCCESS")) { //??¼?ɹ?
	        	LaID.setText(backMsg.getUserId());
	        	JOptionPane.showMessageDialog(null, "?˺ţ?"+backMsg.getUserId()+"\n", "ע???ɹ???",JOptionPane.ERROR_MESSAGE);
	        }else { //??¼ʧ??
	            JOptionPane.showMessageDialog(null, "?û???"+msg.getUserId()+"\n", "ע??ʧ?ܣ?\n"+"??????ע??",JOptionPane.ERROR_MESSAGE);           
	        }
	        
			
		} catch (Exception ex) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, ex.getMessage(), "????", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        LoginUI login = new LoginUI();
        login.setVisible(true);
        this.dispose();
        RegisterSocket.close(); //?ر??׽???
    }//GEN-LAST:event_formWindowClosing
	
	
	private void initComponents() {
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width)/2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height)/2;
        this.setLocation(x, y);
        this.setSize(400,240);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{31, 78, 187, 57, 0};
        gridBagLayout.rowHeights = new int[]{1, 21, 21, 21, 21, 23, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);
        
        LaID = new JLabel("            ");
        GridBagConstraints gbc_LaID = new GridBagConstraints();
        gbc_LaID.anchor = GridBagConstraints.NORTH;
        gbc_LaID.insets = new Insets(0, 0, 5, 5);
        gbc_LaID.gridx = 2;
        gbc_LaID.gridy = 0;
        getContentPane().add(LaID, gbc_LaID);
        
        JLabel LaName = new JLabel("\u6635\u79F0");
        GridBagConstraints gbc_LaName = new GridBagConstraints();
        gbc_LaName.anchor = GridBagConstraints.EAST;
        gbc_LaName.insets = new Insets(0, 0, 5, 5);
        gbc_LaName.gridx = 1;
        gbc_LaName.gridy = 1;
        getContentPane().add(LaName, gbc_LaName);
        
        textName = new JTextField();
        GridBagConstraints gbc_textName = new GridBagConstraints();
        gbc_textName.anchor = GridBagConstraints.NORTH;
        gbc_textName.fill = GridBagConstraints.HORIZONTAL;
        gbc_textName.insets = new Insets(0, 0, 5, 5);
        gbc_textName.gridx = 2;
        gbc_textName.gridy = 1;
        getContentPane().add(textName, gbc_textName);
        textName.setColumns(10);
        
        JLabel Lapassword1 = new JLabel("\u5BC6\u7801");
        GridBagConstraints gbc_Lapassword1 = new GridBagConstraints();
        gbc_Lapassword1.anchor = GridBagConstraints.EAST;
        gbc_Lapassword1.insets = new Insets(0, 0, 5, 5);
        gbc_Lapassword1.gridx = 1;
        gbc_Lapassword1.gridy = 2;
        getContentPane().add(Lapassword1, gbc_Lapassword1);
        
        passwordField1 = new JPasswordField();
        GridBagConstraints gbc_passwordField1 = new GridBagConstraints();
        gbc_passwordField1.anchor = GridBagConstraints.NORTH;
        gbc_passwordField1.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField1.insets = new Insets(0, 0, 5, 5);
        gbc_passwordField1.gridx = 2;
        gbc_passwordField1.gridy = 2;
        getContentPane().add(passwordField1, gbc_passwordField1);
        
        JLabel lLaPassword2 = new JLabel(" \u91CD\u65B0\u8F93\u5165\u5BC6\u7801");
        GridBagConstraints gbc_lLaPassword2 = new GridBagConstraints();
        gbc_lLaPassword2.anchor = GridBagConstraints.EAST;
        gbc_lLaPassword2.insets = new Insets(0, 0, 5, 5);
        gbc_lLaPassword2.gridx = 1;
        gbc_lLaPassword2.gridy = 3;
        getContentPane().add(lLaPassword2, gbc_lLaPassword2);
        
        passwordField2 = new JPasswordField();
        GridBagConstraints gbc_passwordField2 = new GridBagConstraints();
        gbc_passwordField2.anchor = GridBagConstraints.NORTH;
        gbc_passwordField2.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField2.insets = new Insets(0, 0, 5, 5);
        gbc_passwordField2.gridx = 2;
        gbc_passwordField2.gridy = 3;
        getContentPane().add(passwordField2, gbc_passwordField2);
        
        JLabel LaEmail = new JLabel("Email");
        GridBagConstraints gbc_LaEmail = new GridBagConstraints();
        gbc_LaEmail.anchor = GridBagConstraints.EAST;
        gbc_LaEmail.insets = new Insets(0, 0, 5, 5);
        gbc_LaEmail.gridx = 1;
        gbc_LaEmail.gridy = 4;
        getContentPane().add(LaEmail, gbc_LaEmail);
        
        textEmail = new JTextField();
        GridBagConstraints gbc_textEmail = new GridBagConstraints();
        gbc_textEmail.anchor = GridBagConstraints.NORTH;
        gbc_textEmail.fill = GridBagConstraints.HORIZONTAL;
        gbc_textEmail.insets = new Insets(0, 0, 5, 5);
        gbc_textEmail.gridx = 2;
        gbc_textEmail.gridy = 4;
        getContentPane().add(textEmail, gbc_textEmail);
        textEmail.setColumns(10);
        
        JButton btnRegister = new JButton("\u6CE8\u518C");
        btnRegister.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		btnRegisterActionPerformed(e);
        	}
        });
        GridBagConstraints gbc_btnRegister = new GridBagConstraints();
        gbc_btnRegister.anchor = GridBagConstraints.NORTHWEST;
        gbc_btnRegister.gridx = 3;
        gbc_btnRegister.gridy = 5;
        getContentPane().add(btnRegister, gbc_btnRegister);
	}

}
