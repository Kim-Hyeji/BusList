package com.javalec.chat;

public class RequestMessage {
	//�޽����� �߼��� ���� �ĺ� Ű
	private String user_key;

	//text, pthoto
	private String type;
	
	//�ڵ����� ��ɾ��� �޽��� �ؽ�Ʈ Ȥ�� �̵�� ���� uri
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
