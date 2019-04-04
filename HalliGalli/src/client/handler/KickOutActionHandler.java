package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

public class KickOutActionHandler implements ActionListener {
	ClientUI ui;
	
	public KickOutActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = ui.pnRoom.list.getSelectedValue();
		ui.net.sendKickOutRequest(String.valueOf(obj));
	}

}
