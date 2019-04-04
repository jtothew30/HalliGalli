package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Serializable {
	public List<String> bundle; // ���� ī�� ��ġ
	public List<Account> ac; // ���ӹ濡 ���� �������� ������ ������ ����
	public boolean flag;
	
	public GameEngine(List<Account> ac) { //
		this.ac = ac;
		bundle = new ArrayList<>();
		flag=false;
		for (int i = 1; i <= 5; i++) { // ī�� ��ġ ����
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

	public void setting() { // ī�� ��ġ�� 20�徿 3���� �й�
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
		System.out.println(ac.nick + "�� ����");
		String card = ac.play();
		return card;
	}

	public boolean compare() {// ���̺� ���� �ִ� ī�� ��
		int cntBanana = 0;
		int cntLime = 0;
		int cntBerry = 0;
		int cntPlum = 0;

		List<String[]> c = new ArrayList<>(); // ���� ������ ������ ī���
		for (Account a : ac) {
			String[] cc = a.playCard.split("#");
			c.add(cc);
		}

		for (String[] s : c) { // �� ī����� ���� ���� ī����
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

		if (cntBanana == 5 || cntLime == 5 || cntBerry == 5 || cntPlum == 5) { // �հ� 5�� �Ǵ� �� Ȯ��
			return true;
		} else
			return false;
	}

	public boolean push(Account pusher) { // ���� ���� ���
		if (compare()) { // ���� �հ� 5�� �Ǵ� ������ �ִٸ�,
			List<String> b = new ArrayList<>();

			for (Account acc : ac) { // �ո� ī�� ��ġ���� ��� ���
				b.addAll(acc.playCardBundle);
				acc.playCardBundle.clear();
				acc.playCard = "";
			}
			pusher.playerBundle.addAll(b); // ���� ���� ����� �޸� ��ġ�� ��ħ.
			System.out.println(pusher.nick + "�� ����! ����!");
			return true;
		} else {
			penalty(pusher);
			System.out.println(pusher.nick + "�� ����! ����..");
			return false;
		}
	}

	public void penalty(Account pusher) {
		int idx = ac.indexOf(pusher);
		
		ac.remove(pusher); // �߸� ���� ����� ������ ������ �����
		boolean check=true;	// ī�带 ������ �� �ִ°� Ȯ��
		for (Account cc : ac) { // �� ����鿡�� 1�徿 �й�
			System.out.println(cc.nick + "��������:"+cc.dead);
			if(!cc.dead) { // �̹� ���� ������Դ� �й� X
				check = pusher.draw();
				if(check) {
					String card = pusher.drawCard;
					System.out.println("�г�Ƽ�� �� ī��"+card);
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
			System.out.print("[" + c.nick + "] �޹�ġ:" + c.playerBundle.size() + "/�չ�ġ" + c.playCardBundle.size() + "  ");
		}
		System.out.println("\n");
	}
	
	

	/*
	 * public String toString() { String str = ""; int cnt = 0; for(String s :
	 * bundle) { str += s+" "; cnt++; if(cnt==5) { str += "\n"; cnt=0; } } return
	 * str; }
	 */

}
