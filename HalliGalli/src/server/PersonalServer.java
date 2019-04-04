package server;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import common.Account;
import common.HalliGalliPool;
import common.Message;
import common.Room;

public class PersonalServer extends Thread {

	static List<SocketAddress> pools; // 연결된 사용자들의 소켓주소를 모을 컬렉션
	static DatagramSocket alramSocket; // 패킷전송시 사용할 UDP 소켓

	static Map<String, Account> allAccounts; // 계정정보를 저장할 맵형 컬렉션
	static List<Account> connectors; // 인증성공해서 접속한 사용자들 저장할 리스트형 컬렉션
	static List<Message> chatLogs; // 채팅 내용 저장 공간
	static List<Message> roomChatLogs; // 게임창 채팅 내용 저장 공간
	static Room[] rooms; // 서버측에서 관리할 방들을 관리할 배열 객체
	static Room[] inviteRooms; // 방 초대를 한 방들을 관리할 배열 객체
	static List<Account> alreadyRoom;
	static List<Account> roomChief; // 방초대를 한 방장을 저장 관리할 객체
	//static Account dead; 
	static List<Account> dead; // 탈락한 사람들을 저장할 컬렉션
	
	static {
		allAccounts = new Hashtable<>(); // File에서 읽어내야 되지 않을까 싶음.
		pools = new ArrayList<>();
		connectors = new Vector<>();
		chatLogs = new Vector<>();
		roomChatLogs = new Vector<>();
		
		dead = new ArrayList<>();
		
		rooms = new Room[10];
		inviteRooms = new Room[10];
		alreadyRoom = new ArrayList<>();
		roomChief = new ArrayList<>();
		//deadCnt = new ArrayList<>();
		//deadOnUser = new ArrayList<>();
		
		try {
			alramSocket = new DatagramSocket(56789);
		} catch (IOException e) {
			System.out.println("alramSocket create failed " + e.toString());
		}
	}
	
	// 모든 접속한 사람들에게 정보를 뿌려주는 소켓
	static void sendAlramToAll(String alram) {
		DatagramPacket p = new DatagramPacket(alram.getBytes(), alram.getBytes().length);

		for (SocketAddress sa : pools) {
			p.setSocketAddress(sa);
			try {
				alramSocket.send(p);
			} catch (IOException e) {
				System.out.println("[server-Thread] send alarm failed target : " + sa);
			}
		}
	}
	
	// 특정 사람들에게만 정보를 뿌려주는 소켓
	static void sendAlramToSome(String alram, List<Account> a) {
		DatagramPacket p = new DatagramPacket(alram.getBytes(), alram.getBytes().length);

		synchronized (a) {
			for (Account sa : a) {
				p.setSocketAddress(sa.getSocket());
				try {
					alramSocket.send(p);
				} catch (IOException e) {
					System.out.println("[server-Thread] send alarm failed target : " + sa);
				}
			}
		}
	}
	
	// 특정 개인에게만 정보를 보내 주는 소켓
	static void sendAlramToIndivisual(String alram, Account ac) {
		DatagramPacket p = new DatagramPacket(alram.getBytes(), alram.getBytes().length);		
				
			p.setSocketAddress(ac.getSocket());						
			try {
				alramSocket.send(p);
			} catch (IOException e) {
				System.out.println("[server-Thread] send alarm failed target : " + ac);
			}
						
	}

	// ===========================================================================================

	Socket socket;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	Account onUser;
	
	// 이 개별서버의 주인을 객체가 알수 있게 변수로 잡아둠. (설정되는 지점은 아래)

	int idx; // 로그인해서 라운지에 들어가는 순간의 채팅 양 저장 공간
	int chatSize; // 게임방에 들어가는 순간의 채팅 양 저장 공간
	long accessTime;
	int currentRoomIdx;
	
	
	HalliGalliPool halliGalliPool;
	
	PersonalServer(Socket soc) {
		halliGalliPool = new HalliGalliPool();
		allAccounts = halliGalliPool.load();
		socket = soc;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("[server-" + getName() + "] connected " + soc.getRemoteSocketAddress());
			//-------------------------------------------------------------
			if(allAccounts!=null) {
				System.out.println("[HalliGalli Server] LOADING COMPLETE...");
			}else {
				System.out.println("[HalliGalli Server] LOADING FAIL...");
				allAccounts = new HashMap<>();
			}
			allAccounts.put("루피", new Account("루피", "1111", "asd@asd.asd"));
			allAccounts.put("조로", new Account("조로", "1111", "asd@asd.asd"));
			allAccounts.put("상디", new Account("상디", "1111", "asd@asd.asd"));
			allAccounts.put("나미", new Account("나미", "1111", "asd@asd.asd"));
			
		} catch (IOException e) {
			System.out.println("[server-" + getName() + "] socket error " + soc.getRemoteSocketAddress());
			throw new RuntimeException();
		}
	}

	public void run() {
		while (!socket.isClosed()) {
			String req;
			try {
				req = (String) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				pools.remove(socket.getRemoteSocketAddress());
				connectors.remove(onUser);
				break;
			}
			System.out.println("[server-" + getName() + "] received request : " + req);
			String[] reqs = req.split("#");
			switch (reqs[0]) {
			case "join": // join#가입하고자하는닉넴#그에따른비번
				joinRequestHandle(reqs[1], reqs[2], reqs[3]);
				break;
			case "auth": // auth#인증하고자하는닉넴#그에따른비번
				authRequestHandle(reqs[1], reqs[2]);
				break;
			case "logout":
				LogoutRequestHandle();
				break;
			case "findid":
				FindIDRequestHandle(reqs[1]);
				break;
			case "findpw":
				FindPWRequestHandle(reqs[1], reqs[2]);
				break;
			case "all":
				allRequestHandle();
				break;
			case "exit":
				exitRequestHandle();
				break;
			case "chat":
				chatRequestHandle(reqs[1]);
				break;
			case "chatLog":
				chatLogRequestHandle();
				break;
			case "roomList":
				roomListRequestHandle();
				break;
			case "enter":
				roomEnterRequestHandle(reqs[1]);
				break;
			case "leave":
				leaveRequestHandle();
				break;
			case "currentRoom":
				currentRoomRequestHandle();
				break;
			case "loginList":
				LoginListRequestHandle();
				break;
			case "ask":
				AskInviteRequestHandle(reqs[1]);
				break;
			case "kickOut":
				KickOutRequestHandle(reqs[1]); 
				break;
			case "roomChat":
				RoomChatRequestHandle(reqs[1]); 
				break;
			case "roomChatLog":
				RoomchatLogRequestHandle(); 
				break;
			case "gameStartRequest":
				gameStartRequestHandle();
				break;
			case "settingRequest":
				settingRequestHandle();
				break;
			case "setting":
				settingHandle();
				break;
			case "draw":
				drawhandle();
				break;
			case "push":
				pushHandle();
				break;
			case "pushResult":
				pushResultHandle(reqs[1], reqs[2]);
				break;
			case "checkGame":
				checkGameHandle();
				break;
			case "gameEnd":
				gameEndHandle();
				break;
			case "resetGame":
				resetGameHandle();
				break;
			default:
				break;
			}
			System.out.println("[Server] allAccounts = "+allAccounts.toString());
		}
		System.out.println("[server-" + getName() + "] disconnect  : " + socket.getRemoteSocketAddress());
		halliGalliPool.save(allAccounts);
	}




	private void resetGameHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		r.GameReset();					
		sendAlramToSome("setting", r.joiner);
	}

	private void gameEndHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];		
		
		synchronized (r.joiner) {
			Account winner = null;
			for(Account ac : r.joiner) {
				if(ac.dead==false) {
					winner = ac;
					break;
				}
			}
			Account user = onUser;
			sendRepsonse(user);
			sendRepsonse(winner);
			sendRepsonse(r);
		}
		System.out.println("game end");
		
	}
	
	
	private void checkGameHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		
		synchronized (r.joiner) {
			if(dead.size() == 2) {
				r.engine.flag=false;
				/*int i = r.joiner.indexOf(dead.get(0));
				r.joiner.remove(r.joiner.get(i));
				int n1 = r.joiner.get(0).playerBundle.size();
				int n2 = r.joiner.get(1).playerBundle.size();
				r.joiner.add(i, dead.get(0));
				
				if(n1+30<=n2 || n2+30<=n1) {
					r.engine.flag=false;
				}*/
			}
		}
	}
	
	
	private void pushResultHandle(String b, String nick) {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		sendAlramToSome("pushResult#"+b+"#"+nick, r.joiner);	
	}

	private void pushHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		boolean b = r.engine.push(onUser); // 종 누른 것의 결과 진행
		
		if(b) { // 성공이라면
			for(Account ac : r.joiner)
				ac.playCard="";
		}
		
		if(onUser.playerBundle.size()==0) { // 만약 실패하여 카드 뭉치가 0이 됐는데
			onUser.dead=true;
			dead.add(onUser);	// 그 시점이 자기 차례였다면 다음으로 차례를 넘김
			System.out.println("탈락 전 cnt" + r.cnt);
			if(r.cnt%3==r.joiner.indexOf(onUser))
				r.cnt++;
			System.out.println("탈락 후 cnt" + r.cnt);
		}
		

		Account user = onUser;
		
		sendRepsonse(b);
		sendRepsonse(user);
	}
	

	private void drawhandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		String card = onUser.play();
		System.out.println(onUser.nick + "-뒤집은 카드 :"+card);
		
		int deadN = 0;
		
		if(onUser.playerBundle.size()==0) {
			onUser.dead=true;
			deadN = r.joiner.indexOf(onUser);
			dead.add(onUser);
		}
		
		System.out.println("dead 체크 : " + dead.isEmpty());
		System.out.println(deadN + "/ 전 " + r.cnt);
		if(!dead.isEmpty()) {	// 탈락한 사람의 순서 건너뛰기
			deadN = r.joiner.indexOf(dead.get(0));
			if((r.cnt+1)%3==deadN)
				r.cnt+=2;
			else
				r.cnt++;
		}else
			r.cnt++;

		System.out.println("후 " + r.cnt);
		
		if(dead.isEmpty())
			r.turnCnt1++;
		else
			r.turnCnt2++;
		
		
		if(dead.isEmpty()) {	// 3명이 한 번씩 플레이 하면 턴이 증가
			if(r.turnCnt1%3==0)	
				r.turn++;
		}else {
			if(r.turnCnt2%2==0)	 // 탈락한 사람이 있다면 남은 두 명이 번갈아 할 시 턴이 증가
				r.turn++;
		}
		
		Account user = onUser;
		sendRepsonse(user);
		sendRepsonse(r);
	}


	private void settingHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		Account user = onUser;
		
		synchronized (r.joiner) {
			sendRepsonse(user);
			sendRepsonse(r);
			for(Account a : r.joiner) {
				if(!a.playerBundle.isEmpty())
					System.out.println("<"+a.nick +">\n"+ a.playerBundle);
			}
		}
		System.out.println();
	}
	
	private void settingRequestHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		
		sendAlramToSome("setting", r.joiner);
		
	}

	private void gameStartRequestHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		r.start();
		dead.clear();

		sendAlramToSome("keyEvent", r.joiner);
		sendAlramToSome("setting", r.joiner);
		
	}
	
	
	//======================================================================================
	private void RoomchatLogRequestHandle() {
		List<Message> sub = new ArrayList<>(roomChatLogs.subList(chatSize, roomChatLogs.size()));
		sendRepsonse(sub);	
	}

	private void RoomChatRequestHandle(String chat) {
		int idx = -1;	
		for(int i=0;i<rooms.length;i++) {
			if(rooms[i]==null)
				continue;
			if(rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		
		roomChatLogs.add(new Message(onUser.nick, chat));
		sendAlramToSome("newRoomChat", r.joiner);
	}
	
	private void KickOutRequestHandle(String nick) {
		Account ac = null;
		Room r = rooms[onUser.roomNumber];
		for(int i=0;i<r.joiner.size();i++) {
			ac = r.joiner.get(i);
			if(ac.nick.equals(nick)) {
				r.joiner.remove(ac);
				alreadyRoom.remove(ac);
			}
		}
		
		sendAlramToSome("changeList", r.joiner);
		sendAlramToIndivisual("out", ac);
	}

	private void LoginListRequestHandle() {	// 자기 방 사람외에 로그인된 사람을 불러올때					
		List<Account> invit = new Vector<>(connectors);	

		Room r = rooms[onUser.getRoomNumber()];
		System.out.println("[방초대]한 room : "+r.toString());
		if(r!=null) {
			System.out.println("[Server] alreadyRoom : " + alreadyRoom.toString());
			for(int j=0;j<alreadyRoom.size();j++) {
				Account a = alreadyRoom.get(j);
				if(invit.contains(a)) {
					int d = invit.indexOf(a);
					invit.remove(d);
				}
			}
		}
		roomChief.add(onUser); // 방 초대한 방장을 저장
		sendRepsonse(invit);
	}
	
	private void ReLoginListRequestHandle() {		
		sendAlramToSome("reLoginList", roomChief);
	}

	private void AskInviteRequestHandle(String inv) { //방 초대 받은 사람에게 응할지 여부를 묻는 코드
		System.out.println("방 초대 받은 사람 :" + inv);				
		
		List<Account> acc = new Vector<>();
		for(int i=0;i<connectors.size();i++) {
			Account ac = connectors.get(i);
			if(ac!=null) {																		
				if(ac.nick.equals(inv)) {
					acc.add(ac);
					sendAlramToSome("askInvite#"+onUser.getRoomNumber(), acc);
					break;
				}
			}
		}
	}		
	
	
	private void currentRoomRequestHandle() {
		int idx = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		sendRepsonse(r);
	}

	// 방퇴장요청처리
	private void leaveRequestHandle() {
		int idx = -1; // 사용자가 있는 방번호를 얻어서

		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] == null)
				continue;
			if (rooms[i].joiner.contains(onUser)) {
				idx = i;
				break;
			}
		}
		Room r = rooms[idx];
		boolean rst = r.leave(onUser);
		sendRepsonse(rst);
		if (r.isEmpty()) {
			rooms[idx] = null;
		}
		alreadyRoom.remove(onUser);
		sendAlramToAll("changeRooms");
		sendAlramToSome("changeList", r.joiner);
		ReLoginListRequestHandle();
	}

	private void roomEnterRequestHandle(String n) {
		int in = Integer.parseInt(n);
		if (rooms[in] != null) {
			if (rooms[in].joiner.size() < 3) {
				rooms[in].enter(onUser);
				onUser.setRoomNumber(in);
				alreadyRoom.add(onUser);
				System.out.println("[Server] roomEnter alreadyRoom : " + alreadyRoom.toString());
				chatSize = roomChatLogs.size();
				sendRepsonse(rooms[in]);
				sendAlramToAll("changeRooms");
				sendAlramToSome("changeList", rooms[in].joiner);
				ReLoginListRequestHandle();
			} else {
				sendRepsonse(null);
			}
		} else {
			Room r = new Room(onUser);
			rooms[in] = r;
			onUser.setRoomNumber(in);
			alreadyRoom.add(onUser);
			sendRepsonse(rooms[in]);
			sendAlramToAll("changeRooms");
			sendAlramToSome("changeList", r.joiner);
			ReLoginListRequestHandle();
		}
	}

	private void roomListRequestHandle() {
		sendRepsonse(rooms);
	}

	// 응답 보낼때 사용할 것 : 일일이 try - catch 가 힘듬.
	private void sendRepsonse(Object obj) {
		try {
			// ObjectOutputStream 로 출력한 객체는 객체값을 자체적으로 기억을 하게 됨.
			// 만약 출력해야 되는 객체가, 이미 출력된 적이 있는 객체면,
			// 그냥 그걸 다시 출력함.
			// 객체값을 기억하고 있는거라서
			// 매번 새로운 객체를
			oos.reset(); // 기억 정보를 날림
			oos.writeObject(obj);
		} catch (IOException e) {
			System.out.println("[server-" + getName() + "] i/o error  : " + e.toString());
			pools.remove(socket.getRemoteSocketAddress());
			connectors.remove(onUser);
			stop();
		}
	}

	// =====================================================================
	private void allRequestHandle() {
		Vector<Account> vc = new Vector<>();
		vc.addAll(connectors);
		/*
		 * for(Account ac : connectors) { vc.add(ac); }
		 */
		System.out.println("[Server] vector = " + vc);
		// sendRepsonse(connectors);
		sendRepsonse(vc);
	}
	
	
	private void FindIDRequestHandle(String email) {
		Account t = null;
		for (Account ac : allAccounts.values()) {
			if (ac.getEmail().equals(email)) {
				t= ac;
				break;
			}
		}
		if(t == null) {
			sendRepsonse(false);
		}else {
			sendRepsonse(true);
			sendRepsonse(t.nick);
		}
	}
	
	private void FindPWRequestHandle(String nick, String email) {
		if (allAccounts.containsKey(nick)) {
			Account ac = allAccounts.get(nick);
			if (ac.getEmail().equals(email)) {
				sendRepsonse(true);
				sendRepsonse(ac.pass);
			} else {
				sendRepsonse(false);
			}
		} else {
			sendRepsonse(false);
		}
	}
	
	private void LogoutRequestHandle() {
		connectors.remove(onUser);
		ReLoginListRequestHandle();
		sendAlramToAll("changeUser");
	}
	
	private void authRequestHandle(String nick, String pass) {
		if (allAccounts.containsKey(nick) && allAccounts.get(nick).getPass().equals(pass)) {
			onUser = allAccounts.get(nick); // *

			if (connectors.contains(onUser)) {
				sendRepsonse(false);
			} else {
				sendRepsonse(true);
				sendAlramToAll("changeUser");
				pools.add(socket.getRemoteSocketAddress());
				accessTime = System.currentTimeMillis();
				idx = chatLogs.size(); // 뒤에 것 부터 채팅할 때
				connectors.add(onUser);
				onUser.setSocket(socket.getRemoteSocketAddress());
				ReLoginListRequestHandle();
			}

		} else {
			sendRepsonse(false);
		}
	}

	private void joinRequestHandle(String nick, String pass, String em) {
		if (allAccounts.containsKey(nick)) {
			sendRepsonse(false);
		} else {
			allAccounts.put(nick, new Account(nick, pass, em));
			sendRepsonse(true);
		}
	}

	private void exitRequestHandle() {
		pools.remove(socket.getRemoteSocketAddress());
		connectors.remove(onUser);
		sendAlramToAll("changeUser");
		stop();
	}

	private void chatRequestHandle(String chat) {
		chatLogs.add(new Message(onUser.nick, chat));
		// sendRepsonse(b);
		sendAlramToAll("newChat");
	}

	private void chatLogRequestHandle() {

		// sendRepsonse(chatLogs);
		// subList에 만들어진 List는 보낼수 없다고 함
		List<Message> sub = new ArrayList<>(chatLogs.subList(idx, chatLogs.size()));
		sendRepsonse(sub);
	}

}
