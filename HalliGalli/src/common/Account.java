package common;

import java.io.Serializable;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
	public String nick;
	public String pass;
	public String email;
	public SocketAddress address;
	public int roomNumber;

	public int score;

	public List<String> playerBundle; // 뒷면 뭉치
	public List<String> playCardBundle; // 공개한 카드 모아놓은 뭉치

	public String playCard;
	public String drawCard;
	public boolean dead;

	public String getPass() {
		return pass;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getNick() {
		return nick;
	}

	public Account(String nick, String pass, String email) {
		this.nick = nick;
		this.pass = pass;
		this.email = email;
		
		playerBundle = new ArrayList<>();
		playCardBundle = new ArrayList<>();
		playCard = "";
		score = 0;
	}
	
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	public int getRoomNumber() {
		return roomNumber;
	}

	public void setSocket(SocketAddress address) {
		this.address = address;
	}

	public SocketAddress getSocket() {
		return address;
	}

	public void setPlayerBundle(List<String> bundle) {
		playerBundle = bundle;
		dead = false;
	}

	public String play() { // 카드 뭉치에서 한 장 뒤집음
		int r = (int) (Math.random() * playerBundle.size());

		playCard = playerBundle.get(r); // 뒤집은 카드가 앞면 카드로
		playCardBundle.add(playCard); // 앞면 카드 뭉치에 뒤집은 카드를 추가
		playerBundle.remove(r); // 카드 뭉치에서 뒤집은 카드 제거
		return playCard;
	}

	public boolean draw() {
		if(playerBundle.size()>0) {
			int r = (int) (Math.random() * playerBundle.size());
			drawCard = playerBundle.get(r); // 뒷면 뭉치에서 한 장 뽑음
			playerBundle.remove(r); // 카드 뭉치에서 뒤집은 카드 제거
			System.out.println("in acct drawCard : " + drawCard);
			return true;
		}else
			return false;
	}

	public String toString() {
		return nick;
	}
}
