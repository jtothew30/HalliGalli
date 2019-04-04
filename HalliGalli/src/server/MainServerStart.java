package server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class MainServerStart {
	public static void main(String[] args) {
		System.out.println("[server] starting");
		long t1 = System.currentTimeMillis();
		try {
			ServerSocket server = new ServerSocket(56789); // 서버에서 개방할 포트
			long t2 = System.currentTimeMillis();
			System.out.println("[server] startup in " + (t2 - t1) + " ms");
			/*
			 * 사용자간에 공통적으로 써야되는 객체가 있을때, 이걸 메인쓰레드에서 꼭 일일이 만들어서 넘겨줘야 하냐..?
			 * 
			 * 결론은 안그래도 된다.
			 */
			while (!server.isClosed()) {
				Socket socket = server.accept(); //
				System.out.println("[server] connected by " + socket.getRemoteSocketAddress());
				Thread p = new PersonalServer(socket);
				p.start();
			}
			server.close();
		} catch (IOException e) {
			System.out.println("[server] main error : " + e.toString());
			System.out.println("[server] terminated");
			
		}
	}
}
