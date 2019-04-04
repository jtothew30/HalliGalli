package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class RoomChatActionHandler implements ActionListener {
	ClientUI ui;
	
	public RoomChatActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String chat = ui.pnRoom.tfChat.getText();
		
		if(chat.contains("#")) {
			JOptionPane.showMessageDialog(ui, "#은 사용할 수 없어여");
		}else {
			ui.net.sendRoomChatRequest(chat);
		}
		ui.pnRoom.tfChat.setText("");
	}

}
