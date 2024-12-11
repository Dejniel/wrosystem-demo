package pl.wtrymiga.mandates.controller;

import lombok.Data;

@Data
public class Message {
	private final String message;
	private final MessageType type;

	public enum MessageType {
		primary, secondary, success, danger, warning, info, light, dark
	}
}
