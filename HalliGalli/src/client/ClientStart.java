package client;

import javax.swing.JOptionPane;

public class ClientStart {
	public static void main(String[] args) {
		//"192.168.104.153"; //192.168.35.207   
		String ip = JOptionPane.showInputDialog("IP ют╥б");
		ClientUI ui = new ClientUI(ip);
		ui.setVisible(true);		
	}
}
