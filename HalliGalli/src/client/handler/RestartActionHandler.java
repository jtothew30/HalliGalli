package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

public class RestartActionHandler implements ActionListener {
	ClientUI ui;
	
	public RestartActionHandler(ClientUI ui){
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ui.net.resetGame();
	}

}
