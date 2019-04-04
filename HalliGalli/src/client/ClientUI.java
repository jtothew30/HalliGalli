package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import client.handler.AskInviteActionHandler;
import client.handler.AuthActionHandler;
import client.handler.ChatActionHandler;
import client.handler.DrawHanlder;
import client.handler.EnterActionHandler;
import client.handler.ExitActionHandler;
import client.handler.FindIDActionHandler;
import client.handler.FindPWActionHandler;
import client.handler.InviteActionHandler;
import client.handler.JoinActionHandler;
import client.handler.KickOutActionHandler;
import client.handler.LeaveActionHandler;
import client.handler.LeaveSecondActionHandler;
import client.handler.LogoutActionHandler;
import client.handler.PushingBellHandler;
import client.handler.RestartActionHandler;
import client.handler.RoomChatActionHandler;
import client.panel.FindPanel;
import client.panel.HelpPanel;
import client.panel.InviteListDialog;
import client.panel.JoinPanel;
import client.panel.LoungePanel;
import client.panel.RoomPanel;
import client.panel.WelcomePanel;

class JoinHandler implements ActionListener {
	public ClientUI ui;

	public JoinHandler(ClientUI ui) {
		this.ui = ui;
	}

	public void actionPerformed(ActionEvent e) {
		ui.pnJoin.tfJoinNick.setText("");
		ui.pnJoin.pfJoinPass.setText("");
		ui.pnJoin.tfJoinemail.setText("");
		ui.setContentPane(ui.pnJoin);
	}
}

class FindHandler implements ActionListener{
	ClientUI ui;
	
	public FindHandler(ClientUI ui) {
		this.ui=ui;
	}

	public void actionPerformed(ActionEvent e) {
		ui.pnFind.tfCPEmail.setText("");
		ui.pnFind.tfCPnick.setText("");
		ui.pnFind.tfCPEmail2.setText("");
		ui.setContentPane(ui.pnFind);
	}
}


class FindCancelHandler implements ActionListener{
	ClientUI ui;
	public FindCancelHandler(ClientUI ui) {
		this.ui=ui;
	}
	public void actionPerformed(ActionEvent e) {
		ui.setContentPane(ui.pnWelcome);
	}
}



class JoinCancelHandler implements ActionListener{
	ClientUI ui;
	public JoinCancelHandler(ClientUI ui) {
		this.ui=ui;
	}
	public void actionPerformed(ActionEvent e) {
		ui.setContentPane(ui.pnWelcome);
	}
}


class GameStartHandler implements ActionListener {
	ClientUI ui;

	GameStartHandler(ClientUI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ui.net.gameStartRequest();
	}
}

class HelpHandler implements ActionListener{
	ClientUI ui;

	HelpHandler(ClientUI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ui.net.clickHelp();
	}
}

public class ClientUI extends JFrame {

	public ClientNetWorker net;

	public WelcomePanel pnWelcome;
	public JoinPanel pnJoin;
	public LoungePanel pnLounge;
	public RoomPanel pnRoom;
	public InviteListDialog pnInvite;
	public FindPanel pnFind;
	public HelpPanel pnHelf;
	
	public PushingBellHandler pnKeyListener;
	
	public ClientUI(String ip) {
		net = new ClientNetWorker(ip, this);
		setUIcomponent();
		addListeners();
	}

	private void addListeners() {
		pnWelcome.pfAuthPass.addActionListener(new AuthActionHandler(this));
		pnWelcome.btJoin.addActionListener(new JoinHandler(this));
		pnWelcome.btfind.addActionListener(new FindHandler(this));
	
		pnJoin.btnjoin.addActionListener(new JoinActionHandler(this));
		pnJoin.btCancel.addActionListener(new JoinCancelHandler(this));
		
		pnFind.btnAccess.addActionListener(new FindIDActionHandler(this));
		pnFind.btnAccess2.addActionListener(new FindPWActionHandler(this));
		pnFind.btnback.addActionListener(new FindCancelHandler(this));
		
		pnLounge.btExit.addActionListener(new ExitActionHandler(this));
		pnLounge.tfChat.addActionListener(new ChatActionHandler(this));
		pnLounge.btnLogout.addActionListener(new LogoutActionHandler(this));
		
		for (JButton b : pnLounge.rbts) {
			b.addActionListener(new EnterActionHandler(this));
		}
		pnRoom.btLeave.addActionListener(new LeaveActionHandler(this));
		pnRoom.start.addActionListener(new GameStartHandler(this));
		
		pnInvite.btInviteSecond.addActionListener(new AskInviteActionHandler(this));
		pnInvite.btLeaveSecond.addActionListener(new LeaveSecondActionHandler(this));
		pnRoom.btInvite.addActionListener(new InviteActionHandler(this));
		pnRoom.btKIckOut.addActionListener(new KickOutActionHandler(this));
		pnRoom.tfChat.addActionListener(new RoomChatActionHandler(this));
		pnRoom.Bundle1.addActionListener(new DrawHanlder(this));
		pnRoom.btHelp.addActionListener(new HelpHandler(this));
		
		pnRoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// System.out.println("mouseClicked");
				pnRoom.requestFocusInWindow();
			}
		});
		
		pnRoom.btRestart.addActionListener(new RestartActionHandler(this));
	}

	private void setUIcomponent() {
		UIManager.put("OptionPane.font", new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		UIManager.put("OptionPane.messageFont", new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 12));
		UIManager.put("OptionPane.buttonFont", new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 13));

		pnWelcome = new WelcomePanel();
		pnJoin = new JoinPanel();
		pnLounge = new LoungePanel();
		pnRoom = new RoomPanel();
		pnInvite = new InviteListDialog();
		pnFind = new FindPanel();
		pnHelf = new HelpPanel();

		setTitle("Halli Galli");
		setSize(500, 425);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		int rand = (int) (Math.random() * 8) * 30;
		setLocation(300 + rand, 200 + rand);
		setContentPane(pnWelcome);
		
		net.homeThread.start();
	}
}
