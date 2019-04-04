package client.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class ExitActionHandler implements ActionListener {
	ClientUI ui;
	
	public ExitActionHandler(ClientUI ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int n = JOptionPane.showConfirmDialog(ui, "������ ����?");
		// �� = 0, �ƴϿ� = 1, ��� = 2 , esc = -1
		if(n==-1) {
			
		}else if(n==0) {
			ui.net.sendExitRuquest();			
		}
		
	}

}
