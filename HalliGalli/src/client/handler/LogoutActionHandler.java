package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class LogoutActionHandler implements ActionListener {
	ClientUI ui;
	
	public LogoutActionHandler(ClientUI clientUI) {
		ui = clientUI;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int n = JOptionPane.showConfirmDialog(ui, "�α׾ƿ� �Ͻðڽ��ϱ�?");
		// �� = 0, �ƴϿ� = 1, ��� = 2 , esc = -1
		if(n==-1) {
			
		}else if(n==0) {
			ui.pnWelcome.tfAuthNick.setText("");
			ui.pnWelcome.pfAuthPass.setText("");
			ui.net.sendLogoutRequest();			
		}
	}
}
