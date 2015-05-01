package edu.asu.cse360.team25.protocol;

import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;

public class ChatInfo {

	protected int chatID;

	protected int caseID;

	protected int patientID;
	protected int doctorID;

	// True means from patient to doctor; false means from doctor to patient.
	public static final boolean PATIENT_TO_DOCTOR = true;
	public static final boolean DOCTOR_TO_PATIENT = false;
	protected boolean direction;

	protected String message;

	protected ChatInfo(int chatID, int caseID, int patientID, int doctorID,
			boolean direction, String message) {
		super();
		this.chatID = chatID;
		this.caseID = caseID;
		this.patientID = patientID;
		this.doctorID = doctorID;
		this.direction = direction;
		this.message = message;
	}

	public ChatInfo(String str) throws InvalidDataRecordException {

		String[] strs = new String[6];
		for (int i = 0; i < 5; i++) {
			int mark = str.indexOf('$');
			if (mark < 0) {
				throw new InvalidDataRecordException(
						"Invalid number of fields in chat info record!!! record = "
								+ str);
			}
			strs[i] = str.substring(0, mark);
			str = str.substring(mark + 1);
		}
		strs[5] = str;

		int id = 0;
		try {
			id = Integer.parseInt(strs[0]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid chat ID in chat info record!!! record = " + str);
		}

		int cid = 0;
		try {
			cid = Integer.parseInt(strs[1]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid case ID in chat info record!!! record = " + str);
		}

		int pid = 0;
		try {
			pid = Integer.parseInt(strs[2]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid patient ID in chat info record!!! record = " + str);
		}

		int did = 0;
		try {
			did = Integer.parseInt(strs[3]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid doctor ID in chat info record!!! record = " + str);
		}

		boolean direction = false;
		direction = Boolean.parseBoolean(strs[4]);
		if (!strs[4].equals("true") && !strs[4].equals("false")
				&& !strs[4].equals("True") && !strs[4].equals("False")
				&& !strs[4].equals("TRUE") && !strs[4].equals("FALSE")) {
			throw new InvalidDataRecordException(
					"Invalid direction in chat info record!!! record = " + str);
		}

		// TODO: handle escaped "#" here

		this.chatID = id;
		this.caseID = cid;
		this.patientID = pid;
		this.doctorID = did;
		this.direction = direction;
		this.message = strs[5];
	}

	public int getChatID() {
		return chatID;
	}

	public int getCaseID() {
		return caseID;
	}

	public int getPatientID() {
		return patientID;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public boolean isDirection() {
		return direction;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {

		// TODO: escape "#" here

		return chatID + "$" + caseID + "$" + patientID + "$" + doctorID + "$"
				+ direction + "$" + message;
	}

	public static void main(String[] args) {

		try {
			ChatInfo c = new ChatInfo(0, 0, 0, 0, true, "adhfakfkjf");

			ChatInfo c2 = new ChatInfo(0, 0, 0, 0, false, "ertoiro");

			String cstr = c.toString();

			ChatInfo cRe = new ChatInfo(cstr);

			ChatInfo c2Re = new ChatInfo(c2.toString());

			System.out.println(cRe);
			System.out.println(c2Re);

		} catch (InvalidDataRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
