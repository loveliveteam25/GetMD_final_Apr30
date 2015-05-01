package edu.asu.cse360.team25.server;

import edu.asu.cse360.team25.protocol.LabMeasurementInfo;

public class LabMeasurement extends LabMeasurementInfo {

	protected LabMeasurement(int labMeasurementID, String type, String content,
			String dateTime, int patientID, int nurseID) {
		super(labMeasurementID, type, content, dateTime, patientID, nurseID);
	}

	
	
	
	
}
