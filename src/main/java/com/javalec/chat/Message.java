package com.javalec.chat;

import com.fasterxml.jackson.databind.JsonNode;

public class Message {
	//����ڿ��� �߼۵� �޽��� �ؽ�Ʈ(�ִ� 1000��)
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
