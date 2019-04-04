package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JOptionPane;

import client.ClientUI;
import server.PersonalServer;

public class HalliGalliPool implements Serializable{
	Map<String, Account> pool;
	File dir;
	
	public HalliGalliPool() {
		pool = new HashMap<>();
		dir = new File(System.getProperty("user.home"), "AllAccounts");
		System.out.println("getAbsolutePath : " + dir.getAbsolutePath());
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	public boolean save(Map<String, Account> allAccounts) {
		try {
			for(String key : allAccounts.keySet()) {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dir, key)));
				Account ac = allAccounts.get(key);
				oos.writeObject(ac);
				oos.close();
			}
			return true;
		}catch(NotSerializableException e) {
			System.out.println("NotSerializableException :" + e.getMessage());
			return false;
		}catch(IOException e) {
			System.out.println("IOException : " + e.getMessage());
			return false;
		}
	}

	public Map<String, Account> load() {
		Map<String, Account> map = new HashMap<>();
		File[] childs = dir.listFiles();
		try {
			for(File c: childs) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(c));
				Account ac = (Account)ois.readObject();
				pool.put(c.getName(), ac);
				ois.close();
			}
			return pool;
		}catch(IOException | ClassNotFoundException e) {
			for(File c : childs) {
				c.delete();
			}
			System.out.println("Load fail" + e.getMessage());
			return null;
		}
	}	
}

