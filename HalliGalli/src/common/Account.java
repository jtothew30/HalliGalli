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

	public List<String> playerBundle; // �޸� ��ġ
	public List<String> playCardBundle; // ������ ī�� ��Ƴ��� ��ġ

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

	public String play() { // ī�� ��ġ���� �� �� ������
		int r = (int) (Math.random() * playerBundle.size());

		playCard = playerBundle.get(r); // ������ ī�尡 �ո� ī���
		playCardBundle.add(playCard); // �ո� ī�� ��ġ�� ������ ī�带 �߰�
		playerBundle.remove(r); // ī�� ��ġ���� ������ ī�� ����
		return playCard;
	}

	public boolean draw() {
		if(playerBundle.size()>0) {
			int r = (int) (Math.random() * playerBundle.size());
			drawCard = playerBundle.get(r); // �޸� ��ġ���� �� �� ����
			playerBundle.remove(r); // ī�� ��ġ���� ������ ī�� ����
			System.out.println("in acct drawCard : " + drawCard);
			return true;
		}else
			return false;
	}

	public String toString() {
		return nick;
	}
}
