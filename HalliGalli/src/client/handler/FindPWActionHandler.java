package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class FindPWActionHandler implements ActionListener {
	ClientUI ui;
	
	public FindPWActionHandler(ClientUI clientUI) {
		ui = clientUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String nick = ui.pnFind.tfCPnick.getText();
		String email2 = ui.pnFind.tfCPEmail2.getText();
	
		if(nick.equals("") || email2.equals("")) {
			JOptionPane.showMessageDialog(ui, "ID/Email을 입력해주세요.");
			return;
		}
			ui.net.sendFindPWRequest(nick, email2);
	}
	
}