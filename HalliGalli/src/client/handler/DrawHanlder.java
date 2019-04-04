package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

public class DrawHanlder implements ActionListener{
	ClientUI ui;
	public DrawHanlder(ClientUI ui){
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ui.net.drawRequest();
	}
}
