package common;

import java.io.Serializable;

public class Message implements Serializable{
	String talker;
	String ment;
	long time; // System.currentTimeMillis(); �� ����� ���ѵ���.
	
	public Message(String talker, String ment) {
		this.talker = talker;
		this.ment = ment;
	}
	
	public String toString() {
		return "[" + talker + "]" + ment;
	}
}
