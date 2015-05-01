package edu.asu.cse360.team25.protocol;

import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;

public class LabMeasurementInfo {

	protected int labMeasurementID;

	protected String type;
	protected String content;
	protected String dateTime;
	
	protected int patientID;
	protected int nurseID;
	
	
	
	protected LabMeasurementInfo(int labMeasurementID, String type, String content, String dateTime, int patientID,
			int nurseID) {
		super();
		this.labMeasurementID = labMeasurementID;
		this.type = type;
		this.content = content;
		this.dateTime = dateTime;
		this.patientID = patientID;
		this.nurseID = nurseID;
	}

	public LabMeasurementInfo(String str) throws InvalidDataRecordException {
		
		String[] strs = str.split("[$]");
		
		if(strs.length != 6) {
			throw new InvalidDataRecordException("Invalid number of fields in lab measurement info record!!! record = " + str);
		}

		int lid = 0;
		try {
			lid = Integer.parseInt(strs[0]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException("Invalid lab measurement ID in lab measurement info record!!! record = " + str);
		}
		
		int pid = 0;
		try {
			pid = Integer.parseInt(strs[4]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException("Invalid patient ID in lab measurement info record!!! record = " + str);
		}
		
		int nid = 0;
		try {
			nid = Integer.parseInt(strs[4]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException("Invalid nurse ID in lab measurement info record!!! record = " + str);
		}

		this.labMeasurementID = lid;
		this.type = strs[1];
		this.content = strs[2];
		this.dateTime = strs[3];
		this.patientID = pid;
		this.nurseID = nid;
		
	}
	
	public int getLabMeasurementID() {
		return labMeasurementID;
	}

	public String getType() {
		return type;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getContent() {
		return content;
	}

	public int getPatientID() {
		return patientID;
	}

	public int getNurseID() {
		return nurseID;
	}

	@Override
	public String toString() {
		
		return labMeasurementID + "$" + type + "$" + content + "$" + dateTime + "$" + patientID + "$" + nurseID;
	}

}
