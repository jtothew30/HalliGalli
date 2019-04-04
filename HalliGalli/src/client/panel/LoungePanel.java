package client.panel;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoungePanel extends JPanel {
	public JTextField tfChat;
	public JTextArea taLog;
	public JButton btExit;
	public JList liUser;
	public JButton btnLogout;
	
	public List<JButton> rbts;	// JButton °´Ã¼ÀúÀå ListÇü ÄÃ·º¼Ç ¸¸µé°í. 
	
	public LoungePanel() {
		setSize(700, 550);
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 358, 469, 146);
		add(scrollPane);
		
		taLog = new JTextArea();
		taLog.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		taLog.setLineWrap(true);
		taLog.setEditable(false);
		scrollPane.setViewportView(taLog);
		
		tfChat = new JTextField();
		tfChat.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		tfChat.setBounds(12, 514, 469, 21);
		add(tfChat);
		tfChat.setColumns(10);
		
		btExit = new JButton("");
		btExit.setIcon(new ImageIcon(ClassLoader.getSystemResource("image/button/exit.png")));
		btExit.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 16));
		btExit.setBounds(493, 485, 195, 50);
		add(btExit);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(493, 49, 195, 385);
		add(scrollPane_1);
		
		liUser = new JList();
		liUser.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		scrollPane_1.setViewportView(liUser);
		
		JLabel lbNew1 = new JLabel("\u3014\uC811\uC18D\uC790\u3015");
		lbNew1.setHorizontalAlignment(SwingConstants.LEFT);
		lbNew1.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 15));
		lbNew1.setBounds(493, 24, 195, 15);
		add(lbNew1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 49, 469, 298);
		add(scrollPane_2);
		
		JPanel panel = new JPanel();
		scrollPane_2.setViewportView(panel);
		panel.setLayout(new GridLayout(5, 2, 0, 3));
		//================================================================
		rbts = new ArrayList<>();
		for(int cnt=1; cnt<=10; cnt++) {
			JButton bt = new JButton();
			bt.setHorizontalAlignment(SwingConstants.LEADING);
			bt.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
			panel.add(bt);
			rbts.add(bt);
		}
		//================================================================
		
		btnLogout = new JButton("");
		btnLogout.setIcon(new ImageIcon(ClassLoader.getSystemResource("image/button/logout.png")));
		btnLogout.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));
		btnLogout.setBounds(493, 444, 195, 31);
		add(btnLogout);
		
			
		JLabel label = new JLabel("\u3014\uAC8C\uC784\uBC29\u3015");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 15));
		label.setBounds(12, 24, 195, 15);
		add(label);
		
		JLabel lblimage = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/lounge.png")));
		lblimage.setBounds(0, 0, 700, 565);
		add(lblimage);
		
		
		
	}
}
