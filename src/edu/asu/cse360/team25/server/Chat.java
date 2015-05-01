package edu.asu.cse360.team25.server;

import edu.asu.cse360.team25.protocol.ChatInfo;

public class Chat extends ChatInfo {

	protected Chat(int chatID, int caseID, int patientID, int doctorID,
			boolean direction, String message) {
		super(chatID, caseID, patientID, doctorID, direction, message);
	}



	
	
}
