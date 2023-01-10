package kr.jiSocket.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;


// @ServerEndpoint는 WebSocket을 활성화시키는 매핑 정보를 지정
@Service
@ServerEndpoint(value="/chatt")
public class WebSocketClient {
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	
	/**
	 * 클라이언트가 접속할 때 발생하는 이벤트
	 * 
	 * @param s
	 */
	@OnOpen
	public void onOpen(Session s) {
		System.out.println("open session : " + s.toString());
		if(!clients.contains(s)) {
			clients.add(s);
			System.out.println("session open : " + s);
		} else {
			System.out.println("이미 연결된 session ! !");
		}
	}

	/**
	 * 메세지 수신 되었을 때
	 * 
	 * @param msg
	 * @param session
	 * @throws Exception
	 */
	@OnMessage
	public void onMessage(String msg, Session session) throws Exception {
		System.out.println("receive message : " + msg);
		for(Session s : clients) {
			System.out.println("send data : " + msg);
			s.getBasicRemote().sendText(msg);
		}
	}
	
	/**
	 * 클라이언트가 브라우저를 끄거나 다른 경로로 이동할 때
	 * 
	 * @param s
	 */
	@OnClose
	public void onClose(Session s) {
		System.out.println("session close : " + s);
		clients.remove(s);
	}
}
