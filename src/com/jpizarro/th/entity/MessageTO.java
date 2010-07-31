package com.jpizarro.th.entity;

public class MessageTO {
	private long messageId;
	private String senderLogin;
	private String receiverLogin;
	private String messageBody;
	private int type;
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	public String getSenderLogin() {
		return senderLogin;
	}
	public void setSenderLogin(String senderLogin) {
		this.senderLogin = senderLogin;
	}
	public String getReceiverLogin() {
		return receiverLogin;
	}
	public void setReceiverLogin(String receiverLogin) {
		this.receiverLogin = receiverLogin;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
