package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class FindIDActionHandler implements ActionListener {
	ClientUI ui;
	
	public FindIDActionHandler(ClientUI clientUI) {
		ui = clientUI;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String email = ui.pnFind.tfCPEmail.getText();
		
		if(email.equals("")) {
			JOptionPane.showMessageDialog(ui, "Email�� �Է����ּ���.");
			return;
		}
			ui.net.sendFindIDRequest(email);
	}
}

