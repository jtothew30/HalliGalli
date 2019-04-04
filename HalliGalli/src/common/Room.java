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
		titles = new String[] { "페어플레이합시다.", "매너게임", "일단 들어오세요.", "스피드 한게임" };
		
	}

	public String title; // 방제목

	public List<Account> joiner;
	
	public GameEngine engine;

	public Room(Account owner, String title) { // 생성자
		joiner = new Vector<>();
		joiner.add(owner);
		
		
		this.title = title;
		

	}

	public Room(Account owner) { // 생성자 
		this(owner, titles[(int) (Math.random() * titles.length)]);
	}

	public boolean enter(Account acc) { // 방 안에 사람이 들어오는 거
		if (joiner.size() < 3) {
			joiner.add(acc);
			return true;
		} else {
			return false;
		}

	}

	public boolean leave(Account acc) { // 방 안의 사람이 나가는거
		return joiner.remove(acc);
	}

	public boolean isEmpty() { // 방 안에 사람이 없으면 true, 있으면 false
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
		return "【" + title + " | 방장:" + creater.nick + " | 참가인원:" + joiner.size() + "/ 3】 ";
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
