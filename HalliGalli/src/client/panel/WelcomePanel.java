package client.panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class WelcomePanel extends JPanel {
	public JTextField tfAuthNick;
	public JPasswordField pfAuthPass;
	public JButton btJoin;
	public JButton btfind;
	
	public WelcomePanel() {
		
		setSize(500, 400);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 500, 400);
		add(panel);
		panel.setLayout(null);	
		
		tfAuthNick = new JTextField();
		tfAuthNick.setBackground(new Color(255, 255, 204));
		tfAuthNick.setBounds(190, 249, 145, 20);
		panel.add(tfAuthNick);
		tfAuthNick.setColumns(10);
		
		pfAuthPass = new JPasswordField();
		pfAuthPass.setBackground(new Color(255, 255, 204));
		pfAuthPass.setBounds(190, 280, 145, 20);
		panel.add(pfAuthPass);
		
		JLabel lbmain3;
		lbmain3 = new JLabel("\uC544\uC774\uB514");
		lbmain3.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
		lbmain3.setHorizontalAlignment(SwingConstants.CENTER);
		lbmain3.setBounds(108, 253, 70, 15);
		panel.add(lbmain3);
		
		JLabel lbmain4 = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbmain4.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
		lbmain4.setHorizontalAlignment(SwingConstants.CENTER);
		lbmain4.setBounds(108, 284, 70, 15);
		panel.add(lbmain4);
		
		JLabel lbmain5 = new JLabel("\uC544\uC9C1 \uACC4\uC815\uC774 \uC5C6\uC73C\uC2E0\uAC00\uC694?");
		lbmain5.setHorizontalAlignment(SwingConstants.CENTER);
		lbmain5.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 11));
		lbmain5.setBounds(78, 319, 144, 15);
		panel.add(lbmain5);
		
		btJoin = new JButton("\uD68C\uC6D0\uAC00\uC785");
		btJoin.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
		btJoin.setBounds(95, 344, 109, 23);
		panel.add(btJoin);
		
		JLabel lbmain6 = new JLabel("ID/PW\uB97C \uC78A\uC5B4\uBC84\uB9AC\uC168\uB098\uC694?");
		lbmain6.setHorizontalAlignment(SwingConstants.CENTER);
		lbmain6.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 11));
		lbmain6.setBounds(273, 319, 155, 15);
		panel.add(lbmain6);
		
		btfind = new JButton("ID/PW \uCC3E\uAE30");
		btfind.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
		btfind.setBounds(296, 344, 109, 23);
		panel.add(btfind);
		
		JLabel lbimage = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/welcome.gif")));
		lbimage.setBounds(0, 0, 500, 400);
		panel.add(lbimage);
	}
}
//
