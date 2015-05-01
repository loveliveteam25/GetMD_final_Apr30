package edu.asu.cse360.team25.server;

import edu.asu.cse360.team25.protocol.PatientInfo;

public class Patient extends PatientInfo {


	public Patient(int patientID, String password, String name, String gender,
			String height, String weight, String birthday) {
		super(patientID, password, name, gender, height, weight, birthday);
	}



	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}



}
