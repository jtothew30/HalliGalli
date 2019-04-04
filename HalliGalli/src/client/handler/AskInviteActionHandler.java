package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

public class AskInviteActionHandler implements ActionListener {
	ClientUI ui;
	
	public AskInviteActionHandler (ClientUI ui) {
		this.ui = ui;
	}
	@Override
	public void actionPerformed(ActionEvent e) {		 		 
		 Object obj = ui.pnInvite.list.getSelectedValue();
		 ui.net.sendAllowRequest(String.valueOf(obj));
	}

}
