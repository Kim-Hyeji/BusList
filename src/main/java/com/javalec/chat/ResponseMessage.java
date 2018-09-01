package com.javalec.chat;

public class ResponseMessage {
	//자동응답 명령어에 대한 응답 메시지의 내용
	private Message message;
	//키보드 영역에 표현될 명령어 버튼에 대한 정보
	private KeyboardInfo keyboard;
	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public KeyboardInfo getKeyboard() {
		return keyboard;
	}
	public void setKeyboard(KeyboardInfo keyboard) {
		this.keyboard = keyboard;
	}
	
	

}
