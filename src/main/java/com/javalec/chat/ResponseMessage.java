package com.javalec.chat;

public class ResponseMessage {
	//�ڵ����� ��ɾ ���� ���� �޽����� ����
	private Message message;
	//Ű���� ������ ǥ���� ��ɾ� ��ư�� ���� ����
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
