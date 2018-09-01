package com.javalec.chat;

import com.fasterxml.jackson.databind.JsonNode;

public class Message {
	//사용자에게 발송될 메시지 텍스트(최대 1000자)
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
