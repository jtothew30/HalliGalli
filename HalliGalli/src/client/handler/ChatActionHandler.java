package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.ClientUI;

public class ChatActionHandler implements ActionListener {
	ClientUI ui;
	
	public ChatActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String chat = ui.pnLounge.tfChat.getText();
		
		JTextField tf = (JTextField)e.getSource();
		System.out.println(tf==ui.pnLounge.tfChat);
		if(chat.contains("#")) {
			JOptionPane.showMessageDialog(ui, "#은 사용할 수 없어여");
		}else {
			ui.net.sendChatRequest(chat);
		}
		
	}

}
