package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class AuthActionHandler implements ActionListener {
	ClientUI ui;

	public AuthActionHandler(ClientUI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		String nick = ui.pnWelcome.tfAuthNick.getText();
		String pass = ui.pnWelcome.pfAuthPass.getText();
		if(nick.equals("") || pass.equals("")) {
			JOptionPane.showMessageDialog(ui, "���̵�� ��й�ȣ�� �Է��Ѵ�.");
			return;
		}

		ui.net.sendAuthRequest(nick, pass);

	}
}
