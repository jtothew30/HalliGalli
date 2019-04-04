package client.panel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;

public class FindPanel extends JPanel {
	public JTextField tfCPEmail;
	public JTextField tfCPEmail2;
	public JTextField tfCPnick;
	public JButton btnAccess;
	public JButton btnAccess2;
	public JButton btnback;
	
	public FindPanel() {
		setSize(500, 400);
		setLayout(null);
		
		JPanel panelmain = new JPanel();
		panelmain.setBounds(0, 0, 500, 400);
		add(panelmain);
		panelmain.setLayout(null);
		
		JLabel lbfind1 = new JLabel("\u3014ID / PW \uCC3E\uAE30\u3015");
		lbfind1.setBounds(26, 10, 447, 45);
		lbfind1.setForeground(new Color(0, 0, 51));
		lbfind1.setHorizontalAlignment(SwingConstants.CENTER);
		lbfind1.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 24));
		panelmain.add(lbfind1);
		
		JPanel pnid = new JPanel();
		pnid.setBounds(26, 65, 447, 130);
		pnid.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pnid.setOpaque(false);
		panelmain.add(pnid);
		pnid.setLayout(null);
		
		JLabel lbfind2 = new JLabel("\uFF3BID \uCC3E\uAE30\uFF3D");
		lbfind2.setForeground(new Color(0, 0, 51));
		lbfind2.setBounds(0, 0, 110, 30);
		pnid.add(lbfind2);
		lbfind2.setHorizontalAlignment(SwingConstants.CENTER);
		lbfind2.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 16));
		
		JLabel lbfind3 = new JLabel("Email \uC8FC\uC18C");
		lbfind3.setForeground(new Color(0, 0, 51));
		lbfind3.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 13));
		lbfind3.setHorizontalAlignment(SwingConstants.CENTER);
		lbfind3.setBounds(25, 58, 85, 21);
		pnid.add(lbfind3);
		
		JLabel lblNewLabel_2 = new JLabel("\uD68C\uC6D0\uAC00\uC785 \uB2F9\uC2DC\uC5D0 \uC785\uB825\uD558\uC2E0 Email \uC8FC\uC18C\uB97C \uC785\uB825\uD574\uC8FC\uC138\uC694.");
		lblNewLabel_2.setForeground(new Color(0, 0, 51));
		lblNewLabel_2.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		lblNewLabel_2.setBounds(76, 35, 310, 15);
		pnid.add(lblNewLabel_2);
		
		tfCPEmail = new JTextField();
		tfCPEmail.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		tfCPEmail.setForeground(new Color(0, 0, 0));
		tfCPEmail.setBackground(new Color(255, 255, 204));
		tfCPEmail.setBounds(108, 57, 220, 21);
		pnid.add(tfCPEmail);
		tfCPEmail.setColumns(10);
		
		btnAccess = new JButton("\uD655\uC778");
		btnAccess.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		btnAccess.setBounds(180, 88, 80, 30);
		pnid.add(btnAccess);
		
		JPanel pnpw = new JPanel();
		pnpw.setBounds(26, 207, 447, 160);
		pnpw.setForeground(new Color(0, 0, 128));
		pnpw.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		pnpw.setOpaque(false);
		panelmain.add(pnpw);
		pnpw.setLayout(null);
		
		JLabel lbfind4 = new JLabel("\uFF3BPW \uCC3E\uAE30\uFF3D");
		lbfind4.setBounds(0, 0, 110, 30);
		lbfind4.setForeground(new Color(0, 0, 51));
		lbfind4.setHorizontalAlignment(SwingConstants.CENTER);
		lbfind4.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 16));
		pnpw.add(lbfind4);
		
		JLabel lbfind5 = new JLabel("\uC544\uC774\uB514");
		lbfind5.setBounds(22, 59, 85, 21);
		lbfind5.setForeground(new Color(0, 0, 51));
		lbfind5.setHorizontalAlignment(SwingConstants.CENTER);
		lbfind5.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 13));
		pnpw.add(lbfind5);
		
		JLabel lbfind6 = new JLabel("Email \uC8FC\uC18C");
		lbfind6.setBounds(22, 90, 85, 21);
		lbfind6.setForeground(new Color(0, 0, 51));
		lbfind6.setHorizontalAlignment(SwingConstants.CENTER);
		lbfind6.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 13));
		pnpw.add(lbfind6);
		
		JLabel lbfind7 = new JLabel("ID\uC640 Email\uC8FC\uC18C\uB97C \uB300\uC870\uD6C4 \uBE44\uBC00\uBC88\uD638\uB97C \uC54C\uB824\uB4DC\uB9BD\uB2C8\uB2E4.");
		lbfind7.setBounds(85, 35, 295, 15);
		lbfind7.setForeground(new Color(0, 0, 51));
		lbfind7.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		pnpw.add(lbfind7);
		
		tfCPnick = new JTextField();
		tfCPnick.setBounds(108, 58, 220, 21);
		tfCPnick.setBackground(new Color(255, 255, 204));
		tfCPnick.setColumns(10);
		pnpw.add(tfCPnick);
		
		tfCPEmail2 = new JTextField();
		tfCPEmail2.setBounds(108, 90, 220, 21);
		tfCPEmail2.setBackground(new Color(255, 255, 204));
		tfCPEmail2.setColumns(10);
		pnpw.add(tfCPEmail2);
		
		btnAccess2 = new JButton("\uD655\uC778");
		btnAccess2.setBounds(180, 120, 80, 30);
		btnAccess2.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		pnpw.add(btnAccess2);
		
		btnback = new JButton(new ImageIcon(ClassLoader.getSystemResource("image/button/back2.png")));
		btnback.setBounds(408, 10, 80, 50);
		panelmain.add(btnback);
		
		JLabel lbimage3 = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/find.png")));
		lbimage3.setBounds(0, 0, 500, 400);
		panelmain.add(lbimage3);
		lbimage3.setHorizontalAlignment(SwingConstants.CENTER);
	}
}
