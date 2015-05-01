package edu.asu.cse360.team25.protocol;

import java.util.Calendar;

import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;

public class CaseInfo {

	protected int caseID;

	protected String painLevel;
	protected String symptom;

	protected int patientID;
	protected int doctorID;

	protected String dateTime;
	protected String refCases;
	protected String refLabMeasurements;
	protected String finalDiagnose;

	protected CaseState state;

	protected static enum CaseState {
		INIT, PROCESSING, SUSPENDED, FINISHED;

		public static CaseState parseCaseState(String str) {
			if (str.equals("INIT"))
				return INIT;
			else if (str.equals("PROCESSING"))
				return PROCESSING;
			else if (str.equals("SUSPENDED"))
				return SUSPENDED;
			else if (str.equals("FINISHED"))
				return FINISHED;
			else
				return null;
		}
	}

	protected CaseInfo(int caseID, String painLevel, String symptom,
			int patientID, int doctorID, String dateTime) {
		super();
		this.caseID = caseID;
		this.painLevel = painLevel;
		this.symptom = symptom;
		this.patientID = patientID;
		this.doctorID = doctorID;
		this.dateTime = dateTime;
		refCases = "";
		refLabMeasurements = "";
		finalDiagnose = "";

		state = CaseState.INIT;
	}

	public CaseInfo(String str) throws InvalidDataRecordException {

		String[] strs = str.split("[$]");
		if (strs.length != 10) {
			throw new InvalidDataRecordException(
					"Invalid number of fields in case info record!!!"
							+ "strs.length = " + strs.length + "record = "
							+ str);
		}

		int cid = 0;
		try {
			cid = Integer.parseInt(strs[0]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid case ID in case info record!!! record = " + str);
		}

		int pid = 0;
		try {
			pid = Integer.parseInt(strs[3]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid patient ID in case info record!!! record = " + str);
		}

		int did = 0;
		try {
			did = Integer.parseInt(strs[4]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException(
					"Invalid doctor ID in case info record!!! record = " + str);
		}

		caseID = cid;
		painLevel = strs[1];
		symptom = strs[2];
		patientID = pid;
		doctorID = did;
		dateTime = strs[5];
		refCases = strs[6];
		refLabMeasurements = strs[7];
		finalDiagnose = strs[8];
		state = CaseState.parseCaseState(strs[9]);
		if (state == null) {
			throw new InvalidDataRecordException(
					"Invalid state in case info record!!! record = " + str);
		}

	}

	public int getCaseID() {
		return caseID;
	}

	public String getPainLevel() {
		return painLevel;
	}

	public String getSymptom() {
		return symptom;
	}

	public int getPatientID() {
		return patientID;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getRefCases() {
		return refCases;
	}

	public String getRefLabMeasurements() {
		return refLabMeasurements;
	}

	public String getFinalDiagnose() {
		return finalDiagnose;
	}

        public String getState() {
            return state.toString();
        }
        
	public boolean isInitial() {
		return state == CaseState.INIT;
	}

	public boolean isInProcessing() {
		return state == CaseState.PROCESSING;
	}

	public boolean isSuspended() {
		return state == CaseState.SUSPENDED;
	}

	public boolean isFinished() {
		return state == CaseState.FINISHED;
	}

	@Override
	public String toString() {
		return caseID + "$" + painLevel + "$" + symptom + "$" + patientID + "$"
				+ doctorID + "$" + dateTime + "$" + refCases + "$"
				+ refLabMeasurements + "$" + finalDiagnose + "$" + state;
	}

	public static void main(String[] args) {

		try {

			String dataTime = Calendar.getInstance().getTime().toString();

			CaseInfo c = new CaseInfo(0, "Unknown", "Headache", 0, 0, dataTime);

			String cstr = c.toString();

			CaseInfo cRebuild = new CaseInfo(cstr);

			System.out.println(c);
			System.out.println(cRebuild);
			
			
			
			
		} catch (InvalidDataRecordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
