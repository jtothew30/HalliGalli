package client.thread;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class HomeThread extends Thread {
	
	public Clip clip;

	public HomeThread() {		

	}

	@Override
	public void run() {		
		try {
			//File file = new File("src/audio/나지막히 부르다-리리에%28RiRie%29 (online-audio-converter.com).wav");
			InputStream is = getClass().getClassLoader().getResourceAsStream("audio/발랄3 (online-audio-converter.com).wav");
			System.out.println(is.toString());
			AudioInputStream stream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			clip = AudioSystem.getClip();							
			clip.open(stream);
			clip.start();	
			clip.loop(1000);											
		} catch (Exception e) {
			System.out.println("??" + e.getMessage());
		}
	}	
}
