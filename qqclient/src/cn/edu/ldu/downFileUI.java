package cn.edu.ldu;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.BoxLayout;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class downFileUI extends javax.swing.JFrame{

	private JFrame frame;
	private JTextField textFileName;
	private TextArea textArea;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					downFileUI window = new downFileUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public downFileUI() {
		initialize();
	}
	public downFileUI(String[] fn) {
		for(int i=fn.length;i<fn.length;i++)
			this.textArea.append(fn[i]+"\n");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 321, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		textArea = new TextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridheight = 5;
		gbc_textArea.gridwidth = 8;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 1;
		frame.getContentPane().add(textArea, gbc_textArea);
		
		JLabel fileName = new JLabel("\u6587\u4EF6\u540D");
		GridBagConstraints gbc_fileName = new GridBagConstraints();
		gbc_fileName.insets = new Insets(0, 0, 0, 5);
		gbc_fileName.anchor = GridBagConstraints.EAST;
		gbc_fileName.gridx = 1;
		gbc_fileName.gridy = 6;
		frame.getContentPane().add(fileName, gbc_fileName);
		
		textFileName = new JTextField();
		GridBagConstraints gbc_textFileName = new GridBagConstraints();
		gbc_textFileName.gridwidth = 6;
		gbc_textFileName.insets = new Insets(0, 0, 0, 5);
		gbc_textFileName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFileName.gridx = 2;
		gbc_textFileName.gridy = 6;
		frame.getContentPane().add(textFileName, gbc_textFileName);
		textFileName.setColumns(10);
		
		JButton btnFiledown = new JButton("\u4E0B\u8F7D");
		btnFiledown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientUI.fName=textFileName.getText();
			}
		});
		GridBagConstraints gbc_btnFiledown = new GridBagConstraints();
		gbc_btnFiledown.gridx = 8;
		gbc_btnFiledown.gridy = 6;
		frame.getContentPane().add(btnFiledown, gbc_btnFiledown);
	}

}
