package client.panel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

public class JoinPanel extends JPanel {
	public JTextField tfJoinNick;
	public JPasswordField pfJoinPass;
	public JTextField tfJoinemail;
	public JButton btnjoin;
	public JButton btCancel;
	public JRadioButton rbtAgree;
	
	public JoinPanel() {
		
		setSize(500, 400);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 500, 400);
		add(panel);
		panel.setLayout(null);
		
		JLabel lbtmp = new JLabel("\u3014\uD68C\uC6D0 \uAC00\uC785\u3015");
		lbtmp.setForeground(new Color(0, 0, 0));
		lbtmp.setHorizontalAlignment(SwingConstants.CENTER);
		lbtmp.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 24));
		lbtmp.setBounds(25, 7, 447, 45);
		panel.add(lbtmp);
		
		JLabel lbtmp2 = new JLabel("\uC544\uC774\uB514");
		lbtmp2.setForeground(new Color(0, 0, 0));
		lbtmp2.setHorizontalAlignment(SwingConstants.CENTER);
		lbtmp2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		lbtmp2.setBounds(152, 229, 57, 15);
		panel.add(lbtmp2);
		
		JLabel lbtmp3 = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbtmp3.setForeground(new Color(0, 0, 0));
		lbtmp3.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		lbtmp3.setHorizontalAlignment(SwingConstants.CENTER);
		lbtmp3.setBounds(152, 254, 57, 15);
		panel.add(lbtmp3);
		
		JLabel lbtmp4 = new JLabel("Email \uC8FC\uC18C");
		lbtmp4.setForeground(new Color(0, 0, 0));
		lbtmp4.setHorizontalAlignment(SwingConstants.CENTER);
		lbtmp4.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		lbtmp4.setBounds(139, 279, 70, 15);
		panel.add(lbtmp4);
		
		JLabel lbtmp5 = new JLabel("\uC11C\uBE44\uC2A4 \uC774\uC6A9\uC57D\uAD00\uC5D0 \uB3D9\uC758\uD558\uC2ED\uB2C8\uAE4C?");
		lbtmp5.setForeground(new Color(0, 0, 0));
		lbtmp5.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 13));
		lbtmp5.setHorizontalAlignment(SwingConstants.CENTER);
		lbtmp5.setBounds(70, 194, 217, 21);
		panel.add(lbtmp5);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 53, 447, 135);
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		textArea.setText("\uC11C\uBE44\uC2A4 \uC774\uC6A9\uC57D\uAD00\r\n\r\n\uC81C1\uC870 (\uC774\uC6A9\uACC4\uC57D\uC758 \uC131\uB9BD)\r\n\uC774\uC6A9\uACC4\uC57D\uC740 \uC774\uC6A9\uC790\uC758 \uC57D\uAD00\uB0B4\uC6A9\uC5D0 \uB300\uD55C \uB3D9\uC758\uC640 \uC774\uC6A9\uC790\uC758 \uC774\uC6A9\uC2E0\uCCAD\uC5D0 \uB300\uD55C  \uC2B9\uB099\uC73C\uB85C \uC131\uB9BD\uD569\uB2C8\uB2E4.\r\n\r\n\uC81C2\uC870 (\uC774\uC6A9\uC2E0\uCCAD)\r\n\uC774\uC6A9\uC2E0\uCCAD\uC740 \uC11C\uBE44\uC2A4\uC758 \uD68C\uC6D0\uC815\uBCF4 \uD654\uBA74\uC5D0\uC11C \uC774\uC6A9\uC790\uAC00  \uC694\uAD6C\uD558\uB294 \uAC00\uC785\uC2E0\uCCAD \uC591\uC2DD\uC5D0 \uAC1C\uC778\uC758 \uC2E0\uC0C1\uC815\uBCF4\uB97C \uAE30\uB85D\uD558\uB294 \uBC29\uC2DD\uC73C\uB85C \uC2E0\uCCAD\uD569\uB2C8\uB2E4.\r\n");
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		scrollPane.setViewportView(textArea);
		
		tfJoinNick = new JTextField();
		tfJoinNick.setBackground(new Color(255, 255, 255));
		tfJoinNick.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		tfJoinNick.setBounds(221, 226, 116, 21);
		panel.add(tfJoinNick);
		tfJoinNick.setColumns(10);
		
		pfJoinPass = new JPasswordField();
		pfJoinPass.setBackground(new Color(255, 255, 255));
		pfJoinPass.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		pfJoinPass.setBounds(221, 251, 116, 21);
		panel.add(pfJoinPass);
		
		tfJoinemail = new JTextField();
		tfJoinemail.setBackground(new Color(255, 255, 255));
		tfJoinemail.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		tfJoinemail.setColumns(10);
		tfJoinemail.setBounds(221, 275, 116, 21);
		panel.add(tfJoinemail);
		
		btnjoin = new JButton("");
		btnjoin.setIcon(new ImageIcon(ClassLoader.getSystemResource("image/button/btnjoin.png")));
		btnjoin.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
		btnjoin.setBounds(160, 315, 85, 50);
		panel.add(btnjoin);
		
		btCancel = new JButton("");
		btCancel.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 14));
		btCancel.setBounds(270, 315, 85, 50);
		btCancel.setIcon(new ImageIcon(ClassLoader.getSystemResource("image/button/back3.png")));
		panel.add(btCancel);
		
		rbtAgree = new JRadioButton("\uB3D9\uC758\uD569\uB2C8\uB2E4.");
		rbtAgree.setForeground(new Color(0, 0, 0));
		rbtAgree.setBackground(new Color(255, 255, 255));
		rbtAgree.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		rbtAgree.setBounds(290, 194, 91, 20);
		rbtAgree.setOpaque(false);
		panel.add(rbtAgree);
		
		JLabel lbimagee = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/join.png")));
		lbimagee.setForeground(new Color(255, 250, 250));
		lbimagee.setBounds(0, 0, 500, 400);
		panel.add(lbimagee);
		btnjoin.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
	}
}
