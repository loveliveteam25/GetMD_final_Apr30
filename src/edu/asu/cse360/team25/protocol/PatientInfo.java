package edu.asu.cse360.team25.protocol;

import edu.asu.cse360.team25.protocol.exception.InvalidDataRecordException;

public class PatientInfo {

    protected int patientID;
    protected String password;
    protected String name;

    protected String gender;
    protected String height;
    protected String weight;
    protected String birthday;

    public PatientInfo(int patientID, String password, String name, String gender,
            String height, String weight, String birthday) {
        super();
        this.patientID = patientID;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.birthday = birthday;
    }

    public PatientInfo(String str) throws InvalidDataRecordException {

        String[] strs = str.split("[$]");
        if (strs.length != 7) {
            throw new InvalidDataRecordException("Invalid number of fields in patient info record!!! record = " + str);
        }

        int id = 0;
        try {
            id = Integer.parseInt(strs[0]);
        } catch (NumberFormatException e) {
            throw new InvalidDataRecordException("Invalid patient ID in patient info record!!! record = " + str);
        }

        patientID = id;
        password = strs[1];
        name = strs[2];
        gender = strs[3];
        height = strs[4];
        weight = strs[5];
        birthday = strs[6];

    }

    public int getPatientID() {
        return patientID;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setBirthday(String birthDay) {
        this.birthday = birthDay;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

		// Here password is not shown.
        return patientID + "$" + "******" + "$" + name + "$" + gender + "$"
                + height + "$" + weight + "$" + birthday;
    }

}
