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
			JOptionPane.showMessageDialog(ui, "약관 동의를 해달라.");
		}else {
			String nick = ui.pnJoin.tfJoinNick.getText();
			String pass = ui.pnJoin.pfJoinPass.getText();
			String email = ui.pnJoin.tfJoinemail.getText();
			if(!nick.matches(".{2,10}")) {
				JOptionPane.showMessageDialog(ui, "아이디는 한글로 2 ~ 5 글자다.");
				return;
			}
			if(!pass.matches("\\w{2,8}")) {
				JOptionPane.showMessageDialog(ui, "비밀번호는 2 ~ 8 글자다.");
				return;
			}
			if(!email.matches("^[_0-9a-zA-Z-]+@[0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*$")) {
				JOptionPane.showMessageDialog(ui, "올바른 Email을 적어주세요.");
				return;
			}
			ui.net.sendJoinRequest(nick, pass, email);
		}

	}

}
