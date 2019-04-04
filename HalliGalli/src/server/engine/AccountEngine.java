package server.engine;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import common.Account;

public class AccountEngine {
	
	static Map<String, Account> all;
	static List<Account> current;
	
	static {
		all = new HashMap<String, Account>();
	}
	
	public boolean join(String nick, String pass, String em) {
		if(all.containsKey(nick) ) {
			return false;
		}else {
			all.put(nick, new Account(nick, pass, em));
			return true;
		}
	}
	
	public boolean auth(String nick, String pass) {
		if(all.containsKey(nick) && all.get(nick).getPass().equals(pass)) {
			
			return true;
		}else {
			return false;
		}
	}
	
}
