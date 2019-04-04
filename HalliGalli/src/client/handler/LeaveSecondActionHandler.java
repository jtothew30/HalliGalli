package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

public class LeaveSecondActionHandler implements ActionListener {
	ClientUI ui;
	
	public LeaveSecondActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ui.net.sendLeaveSecondRequest();
	}

}
