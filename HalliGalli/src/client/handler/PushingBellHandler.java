package client.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import client.ClientUI;

public class PushingBellHandler implements KeyListener{
	ClientUI ui;
	public Push p;
	public BellThread b;
	
	public PushingBellHandler(ClientUI ui){
		this.ui=ui;
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("keyPressed" + e.getKeyCode());
			if(e.getKeyCode()==32) {
				
				b = new BellThread(ui);
				b.start();
				System.out.println("Bell.start");
				
				
				p = new Push(ui);
				p.start();
				System.out.println("p.start");
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public class Push extends Thread{
		ClientUI ui;
		Push(ClientUI ui){
			this.ui = ui;
		}
		
		public void run() {
			if(ui.net.flag) {
				ui.net.pushRequest();
			}else
				JOptionPane.showMessageDialog(ui, "아직 게임이 시작되지 않았습니다!");
		}
	}
	
	public class BellThread extends Thread{
		ClientUI ui;
		
		public BellThread(ClientUI ui) {
			this.ui = ui;
		}
		
		@Override
		public void run() {
			try {
				//AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src\\audio\\big_swish_with_ding (online-audio-converter.com).wav"));
				InputStream is = getClass().getClassLoader().getResourceAsStream("audio/big_swish_with_ding (online-audio-converter.com).wav");
				AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
				Clip clip = AudioSystem.getClip();
				clip.open(ais);
				clip.start();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}


