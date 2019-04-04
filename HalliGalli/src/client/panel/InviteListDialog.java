package client.panel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class InviteListDialog extends JDialog{
	public JButton btInviteSecond;	
	public JList list;
	public JButton btLeaveSecond;
	
	public InviteListDialog() {
		
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(108, 84, 119, 100);
		getContentPane().add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
		
		btInviteSecond = new JButton("\uBC29 \uCD08 \uB300");
		btInviteSecond.setBounds(108, 194, 119, 43);
		getContentPane().add(btInviteSecond);
		
		btLeaveSecond = new JButton("\uBC29 \uD1F4");
		btLeaveSecond.setBounds(108, 247, 119, 43);
		getContentPane().add(btLeaveSecond);
		
		JLabel lblNewLabel = new JLabel("Login List");
		lblNewLabel.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(108, 10, 119, 33);
		getContentPane().add(lblNewLabel);
		
	}
}
