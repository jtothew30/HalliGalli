package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Serializable {
	public List<String> bundle; // 최초 카드 뭉치
	public List<Account> ac; // 게임방에 들어온 계정들을 모으는 것으로 변경
	public boolean flag;
	
	public GameEngine(List<Account> ac) { //
		this.ac = ac;
		bundle = new ArrayList<>();
		flag=false;
		for (int i = 1; i <= 5; i++) { // 카드 뭉치 생성
			for (int j = 0; j < 6 - i; j++) {
				bundle.add("banana#" + i);
				bundle.add("lime#" + i);
				bundle.add("berry#" + i);
				bundle.add("plum#" + i);
			}
		}
	}

	public void start() {
		flag=true;
		setting();
	}

	public void setting() { // 카드 뭉치를 20장씩 3명에게 분배
		List<String> b1 = new ArrayList<>();
		List<String> b2 = new ArrayList<>();
		List<String> b3 = new ArrayList<>();

		while (b1.size() < 20) {
			int r = (int) (Math.random() * bundle.size());
			b1.add(bundle.get(r));
			bundle.remove(r);
		}
		while (b2.size() < 20) {
			int r = (int) (Math.random() * bundle.size());
			b2.add(bundle.get(r));
			bundle.remove(r);
		}
		while (b3.size() < 20) {
			int r = (int) (Math.random() * bundle.size());
			b3.add(bundle.get(r));
			bundle.remove(r);
		}

		ac.get(0).setPlayerBundle(b1);
		ac.get(1).setPlayerBundle(b2);
		ac.get(2).setPlayerBundle(b3);
		
		for(Account a : ac) {
			a.drawCard="";
			a.playCard="";
			a.dead=false;
		}

		System.out.println(ac);
	}

	public String play(Account ac) {
		System.out.println(ac.nick + "의 차례");
		String card = ac.play();
		return card;
	}

	public boolean compare() {// 테이블에 나와 있는 카드 비교
		int cntBanana = 0;
		int cntLime = 0;
		int cntBerry = 0;
		int cntPlum = 0;

		List<String[]> c = new ArrayList<>(); // 현재 앞으로 뒤집은 카드들
		for (Account a : ac) {
			String[] cc = a.playCard.split("#");
			c.add(cc);
		}

		for (String[] s : c) { // 각 카드들의 과일 숫자 카운팅
			switch (s[0]) {
			case "banana":
				cntBanana += Integer.parseInt(s[1]);
				break;
			case "lime":
				cntLime += Integer.parseInt(s[1]);
				break;
			case "berry":
				cntBerry += Integer.parseInt(s[1]);
				break;
			case "plum":
				cntPlum += Integer.parseInt(s[1]);
				break;
			}
		}

		if (cntBanana == 5 || cntLime == 5 || cntBerry == 5 || cntPlum == 5) { // 합계 5가 되는 것 확인
			return true;
		} else
			return false;
	}

	public boolean push(Account pusher) { // 종을 누른 사람
		if (compare()) { // 만약 합계 5가 되는 과일이 있다면,
			List<String> b = new ArrayList<>();

			for (Account acc : ac) { // 앞면 카드 뭉치들을 모두 모아
				b.addAll(acc.playCardBundle);
				acc.playCardBundle.clear();
				acc.playCard = "";
			}
			pusher.playerBundle.addAll(b); // 종을 누른 사람의 뒷면 뭉치로 합침.
			System.out.println(pusher.nick + "의 도전! 성공!");
			return true;
		} else {
			penalty(pusher);
			System.out.println(pusher.nick + "의 도전! 실패..");
			return false;
		}
	}

	public void penalty(Account pusher) {
		int idx = ac.indexOf(pusher);
		
		ac.remove(pusher); // 잘못 누른 사람을 제외한 나머지 사람들
		boolean check=true;	// 카드를 나눠줄 수 있는가 확인
		for (Account cc : ac) { // 각 사람들에게 1장씩 분배
			System.out.println(cc.nick + "생존여부:"+cc.dead);
			if(!cc.dead) { // 이미 죽은 사람에게는 분배 X
				check = pusher.draw();
				if(check) {
					String card = pusher.drawCard;
					System.out.println("패널티로 준 카드"+card);
					cc.playerBundle.add(card);
				}
			}
		}
		
		ac.add(idx, pusher);		
	}

	public void show() {
		String front = "";
		for (Account c : ac) {
			if (!c.playCard.equals(""))
				front += c.playCard + " ";
			else
				front += "  ";
		}
		System.out.println(front);
		for (Account c : ac) {
			System.out.print("[" + c.nick + "] 뒷뭉치:" + c.playerBundle.size() + "/앞뭉치" + c.playCardBundle.size() + "  ");
		}
		System.out.println("\n");
	}
	
	

	/*
	 * public String toString() { String str = ""; int cnt = 0; for(String s :
	 * bundle) { str += s+" "; cnt++; if(cnt==5) { str += "\n"; cnt=0; } } return
	 * str; }
	 */

}
