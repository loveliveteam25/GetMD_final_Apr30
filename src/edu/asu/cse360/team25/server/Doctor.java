package edu.asu.cse360.team25.server;

import edu.asu.cse360.team25.protocol.DoctorInfo;

public class Doctor extends DoctorInfo {
	
	protected String password;
	protected int rateTotal;
	protected int rateNum;

	// Doctor object can only be constructed by DoctorManager
	public Doctor(int doctorID, String name, String department, String expertise, String password) {
		super(doctorID, name, department, expertise);
		
		this.password = password;
		rate = 0;
		rateTotal = 0;
		rateNum = 0;

	}
	protected Doctor(int doctorID, String name, String department, String expertise, String password, int rate, int rateTotal, int rateNum) {
		super(doctorID, name, department, expertise);
		
		this.password = password;
		this.rate = rate;
		this.rateTotal = rateTotal;
		this.rateNum = rateNum;
	}

	public String getPassword() {
		return password;
	}

	public boolean isOnline() {
		
		return state == DoctorState.FREE || state == DoctorState.BUSY;
	}
	
	public boolean isFree() {
		
		return state == DoctorState.FREE;
	}
	
	// state can only be changed by doctor manager
	
	protected void markOnline() {
		
		state = DoctorState.FREE;
	}
	
	protected void markOffline() {
		
		state = DoctorState.OFFLINE;
	}

	protected void markBusy() {
		
		state = DoctorState.BUSY;
	}

	protected void markFree() {
		
		state = DoctorState.FREE;
	}

	protected void addRate(int rate) {
		
		rateNum++;
		rateTotal += rate;
		rate = rateTotal / rateNum;
	}
}
