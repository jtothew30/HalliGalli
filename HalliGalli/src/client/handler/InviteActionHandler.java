package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class InviteActionHandler implements ActionListener {
	ClientUI ui;
	
	public InviteActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {	
		System.out.println("invite check");
		ui.net.sendLoginListRequest();
	}
}
