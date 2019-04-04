package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import client.ClientUI;

public class EnterActionHandler implements ActionListener {
	ClientUI ui;
	
	public EnterActionHandler(ClientUI ui){
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();
		int idx = ui.pnLounge.rbts.indexOf(b);
		ui.net.sendRoomEnterRequest(idx);
	}
}
