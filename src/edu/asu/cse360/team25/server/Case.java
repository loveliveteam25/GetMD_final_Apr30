package edu.asu.cse360.team25.server;

import edu.asu.cse360.team25.protocol.CaseInfo;

public class Case extends CaseInfo {



	protected Case(int caseID, String painLevel, String symptom, int patientID,
			int doctorID, String dateTime) {
		super(caseID, painLevel, symptom, patientID, doctorID, dateTime);
	}

	protected void markProcessing() {
		
		state = CaseState.PROCESSING;
	}
	
	protected void markSuspended() {
		
		state = CaseState.SUSPENDED;
	}

	protected void markFinished() {
		
		state = CaseState.FINISHED;
	}

	protected void setFinalDiagnose(String finalDiagnose) {
		
		this.finalDiagnose = finalDiagnose;
	}
	
	protected void linkCase(int refID) {
		
		refCases = refCases + "+" + refID;
	}
	
	protected void linkLabMeasurement(int refID) {
		
		refLabMeasurements = refLabMeasurements + "+" + refID;
	}
}
