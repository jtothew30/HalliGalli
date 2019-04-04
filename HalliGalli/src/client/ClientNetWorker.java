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

	Socket socket; // 주 핵심연결 소켓
	ObjectOutputStream oos;
	ObjectInputStream ois;

	DatagramSocket dataSocket; // 서브소켓 (리시브용)
	
	public int count;
	static Room room;
	String id;
	public boolean flag;
	Account player; // 그 턴에 플레이한 사용자
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
	 * 서버로 요청을 보낼때 사용할 것들을 미리 다 만들어 둘것. 응답을 이벤트리스너로 넘기지 말고, 받아서 여기서 처리.
	 */

	// # 가입 요청 #
	public void sendJoinRequest(String nick, String pass, String em) {
		try {
			synchronized (oos) {
				String req = "join#" + nick + "#" + pass+"#"+em;
				oos.writeObject(req);
				Boolean resp = (Boolean) ois.readObject();
				if (resp) {
					JOptionPane.showMessageDialog(ui, "가입 성공이다.");
					// ui.pnWelcome.tabbedPane.setSelectedIndex(0);
					ui.pnWelcome.tfAuthNick.setText(nick);
					ui.setContentPane(ui.pnWelcome);
				} else {
					JOptionPane.showMessageDialog(ui, "가입 실패다.\n이미 있는 닉네임이다.");
				}
			}

		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다");
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
					ui.setTitle("[" + nick + "]" + " 로그인");
					ui.setContentPane(ui.pnLounge);
					ui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					// x 버튼 눌러도 꺼지지 않게 하는 것

					sendUserListRequest();
					sendRoomListRequest();

				} else {
					JOptionPane.showMessageDialog(ui, "인증 실패다.\n아이디 또는 비밀번호 불일치다.");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다");
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
					JOptionPane.showMessageDialog(ui, "찾으시는 ID는 " + name  + " 입니다.");
					ui.pnFind.tfCPnick.setText(name);
				}else {
					JOptionPane.showMessageDialog(ui, "정확한 Email을 입력해주세요.");
				}
			}
		}catch(ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류입니다.");
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
					JOptionPane.showMessageDialog(ui, nick + " 님의 비밀번호는 " + pass + "입니다.");
					ui.pnWelcome.pfAuthPass.setText(pass);
					ui.setContentPane(ui.pnWelcome);
				}else {
					JOptionPane.showMessageDialog(ui, "정확한 ID/Email을 입력해주세요.");
				}
			}
		}catch(IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류입니다.");
		}
	}
	

	//=======================================================================================
	//===================================로비 부분=============================================
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다");
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
				int n = ui.pnLounge.taLog.getCaretPosition(); // 커서 위치
				ui.pnLounge.taLog.setCaretPosition(txt.length());
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 sendRoomListRequest");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류입니다. sendLogoutRequest");
		}
	}

	
	
	//=======================================================================================
	//===================================겜방 부분=============================================
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
					//JOptionPane.showMessageDialog(ui, "방생성이 되었습니다.");
					Rectangle r = ui.getBounds();
					r.grow(270, 100);
					ui.setBounds(r);
					ui.setContentPane(ui.pnRoom);
				}
				sendCurrentRoomRequest(); // 이건 pnRoom에서 보여지기 위해서이고
				// pnLounge에서 보이는 출력을 위해서는 서버에서 뿌려줘
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 sendRoomEnterRequest");
		}

	}

	// #방 퇴장 요청# : 빈방은 만들어지면서 입장 됨.
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
					JOptionPane.showMessageDialog(ui, "방퇴장 오류다");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 . sendLeaveRequest" + e.toString());
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 sendCurrentRoomRequest");
		}
	}
	
	
	// 같은 방 사람외의 로그인된 사람의 목록을 요청하는 것
		public void sendLoginListRequest() {
			try {
				String req = "loginList";
				oos.writeObject(req);
				Vector<Account> vc = (Vector<Account>) ois.readObject();
				ui.pnInvite.setSize(350, 350);
				ui.pnInvite.list.setListData(vc);
				ui.pnInvite.setVisible(true);
			} catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(ui, "sendLoginListRequest 서버 통신 오류다.");
			}		
		}
		
		// 방장이 서버에게 nick을 방초대를 할거니까 nick에게 물어봐 달라는 의미
		public void sendAllowRequest(String nick) {
			System.out.println("nick = "+ nick);
			try {
				String req = "ask#"+nick;
				oos.writeObject(req);
				ui.pnInvite.setVisible(false);
			}catch(IOException e) {
				JOptionPane.showMessageDialog(ui, "서버 통신 오류다 sendAllowRequest");
			}		
		}
		
		// 방 초대를 받은 사람의 요청에 대한 응답
		public void sendAnswerRequest(String roomNumber) {
			System.out.println("[Client] 요청받은 방 번호 :" + roomNumber);
			int n = JOptionPane.showConfirmDialog(ui, roomNumber+"방 초대를 받았어여. 응하시겠습니까?");
			if(n==0) {
				JOptionPane.showMessageDialog(ui, "초대에 응하셨습니다.");				
				sendRoomEnterRequest(Integer.parseInt(roomNumber));
			}
		}
		
		// "방초대" 창에서 나가고자 할 때 
		public void sendLeaveSecondRequest(){
			ui.pnInvite.setVisible(false);
		}
		
		// 강퇴 요청
		public void sendKickOutRequest(String nick){
			try {
				String req = "kickOut#"+nick;
				oos.writeObject(req);
			}catch(IOException e) {
				JOptionPane.showMessageDialog(ui, "KickOut 서버 통신 오류다");
			}
		}
		
		//강퇴당한 사람의 화면이 pnRoom 화면으로 바뀌게
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
				JOptionPane.showMessageDialog(ui, "roomChat 서버 통신 오류다");
			}
		}
		
		// 게임창 채팅창에 내용을 뿌려주는 거
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
				JOptionPane.showMessageDialog(ui, "roomChatLog 서버 통신 오류다");
			}
		}
		

		
	//=======================================================================================
	//===================================게임 진행=============================================
	//=======================================================================================
			
	public void clickHelp() {
		ui.pnHelf.setSize(750, 599);
		ui.pnHelf.setVisible(true);
	}
		
		
	public void gameStartRequest() {	// 방장이 스타트 누르면 호출
		try {
			synchronized (oos) {
				String req = "gameStartRequest";
				oos.writeObject(req);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 gameStartRequest");
		}
	}
	
	public void keyEventSet() {	// 게임 시작 시 종 울리기 리스너를 붙임
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 settingRequest");
		}
	}
	
	public void setting() {	// 테이블을 세팅 & refresh
		try {
			synchronized (oos) {
				String req = "setting";
				oos.writeObject(req);
				Account check = (Account)ois.readObject();
				room = (Room) ois.readObject();
				flag=room.engine.flag;	// 현재 게임이 진행 중인지 확인
				
				if(flag) {	// 게임이 진행 중이라면
					Account ac=null;
					for(Account c : room.joiner) {		//참가자 리스트에서 자신의 계정 확인
						if(c.nick.equals(check.nick))
							ac = c;
					}
					
					if(ac.dead) { // 만약 자신이 탈락한 상태라면 더 이상 종을 누를 수 없음.
						ui.pnKeyListener.p.stop();
						ui.pnRoom.removeKeyListener(ui.pnKeyListener);
						System.out.println("죽은 사람 : " + ac.nick);
					}
			
					synchronized (room) {
						System.out.println("cnt in set:"+room.cnt + "-"+room.joiner.get(room.cnt%3).nick+"의 차례");
						int idx = room.joiner.indexOf(ac);
						Timer timer = new Timer();
						if((room.cnt)%3 == idx) {	// 참가자 리스트에 저장된 순서대로 게임을 진행함. 
							ui.pnRoom.Bundle1.setEnabled(true);
							ui.pnRoom.state.setText("당신의 차례입니다!");
							
							/*synchronized (timer) {	// 시간 제한 방식 (어느 순간부터 카운트가 여러개씩 떨어짐)
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
							ui.pnRoom.state.setText("상대방의 차례입니다.");
						}
						
						ui.pnRoom.turn.setText(String.valueOf(room.turn));	// 턴 카운트
						
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
						
						ui.pnRoom.checkBundle1.setText("남은 카드 : " + ac.playerBundle.size());
						ui.pnRoom.checkBundle2.setText("남은 카드 : " + room.joiner.get(0).playerBundle.size());
						ui.pnRoom.checkBundle3.setText("남은 카드 : " + room.joiner.get(1).playerBundle.size());
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 setting");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 drawRequest");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 pushRequest");
		}
	}
	
	public void pushResult(String b, String pusher) {
		Boolean bb = Boolean.valueOf(b);
		if(bb) {
			ui.pnRoom.resultText.setText(pusher+"! 성공!!!");
			ui.pnRoom.result.setVisible(true);
		}
		else {
			ui.pnRoom.resultText.setText(pusher+"! 실패...ㅜㅜ");
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
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 pushRequest");
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
				JOptionPane.showMessageDialog(ui, "<<GAME!!>>\n승자는 " + winner.nick + "!!!");;
			}
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 gameEnd");
		}
	}
	
	public void resetGame() {
		try {
			synchronized (oos) {
				String req = "resetGame";
				oos.writeObject(req);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ui, "서버 통신 오류다 resetGame");
		}
	}


	// 서버측으로부터 불시에 전달받게될 알림패킷을 처리하는 부분
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
