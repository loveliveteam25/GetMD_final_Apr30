package edu.asu.cse360.team25.protocol;

import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;



public class DoctorInfo {

	protected int doctorID;
	protected String name;
	
	protected String department;
	protected String expertise;
	
	protected int rate;
	
	protected DoctorState state;

	public static enum DoctorState {
		OFFLINE, FREE, BUSY;
		
		public static DoctorState parseCaseState(String str) {
			if(str.equals("OFFLINE"))
				return OFFLINE;
			else if(str.equals("FREE"))
				return FREE;
			else if(str.equals("BUSY"))
				return BUSY;
			else
				return null;
		}
	}
	
	protected DoctorInfo(int doctorID, String name, String department, String expertise) {
		super();
		this.doctorID = doctorID;
		this.name = name;
		this.department = department;
		this.expertise = expertise;
		rate = 0;
		state = DoctorState.OFFLINE;
	}

	public DoctorInfo(String str) throws InvalidDataRecordException {
		
		String[] strs = str.split("[$]");
		if(strs.length != 6) {
			throw new InvalidDataRecordException("Invalid number of fields in doctor info record!!! record = " + str);
		}
		
		int id = 0;
		try {
			id = Integer.parseInt(strs[0]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException("Invalid doctor ID in doctor info record!!! record = " + str);
		}

		int rate = 0;
		try {
			rate = Integer.parseInt(strs[4]);
		} catch (NumberFormatException e) {
			throw new InvalidDataRecordException("Invalid rate in doctor info record!!! record = " + str);
		}

		DoctorState state = DoctorState.parseCaseState(strs[5]);
		if(state == null)
			throw new InvalidDataRecordException("Invalid state in doctor info record!!! record = " + str);

		doctorID = id;
		
		name = strs[1];
		department = strs[2];
		expertise = strs[3];
		this.rate = rate;
		this.state = state;
		
	}
	
	public int getDoctorID() {
		return doctorID;
	}

	public String getName() {
		return name;
	}

	public String getDepartment() {
		return department;
	}

	public String getExpertise() {
		return expertise;
	}

	public int getRate() {
		return rate;
	}

	public String getState() {
		return state.toString();
	}

	@Override
	public String toString() {
		return doctorID + "$" + name + "$" + department + "$" + expertise
				+ "$" + rate + "$" + state;
	}

}
