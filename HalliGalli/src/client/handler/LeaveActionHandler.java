package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

public class LeaveActionHandler implements ActionListener {
	ClientUI ui;
	
	public LeaveActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ui.net.sendLeaveRequest();

	}

}
