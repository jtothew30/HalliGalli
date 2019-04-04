package client.panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoomPanel extends JPanel {
	public JTextField tfChat;
	public JLabel lbRoom;
	public JButton btLeave;
	public JButton btKIckOut;
	public JButton btInvite;
	public JList list;
	public JTextArea chatLog;
	public JButton start;
	
	public JButton Bundle1;
	public JLabel Bundle2;
	public JLabel Bundle3;
	
	public JLabel playCard1;
	public JLabel playCard2;
	public JLabel playCard3;

	public JLabel player1;
	public JLabel player2;
	public JLabel player3;
	
	public JLabel state;
	public JLabel turn;
	public JLabel result;
	public JLabel resultText;
	
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	
	public JLabel checkBundle1;
	public JLabel checkBundle2;
	public JLabel checkBundle3;
	
	public JLabel count;
	public JButton btHelp;
	public JButton btRestart;
	
	public RoomPanel() {
		
		setSize(1200, 800);
		setLayout(null);
		setLocation(700, 400);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(967, 10, 221, 168);
		add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
		
		tfChat = new JTextField();
		tfChat.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 11));
		tfChat.setBounds(967, 589, 221, 21);
		add(tfChat);
		tfChat.setColumns(10);
		
		lbRoom = new JLabel("");
		lbRoom.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD | Font.ITALIC, 15));
		lbRoom.setHorizontalAlignment(SwingConstants.LEFT);
		lbRoom.setBounds(12, 10, 454, 28);
		add(lbRoom);
		
		btLeave = new JButton("L E A V E");
		btLeave.setFont(new Font("³ª´®°íµñÄÚµù", Font.PLAIN, 14));
		btLeave.setBounds(977, 720, 196, 28);
		add(btLeave);	
		
		start = new JButton("S T A R T !");
		start.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 20));
		start.setBounds(977, 653, 196, 28);
		add(start);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 10, 943, 754);
		add(panel);
		panel.setLayout(null);
		
		Bundle1 = new JButton("");
		Bundle1.setBounds(426, 454, 99, 137);
		panel.add(Bundle1);
		
		playCard1 = new JLabel("");
		playCard1.setHorizontalAlignment(SwingConstants.CENTER);
		playCard1.setBounds(426, 297, 99, 137);
		panel.add(playCard1);
		
		Bundle2 = new JLabel("");
		Bundle2.setHorizontalAlignment(SwingConstants.CENTER);
		Bundle2.setBounds(772, 117, 99, 137);
		panel.add(Bundle2);
		
		playCard2 = new JLabel("");
		playCard2.setHorizontalAlignment(SwingConstants.CENTER);
		playCard2.setBounds(627, 117, 99, 137);
		panel.add(playCard2);
		
		Bundle3 = new JLabel("");
		Bundle3.setHorizontalAlignment(SwingConstants.CENTER);
		Bundle3.setBackground(Color.LIGHT_GRAY);
		Bundle3.setBounds(67, 117, 99, 137);
		panel.add(Bundle3);
		
		playCard3 = new JLabel("");
		playCard3.setHorizontalAlignment(SwingConstants.CENTER);
		playCard3.setBounds(212, 117, 99, 137);
		panel.add(playCard3);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(967, 188, 221, 391);
		add(scrollPane_1);
		
		chatLog = new JTextArea();
		chatLog.setEditable(false);
		scrollPane_1.setViewportView(chatLog);
		
		btInvite = new JButton("\uCD08\uB300");
		btInvite.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 13));
		btInvite.setBounds(967, 620, 97, 23);
		add(btInvite);
		
		btKIckOut = new JButton("\uAC15\uD1F4");
		btKIckOut.setBounds(1091, 620, 97, 23);
		add(btKIckOut);
		
		
		
		
		player1 = new JLabel("player1");
		player1.setForeground(Color.WHITE);
		player1.setFont(new Font("±¼¸²", Font.BOLD, 16));
		player1.setHorizontalAlignment(SwingConstants.CENTER);
		player1.setBounds(426, 626, 99, 46);
		panel.add(player1);
		
		player2 = new JLabel("player2");
		player2.setForeground(Color.WHITE);
		player2.setHorizontalAlignment(SwingConstants.CENTER);
		player2.setFont(new Font("±¼¸²", Font.BOLD, 16));
		player2.setBounds(772, 34, 99, 46);
		panel.add(player2);
		
		player3 = new JLabel("player3");
		player3.setForeground(Color.WHITE);
		player3.setHorizontalAlignment(SwingConstants.CENTER);
		player3.setFont(new Font("±¼¸²", Font.BOLD, 16));
		player3.setBounds(67, 34, 99, 46);
		panel.add(player3);
		
		state = new JLabel("\uB300\uAE30");
		state.setFont(new Font("±¼¸²", Font.BOLD, 12));
		state.setForeground(Color.WHITE);
		state.setHorizontalAlignment(SwingConstants.CENTER);
		state.setBounds(579, 626, 270, 46);
		panel.add(state);
		
		turn = new JLabel("turn");
		turn.setForeground(new Color(255, 215, 0));
		turn.setFont(new Font("±¼¸²", Font.ITALIC, 16));
		turn.setHorizontalAlignment(SwingConstants.CENTER);
		turn.setBounds(804, 530, 94, 61);
		panel.add(turn);
		
		result = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/ring.png")));
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setBounds(332, 18, 283, 199);
		result.setVisible(false);
		
		resultText = new JLabel("");
		resultText.setFont(new Font("±¼¸²", Font.BOLD, 14));
		resultText.setHorizontalAlignment(SwingConstants.CENTER);
		resultText.setBounds(332, 105, 283, 61);
		panel.add(resultText);
		panel.add(result);
		
		
		
		lblNewLabel_2 = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/bell.png")));
		lblNewLabel_2.setBounds(426, 154, 100, 100);
		panel.add(lblNewLabel_2);
		
		checkBundle1 = new JLabel("\uB0A8\uC740 \uCE74\uB4DC : ");
		checkBundle1.setFont(new Font("±¼¸²", Font.BOLD, 13));
		checkBundle1.setForeground(Color.WHITE);
		checkBundle1.setBounds(550, 570, 105, 21);
		panel.add(checkBundle1);
		
		checkBundle2 = new JLabel("\uB0A8\uC740 \uCE74\uB4DC : ");
		checkBundle2.setForeground(Color.WHITE);
		checkBundle2.setFont(new Font("±¼¸²", Font.BOLD, 13));
		checkBundle2.setBounds(766, 278, 105, 21);
		panel.add(checkBundle2);
		
		checkBundle3 = new JLabel("\uB0A8\uC740 \uCE74\uB4DC : ");
		checkBundle3.setForeground(Color.WHITE);
		checkBundle3.setFont(new Font("±¼¸²", Font.BOLD, 13));
		checkBundle3.setBounds(77, 264, 105, 21);
		panel.add(checkBundle3);
		
		count = new JLabel("");
		count.setFont(new Font("±¼¸²", Font.BOLD, 15));
		count.setHorizontalAlignment(SwingConstants.CENTER);
		count.setForeground(Color.WHITE);
		count.setBounds(802, 415, 96, 92);
		panel.add(count);
		
		btHelp = new JButton("\uB3C4 \uC6C0 \uB9D0");
		btHelp.setFont(new Font("±¼¸²", Font.BOLD, 12));
		btHelp.setBounds(12, 698, 85, 46);
		panel.add(btHelp);
		
		lblNewLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/game.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 943, 754);
		panel.add(lblNewLabel);
		
		btRestart = new JButton("R E A D Y");
		btRestart.setFont(new Font("³ª´®°íµñÄÚµù", Font.BOLD, 12));
		btRestart.setBounds(977, 687, 196, 23);
		add(btRestart);
		
		lblNewLabel_1 = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/gameback.png")));
		lblNewLabel_1.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(0, 0, 1200, 800);
		add(lblNewLabel_1);
	}
}

