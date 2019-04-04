package client;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import client.handler.PushingBellHandler;
import client.thread.HomeThread;
import common.Account;
import common.Message;
import common.Room;

public class ClientNetWorker extends Thread {
	static HomeThread homeThread;
	static {
		homeThread = new HomeThread();
	}
	ClientUI ui;

	Socket socket; // �� �ٽɿ��� ����
	ObjectOutputStream oos;
	ObjectInputStream ois;

	DatagramSocket dataSocket; // ������� (���ú��)
	
	public int count;
	static Room room;
	String id;
	public boolean flag;
	Account player; // �� �Ͽ� �÷����� �����
	String card;
	
	public ClientNetWorker(String ip, ClientUI ui) {
		this.ui = ui;
		//user = new Account("", "");
		player = new Account("", "","");
		card = "";
		flag=false;
		try {
			socket = new Socket(ip, 56789);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			dataSocket = new DatagramSocket(socket.getLocalPort());
			start();
		} catch (IOException e) {
			System.out.println("[client] network error");
			throw new RuntimeException();
		}
	}

	/*
	 * ������ ��û�� ������ ����� �͵��� �̸� �� ����� �Ѱ�. ������ �̺�Ʈ�����ʷ� �ѱ��� ����, �޾Ƽ� ���⼭ ó��.
	 */

	// # ���� ��û #
	public void sendJoinRequest(String nick, String pass, String em) {
		try {
			synchronized (oos) {
				String req = "join#" + nick + "#" + pass+"#"+em;
				oos.writeObject(req);
				Boolean resp = (Boolean) ois.readObject();
				if (resp) {
					JOptionPane.showMessageDialog(ui, "���� �����̴�.");
					// ui.pnWelcome.tabbedPane.setSelectedIndex(0);
					ui.pnWelcome.tfAuthNick.setText(nick);
					ui.setContentPane(ui.pnWelcome);
				} else {
					JOptionPane.showMessageDialog(ui, "���� ���д�.\n�̹� �ִ� �г����̴�.");
				}
			}

		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������");
		}

	}

	public void sendAuthRequest(String nick, String pass) {
		try {
			synchronized (oos) {
				String req = "auth#" + nick + "#" + pass;
				oos.writeObject(req);
				Boolean resp = (Boolean) ois.readObject();
				if (resp) {
					id = nick;
					Rectangle r = ui.getBounds();
					r.grow(100, 100);
					ui.setBounds(r);
					ui.setTitle("[" + nick + "]" + " �α���");
					ui.setContentPane(ui.pnLounge);
					ui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					// x ��ư ������ ������ �ʰ� �ϴ� ��

					sendUserListRequest();
					sendRoomListRequest();

				} else {
					JOptionPane.showMessageDialog(ui, "���� ���д�.\n���̵� �Ǵ� ��й�ȣ ����ġ��.");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������");
		}
	}
	
	
	public void sendFindIDRequest(String email) {
		try {
			synchronized (oos) {
				String req = "findid#"+email;
				oos.writeObject(req);
				Boolean resp = (Boolean)ois.readObject();
				if(resp) {
					String name = (String)ois.readObject();
					JOptionPane.showMessageDialog(ui, "ã���ô� ID�� " + name  + " �Դϴ�.");
					ui.pnFind.tfCPnick.setText(name);
				}else {
					JOptionPane.showMessageDialog(ui, "��Ȯ�� Email�� �Է����ּ���.");
				}
			}
		}catch(ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� �����Դϴ�.");
		}
	}
	
	public void sendFindPWRequest(String nick, String email) {
		try {
			synchronized (oos) {
				String req = "findpw#"+nick+"#"+email;
				oos.writeObject(req);
				Boolean resp = (Boolean)ois.readObject();
				if(resp) {
					String pass = (String)ois.readObject();
					System.out.println(pass);
					JOptionPane.showMessageDialog(ui, nick + " ���� ��й�ȣ�� " + pass + "�Դϴ�.");
					ui.pnWelcome.pfAuthPass.setText(pass);
					ui.setContentPane(ui.pnWelcome);
				}else {
					JOptionPane.showMessageDialog(ui, "��Ȯ�� ID/Email�� �Է����ּ���.");
				}
			}
		}catch(IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� �����Դϴ�.");
		}
	}
	

	//=======================================================================================
	//===================================�κ� �κ�=============================================
	//=======================================================================================
	public void sendUserListRequest() {
		try {
			synchronized (oos) {
				String req = "all";
				oos.writeObject(req);
				Vector resp = (Vector) ois.readObject();

				ui.pnLounge.liUser.setListData(resp);
			}

		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������");
		}
	}

	public void sendExitRuquest() {
		try {
			synchronized (oos) {
				String req = "exit";
				oos.writeObject(req);
				System.exit(0);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������");
		}
	}

	public void sendChatRequest(String txt) {
		try {
			synchronized (oos) {
				String req = "chat#" + txt;
				oos.writeObject(req);
				ui.pnLounge.tfChat.setText("");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������");
		}
	}

	public void sendChatLogRequest() {
		try {
			synchronized (oos) {
				String req = "chatLog";
				oos.writeObject(req);
				List<Message> resp = (List) ois.readObject();
				String txt = "";
				for (Message m : resp) {
					txt += m.toString() + "\n";
				}
				ui.pnLounge.taLog.setText(txt);
				int n = ui.pnLounge.taLog.getCaretPosition(); // Ŀ�� ��ġ
				ui.pnLounge.taLog.setCaretPosition(txt.length());
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������");
		}
	}

	public void sendRoomListRequest() {
		try {
			synchronized (oos) {
				String req = "roomList";
				oos.writeObject(req);
				Room[] ar = (Room[]) ois.readObject();
				for (int i = 0; i < ar.length; i++) {
					System.out.println(i + " = " + ar[i]);
					JButton bt = ui.pnLounge.rbts.get(i);
					if (ar[i] != null) {
						bt.setText(ar[i].toString());
						if (ar[i].joiner.size() == 3) {
							bt.setEnabled(false);
						} else {
							bt.setEnabled(true);
						}
					} else {
						bt.setText("");
						bt.setEnabled(true);
					}
				}
			}

		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ sendRoomListRequest");
		}
	}
	
	public void sendLogoutRequest() {
		try {
			synchronized (oos) {
				String req = "logout";
				oos.writeObject(req);
				Rectangle r = ui.getBounds();
				r.setSize(500, 425);
				ui.setBounds(r);
				ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ui.setContentPane(ui.pnWelcome);
			}
		}catch(IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� �����Դϴ�. sendLogoutRequest");
		}
	}

	
	
	//=======================================================================================
	//===================================�׹� �κ�=============================================
	//=======================================================================================
	public void sendRoomEnterRequest(int index) {
		try {
			synchronized (oos) {
				String req = "enter#" + index;
				oos.writeObject(req);
				Room obj = (Room) ois.readObject();
				room = obj;
				System.out.println("[Client] room response = " + obj.toString());
				if (obj != null) {
					//JOptionPane.showMessageDialog(ui, "������� �Ǿ����ϴ�.");
					Rectangle r = ui.getBounds();
					r.grow(270, 100);
					ui.setBounds(r);
					ui.setContentPane(ui.pnRoom);
				}
				sendCurrentRoomRequest(); // �̰� pnRoom���� �������� ���ؼ��̰�
				// pnLounge���� ���̴� ����� ���ؼ��� �������� �ѷ���
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ sendRoomEnterRequest");
		}

	}

	// #�� ���� ��û# : ����� ��������鼭 ���� ��.
	public void sendLeaveRequest() {
		try {
			synchronized (oos) {
				String req = "leave";
				oos.writeObject(req);
				Boolean r = (Boolean) ois.readObject();
				if (r) {
					Rectangle rr = ui.getBounds();
					rr.grow(-270, -100);
					ui.setBounds(rr);
					ui.setContentPane(ui.pnLounge);
				} else {
					JOptionPane.showMessageDialog(ui, "������ ������");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ . sendLeaveRequest" + e.toString());
			System.out.println(e.toString() + ".." + e.getMessage());
		}
	}

	public void sendCurrentRoomRequest() {
		try {
			synchronized (oos) {
				String req = "currentRoom";
				oos.writeObject(req);
				Room current = (Room) ois.readObject();
				if (current != null) {
					Vector<Account> li =(Vector<Account>)current.joiner;
					ui.pnRoom.Bundle1.setEnabled(false);
					if(li.get(0).nick.equals(id)) {		
						if(current.joiner.size()==3) {
							ui.pnRoom.start.setEnabled(true);
							ui.pnRoom.btInvite.setEnabled(false);
						}
						else {
							ui.pnRoom.start.setEnabled(false);
							ui.pnRoom.btInvite.setEnabled(true);
							ui.pnRoom.btKIckOut.setEnabled(true);
						}
						ui.pnRoom.btKIckOut.setEnabled(true);
						ui.pnRoom.list.setListData(li);
					}else {
						ui.pnRoom.start.setEnabled(false);
						ui.pnRoom.btInvite.setEnabled(false);
						ui.pnRoom.btKIckOut.setEnabled(false);
						ui.pnRoom.list.setListData(li);
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ sendCurrentRoomRequest");
		}
	}
	
	
	// ���� �� ������� �α��ε� ����� ����� ��û�ϴ� ��
		public void sendLoginListRequest() {
			try {
				String req = "loginList";
				oos.writeObject(req);
				Vector<Account> vc = (Vector<Account>) ois.readObject();
				ui.pnInvite.setSize(350, 350);
				ui.pnInvite.list.setListData(vc);
				ui.pnInvite.setVisible(true);
			} catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(ui, "sendLoginListRequest ���� ��� ������.");
			}		
		}
		
		// ������ �������� nick�� ���ʴ븦 �ҰŴϱ� nick���� ����� �޶�� �ǹ�
		public void sendAllowRequest(String nick) {
			System.out.println("nick = "+ nick);
			try {
				String req = "ask#"+nick;
				oos.writeObject(req);
				ui.pnInvite.setVisible(false);
			}catch(IOException e) {
				JOptionPane.showMessageDialog(ui, "���� ��� ������ sendAllowRequest");
			}		
		}
		
		// �� �ʴ븦 ���� ����� ��û�� ���� ����
		public void sendAnswerRequest(String roomNumber) {
			System.out.println("[Client] ��û���� �� ��ȣ :" + roomNumber);
			int n = JOptionPane.showConfirmDialog(ui, roomNumber+"�� �ʴ븦 �޾Ҿ. ���Ͻðڽ��ϱ�?");
			if(n==0) {
				JOptionPane.showMessageDialog(ui, "�ʴ뿡 ���ϼ̽��ϴ�.");				
				sendRoomEnterRequest(Integer.parseInt(roomNumber));
			}
		}
		
		// "���ʴ�" â���� �������� �� �� 
		public void sendLeaveSecondRequest(){
			ui.pnInvite.setVisible(false);
		}
		
		// ���� ��û
		public void sendKickOutRequest(String nick){
			try {
				String req = "kickOut#"+nick;
				oos.writeObject(req);
			}catch(IOException e) {
				JOptionPane.showMessageDialog(ui, "KickOut ���� ��� ������");
			}
		}
		
		//������� ����� ȭ���� pnRoom ȭ������ �ٲ��
		public void outRoom() {
			Rectangle r = ui.getBounds();
			r.setSize(700, 600);
			ui.setBounds(r);
			ui.setContentPane(ui.pnLounge);
			sendRoomListRequest();
		}
		
		public void sendRoomChatRequest(String chat) {
			try {
				synchronized (oos){
					String req = "roomChat#"+chat;
					oos.writeObject(req);
				}
			}catch(IOException e) {
				JOptionPane.showMessageDialog(ui, "roomChat ���� ��� ������");
			}
		}
		
		// ����â ä��â�� ������ �ѷ��ִ� ��
		public void sendRoomChatLogRequest() {
			try {
				synchronized (oos) {
					String req = "roomChatLog";
					oos.writeObject(req);
					List<Message> resp = (List) ois.readObject();
					String txt = "";
					for (Message m : resp) {
						txt += m.toString() + "\n";
					}
					ui.pnRoom.chatLog.setText(txt);
					ui.pnRoom.chatLog.setCaretPosition(txt.length());
				}
			}catch(ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(ui, "roomChatLog ���� ��� ������");
			}
		}
		

		
	//=======================================================================================
	//===================================���� ����=============================================
	//=======================================================================================
			
	public void clickHelp() {
		ui.pnHelf.setSize(750, 599);
		ui.pnHelf.setVisible(true);
	}
		
		
	public void gameStartRequest() {	// ������ ��ŸƮ ������ ȣ��
		try {
			synchronized (oos) {
				String req = "gameStartRequest";
				oos.writeObject(req);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ gameStartRequest");
		}
	}
	
	public void keyEventSet() {	// ���� ���� �� �� �︮�� �����ʸ� ����
		ui.pnKeyListener = new PushingBellHandler(ui);
		ui.pnRoom.addKeyListener(ui.pnKeyListener);	
		ui.pnRoom.state.setText("");
	}
	
	

	public void settingRequest() {
		try {
			synchronized (oos) {
				String req = "settingRequest";
				oos.writeObject(req);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ settingRequest");
		}
	}
	
	public void setting() {	// ���̺��� ���� & refresh
		try {
			synchronized (oos) {
				String req = "setting";
				oos.writeObject(req);
				Account check = (Account)ois.readObject();
				room = (Room) ois.readObject();
				flag=room.engine.flag;	// ���� ������ ���� ������ Ȯ��
				
				if(flag) {	// ������ ���� ���̶��
					Account ac=null;
					for(Account c : room.joiner) {		//������ ����Ʈ���� �ڽ��� ���� Ȯ��
						if(c.nick.equals(check.nick))
							ac = c;
					}
					
					if(ac.dead) { // ���� �ڽ��� Ż���� ���¶�� �� �̻� ���� ���� �� ����.
						ui.pnKeyListener.p.stop();
						ui.pnRoom.removeKeyListener(ui.pnKeyListener);
						System.out.println("���� ��� : " + ac.nick);
					}
			
					synchronized (room) {
						System.out.println("cnt in set:"+room.cnt + "-"+room.joiner.get(room.cnt%3).nick+"�� ����");
						int idx = room.joiner.indexOf(ac);
						Timer timer = new Timer();
						if((room.cnt)%3 == idx) {	// ������ ����Ʈ�� ����� ������� ������ ������. 
							ui.pnRoom.Bundle1.setEnabled(true);
							ui.pnRoom.state.setText("����� �����Դϴ�!");
							
							/*synchronized (timer) {	// �ð� ���� ��� (��� �������� ī��Ʈ�� �������� ������)
								count = 15;
								TimerTask task = new TimerTask() {
									@Override
									public void run() {
										if(count >= 0) {
											ui.pnRoom.count.setText("["+count+"]");
											count--;
										}else
											ui.pnRoom.Bundle1.doClick();
									}
								};
								timer.schedule(task, 1000, 1000);
							}
							
							*/
							player = ac;
						}else {
							//timer.cancel();
							ui.pnRoom.Bundle1.setEnabled(false);
							ui.pnRoom.state.setText("������ �����Դϴ�.");
						}
						
						ui.pnRoom.turn.setText(String.valueOf(room.turn));	// �� ī��Ʈ
						
						room.joiner.remove(ac);
						ui.pnRoom.player1.setText(ac.nick);
						ui.pnRoom.player2.setText(room.joiner.get(0).nick);
						ui.pnRoom.player3.setText(room.joiner.get(1).nick);
						
						ui.pnRoom.playCard1.setIcon(setImg(ac.playCard));
						ui.pnRoom.playCard2.setIcon(setImg(room.joiner.get(0).playCard));
						ui.pnRoom.playCard3.setIcon(setImg(room.joiner.get(1).playCard));
						
						setCardImg(ui.pnRoom.Bundle1, ac.playerBundle.size());
						setCardImg(ui.pnRoom.Bundle2, room.joiner.get(0).playerBundle.size());
						setCardImg(ui.pnRoom.Bundle3, room.joiner.get(1).playerBundle.size());
						
						ui.pnRoom.checkBundle1.setText("���� ī�� : " + ac.playerBundle.size());
						ui.pnRoom.checkBundle2.setText("���� ī�� : " + room.joiner.get(0).playerBundle.size());
						ui.pnRoom.checkBundle3.setText("���� ī�� : " + room.joiner.get(1).playerBundle.size());
						room.joiner.add(idx, ac);
					}
					
					ui.pnRoom.resultText.setText("");
					ui.pnRoom.result.setVisible(false);
					ui.pnRoom.start.setEnabled(false);
					ui.pnRoom.btLeave.setEnabled(false);
					ui.pnRoom.btKIckOut.setEnabled(false);
					checkGame();
					
					/*if(ui.pnRoom.Bundle1.isEnabled()) {
						
					}*/
				}else {
					gameEnd();
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ setting");
		}
	}
	
	public void drawRequest() {
		try {
			synchronized (oos) {
				String req = "draw";
				oos.writeObject(req);
				
				Account check = (Account)ois.readObject();
				room = (Room) ois.readObject();
				
				Account ac=new Account("", "","");	
				for(Account c : room.joiner) {
					if(c.nick.equals(check.nick))
						ac = c;
				}
				System.out.println("cnt in draw:"+room.cnt);
				
				card = ac.playCard;
				System.out.println("check play card:"+card);
				ui.pnRoom.playCard1.setIcon(setImg(card));
				ui.pnRoom.requestFocusInWindow();
				//checkGame();
				settingRequest();
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ drawRequest");
		}
	}
	
	
	public void pushRequest() {
		try {
			synchronized (oos) {
				String req = "push";
				oos.writeObject(req);
				
				boolean b = (boolean)ois.readObject();
				Account pusher = (Account)ois.readObject();
				
				oos.writeObject("pushResult#"+b+"#"+pusher.nick);
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ pushRequest");
		}
	}
	
	public void pushResult(String b, String pusher) {
		Boolean bb = Boolean.valueOf(b);
		if(bb) {
			ui.pnRoom.resultText.setText(pusher+"! ����!!!");
			ui.pnRoom.result.setVisible(true);
		}
		else {
			ui.pnRoom.resultText.setText(pusher+"! ����...�̤�");
			ui.pnRoom.result.setVisible(true);
		}
		
		try {
			Thread.sleep(1500);
		}catch(InterruptedException e) {
			System.out.println(e.getMessage());
		}
		settingRequest();
	}
	
	
	public void checkGame() {
		try {
			synchronized (oos) {
				String req = "checkGame";
				oos.writeObject(req);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ pushRequest");
		}
	}
	
	
	public void gameEnd() {
		try {
			synchronized (oos) {
				String req = "gameEnd";
				oos.writeObject(req);
				
				Account check = (Account)ois.readObject();
				Account winner = (Account)ois.readObject();
				room = (Room) ois.readObject();
				
				//if(check != null)
				
				if(check.nick.equals(room.joiner.get(0).nick)) {
					
					ui.pnRoom.start.setEnabled(true);
					ui.pnRoom.btKIckOut.setEnabled(true);
					ui.pnRoom.btLeave.setEnabled(true);
				}
				ui.pnRoom.turn.setText("");
				ui.pnRoom.state.setText("");
				ui.pnRoom.btLeave.setEnabled(true);
				ui.pnRoom.Bundle1.setEnabled(false);
//				if(check.nick.equals(winner.nick)) {
				
					ui.pnKeyListener.p.stop();
					ui.pnRoom.removeKeyListener(ui.pnKeyListener);
//				}
				JOptionPane.showMessageDialog(ui, "<<GAME!!>>\n���ڴ� " + winner.nick + "!!!");;
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ gameEnd");
		}
	}
	
	public void resetGame() {
		try {
			synchronized (oos) {
				String req = "resetGame";
				oos.writeObject(req);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "���� ��� ������ resetGame");
		}
	}


	// ���������κ��� �ҽÿ� ���޹ްԵ� �˸���Ŷ�� ó���ϴ� �κ�
	public void run() {
		while (!dataSocket.isClosed()) {
			try {
				DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
				dataSocket.receive(p);
				String alram = new String(p.getData(), 0, p.getLength());
				System.out.println("[client] packet received : " + alram);
				String[] alarm = alram.split("#");
				switch (alarm[0]) {
				case "changeUser":
					sendUserListRequest();
					break;
				case "newChat":
					sendChatLogRequest();
					break;
				case "roomList":
					sendRoomListRequest();
					break;
				case "changeRooms":
					sendRoomListRequest();
					break;
				case "changeList":
					sendCurrentRoomRequest();
					break;
				case "askInvite":
					sendAnswerRequest(alarm[1]); 
					break;
				case "out":
					outRoom(); 
					break;
				case "newRoomChat":
					sendRoomChatLogRequest();
					break;
				case "keyEvent":
					keyEventSet();
					break;
				case "setting":
					setting();
					break;
				case "pushResult":
					pushResult(alarm[1], alarm[2]);
					break;
				case "reLoginList":
					sendLoginListRequest();
					break;
				default:
					break;
				}

			} catch (IOException e) {
				dataSocket.close();
				break;
			}
		}
	}
	
	public ImageIcon setImg(String s) {
		
		String[] fruit = s.split("#");
		switch(fruit[0]) {
		case "banana":
			switch(fruit[1]) {
			case "1":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/banana1.jpg"));
			case "2":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/banana2.jpg"));
			case "3":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/banana3.jpg"));
			case "4":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/banana4.jpg"));
			case "5":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/banana5.jpg"));
			}
		case "lime":
			switch(fruit[1]) {
			case "1":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/lime1.jpg"));
			case "2":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/lime2.jpg"));
			case "3":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/lime3.jpg"));
			case "4":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/lime4.jpg"));
			case "5":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/lime5.jpg"));
			}
		case "berry":
			switch(fruit[1]) {
			case "1":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/berry1.jpg"));
			case "2":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/berry2.jpg"));
			case "3":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/berry3.jpg"));
			case "4":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/berry4.jpg"));
			case "5":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/berry5.jpg"));
			}
		case "plum":
			switch(fruit[1]) {
			case "1":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/plum1.jpg"));
			case "2":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/plum2.jpg"));
			case "3":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/plum3.jpg"));
			case "4":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/plum4.jpg"));
			case "5":
				return new ImageIcon(ClassLoader.getSystemResource("image/fruit/plum5.jpg"));
			}
		default:
			return null;
		}
	}
	
	public void setCardImg(Object o, int n) {
		if(n>0) {
			if(o instanceof JButton)
				((JButton) o).setIcon(new ImageIcon(ClassLoader.getSystemResource("image/cardback.png")));
			else if(o instanceof JLabel)
				((JLabel) o).setIcon(new ImageIcon(ClassLoader.getSystemResource("image/cardback.png")));
		}else {
			if(o instanceof JButton)
				((JButton) o).setIcon(new ImageIcon(ClassLoader.getSystemResource("image/cardx.png")));
			else if(o instanceof JLabel)
				((JLabel) o).setIcon(new ImageIcon(ClassLoader.getSystemResource("image/cardx.png")));
		}
	}

}
