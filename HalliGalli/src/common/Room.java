package common;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class Room implements Serializable {
	static String[] titles;
	public int cnt;
	public int turnCnt1;
	public int turnCnt2;
	public int turn;
	static {
		titles = new String[] { "����÷����սô�.", "�ųʰ���", "�ϴ� ��������.", "���ǵ� �Ѱ���" };
		
	}

	public String title; // ������

	public List<Account> joiner;
	
	public GameEngine engine;

	public Room(Account owner, String title) { // ������
		joiner = new Vector<>();
		joiner.add(owner);
		
		
		this.title = title;
		

	}

	public Room(Account owner) { // ������ 
		this(owner, titles[(int) (Math.random() * titles.length)]);
	}

	public boolean enter(Account acc) { // �� �ȿ� ����� ������ ��
		if (joiner.size() < 3) {
			joiner.add(acc);
			return true;
		} else {
			return false;
		}

	}

	public boolean leave(Account acc) { // �� ���� ����� �����°�
		return joiner.remove(acc);
	}

	public boolean isEmpty() { // �� �ȿ� ����� ������ true, ������ false
		return joiner.size() == 0;
	}
	
	public void start() {
		engine = new GameEngine(joiner);
		engine.start();
		cnt = (int)(Math.random()*3);
		turn = 1;
		turnCnt1=0;
		turnCnt2=0;
	}

	public String toString() {
		Account creater = joiner.get(0);
		return "��" + title + " | ����:" + creater.nick + " | �����ο�:" + joiner.size() + "/ 3�� ";
	}
	
	public void GameReset() {
		//n++;
		//if(joiner.size()==n) {
			for(Account ac : joiner) {
				ac.dead=false;
				ac.drawCard="";
				ac.playCard="";
				ac.playCardBundle.clear();
				ac.playerBundle.clear();
			}
		//}
	}

}
