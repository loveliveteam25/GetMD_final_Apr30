package edu.asu.cse360.team25.server;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class PatientManager {

	protected int idNext = 14;

	protected WeakHashMap<Integer, Patient> cacheByID = new WeakHashMap<Integer, Patient>();
	protected WeakHashMap<String, List<Patient>> cacheByName = new WeakHashMap<String, List<Patient>>();

	public int registerPatient(String password, String name, String gender,
			String height, String weight, String birthday) {

		int id = getNextID();

		Patient p = new Patient(id, password, name, gender, height, weight,
				birthday);

		addNewPatientToCache(p);
		addNewPatientToDatabase(p);

		return id;
	}

	public synchronized Patient findPatientByID(int id) {

		if (!cacheByID.containsKey(id)) {
			// not in cache, query the database
			Patient one = findPatientByIDFromDatabase(id);
			if (one != null) {

				cacheByID.put(id, one);
			} else {

				// no such user
				return null;
			}

		}

		Patient p = cacheByID.get(id);
		return p;
	}

	public synchronized List<Patient> findPatientsByName(String name) {

		if (!cacheByName.containsKey(name)) {
			// not in cache, query the database
			List<Patient> ps = findPatientsByNameFromDatabase(name);
			if (ps != null) {

				cacheByName.put(name, ps);
			} else {

				// no such user
				return null;
			}

		}

		List<Patient> ps = cacheByName.get(name);
		return ps;

	}

	public boolean checkPatientLoginReuest(int id, String password) {

		Patient p = findPatientByID(id);
		if(p != null)
			return p.getPassword().equals(password);
		else
			return false;

	}

	public boolean updatePatientProfile(int patientID, String name,
			String gender, String height, String weight, String birthday) {

		Patient p = cacheByID.get(patientID);
		if (p != null) {
			p.setName(name);
			p.setGender(gender);
			p.setHeight(height);
			p.setWeight(weight);
			p.setBirthday(birthday);
			
			updatePatientInDatabase(p);

			return true;
		} else {
			return false;
		}

	}

	public boolean updatePassword(int patientID, String pwOld, String pwNew) {

		// TODO:

		return false;
	}

	public boolean markPatientLogin(int patientID) {

		// TODO:

		return true;
	}

	public boolean markPatientLogout(int patientID) {

		// TODO:

		return true;
	}

	protected synchronized void addNewPatientToCache(Patient p) {

		if (p == null)
			return;

		cacheByID.put(p.getPatientID(), p);
		List<Patient> ps = cacheByName.get(p.getName());
		if (ps != null) {
			ps.add(p);
		}

	}
	
	// CAUTION: only used for dummy patients
	protected synchronized void addNewPatientToNameCache(Patient p) {
		
		List<Patient> ps = cacheByName.get(p.getName());
		if (ps != null) {
			if(!ps.contains(p))
				ps.add(p);
		} else {
			ps = new ArrayList<Patient>();
			cacheByName.put(p.getName(), ps);
		}

	}

	protected synchronized void addNewPatientToDatabase(Patient p) {

	}

	protected synchronized void updatePatientInDatabase(Patient p) {

	}

	protected synchronized Patient findPatientByIDFromDatabase(int id) {

		return null;
	}

	protected synchronized List<Patient> findPatientsByNameFromDatabase(
			String name) {

		return null;
	}

	protected synchronized int getNextID() {

		return idNext++;
	}

	public void setupDummyPatient() {

		Patient[] p = new Patient[14];

		p[0] = new Patient(0, "123456", "Amami Haruka", "Female", "158", "46",
				"Apr. 3");
		p[1] = new Patient(1, "123456", "Kisaragi Chihaya", "Female", "162",
				"41", "Feb. 25");
		p[2] = new Patient(2, "123456", "Hagiwara Yukiho", "Female", "155",
				"42", "Dec. 24");
		p[3] = new Patient(3, "123456", "Takatsuki Yayoyi", "Female", "145",
				"37", "Mar. 25");
		p[4] = new Patient(4, "123456", "Miura Azusa", "Female", "168", "48",
				"Jul. 19");
		p[5] = new Patient(5, "123456", "Minase Iori", "Female", "153", "40",
				"May. 5");
		p[6] = new Patient(6, "123456", "Kikuchi Makoto", "Female", "159",
				"44", "Aug. 29");
		p[7] = new Patient(7, "123456", "Futami Ami", "Female", "158", "42",
				"May. 22");
		p[8] = new Patient(8, "123456", "Futami Mami", "Female", "158", "42",
				"May. 22");
		p[9] = new Patient(9, "123456", "Hoshii Miki", "Female", "161", "45",
				"Nov. 23");
		p[10] = new Patient(9, "123456", "Ganaha Hibiki", "Female", "152",
				"41", "Oct. 10");
		p[11] = new Patient(9, "123456", "Shijyou Takane", "Female", "169",
				"49", "Jan. 21");
		p[12] = new Patient(9, "123456", "Akiduki Ritsuko", "Female", "156",
				"43", "Jun. 23");
		p[13] = new Patient(9, "123456", "Otonashi Kotori", "Female", "159",
				"49", "Sep. 9");

		for (int i = 0; i < p.length; i++) {
			addNewPatientToCache(p[i]);
			addNewPatientToNameCache(p[i]);
		}

	}
}
