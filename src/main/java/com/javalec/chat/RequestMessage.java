package com.javalec.chat;

public class RequestMessage {
	//메시지를 발송한 유저 식별 키
	private String user_key;

	//text, pthoto
	private String type;
	
	//자동응답 명령어의 메시지 텍스트 혹은 미디어 파일 uri
	private String content;

	public String getUser_key() {
		return user_key;
	}

	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
