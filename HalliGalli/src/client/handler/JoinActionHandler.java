package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class JoinActionHandler implements ActionListener {
	ClientUI ui;
	public JoinActionHandler(ClientUI clientUI) {
		ui = clientUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean b = ui.pnJoin.rbtAgree.isSelected();
		if(!b) {
			JOptionPane.showMessageDialog(ui, "��� ���Ǹ� �ش޶�.");
		}else {
			String nick = ui.pnJoin.tfJoinNick.getText();
			String pass = ui.pnJoin.pfJoinPass.getText();
			String email = ui.pnJoin.tfJoinemail.getText();
			if(!nick.matches(".{2,10}")) {
				JOptionPane.showMessageDialog(ui, "���̵�� �ѱ۷� 2 ~ 5 ���ڴ�.");
				return;
			}
			if(!pass.matches("\\w{2,8}")) {
				JOptionPane.showMessageDialog(ui, "��й�ȣ�� 2 ~ 8 ���ڴ�.");
				return;
			}
			if(!email.matches("^[_0-9a-zA-Z-]+@[0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*$")) {
				JOptionPane.showMessageDialog(ui, "�ùٸ� Email�� �����ּ���.");
				return;
			}
			ui.net.sendJoinRequest(nick, pass, email);
		}

	}

}
