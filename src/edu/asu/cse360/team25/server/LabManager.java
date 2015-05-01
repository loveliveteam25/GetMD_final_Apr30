package edu.asu.cse360.team25.server;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class LabManager {

	protected int idNext = 10;

	WeakHashMap<Integer, LabMeasurement> cacheByID = new WeakHashMap<Integer, LabMeasurement>();
	WeakHashMap<Integer, List<LabMeasurement>> cacheByPID = new WeakHashMap<Integer, List<LabMeasurement>>();

	public LabMeasurement createLabMeasurementByID(String type, String content,
			String dateTime, int patientID, int nurseID) {

		int id = getNextID();

		LabMeasurement lm = new LabMeasurement(id, type, content, dateTime,
				patientID, nurseID);

		addNewLabMeasurementToCache(lm);
		addNewLabMeasurementToDatabase(lm);

		return null;
	}

	public synchronized LabMeasurement findLabMeasurementByID(int id) {

		if (!cacheByID.containsKey(id)) {
			// not in cache, query the database
			LabMeasurement one = findLabMeasurementByIDFromDatabase(id);
			if (one != null) {

				cacheByID.put(id, one);
			} else {

				// no such user
				return null;
			}

		}

		LabMeasurement lm = cacheByID.get(id);
		return lm;
	}

	public synchronized List<LabMeasurement> findAllLabMeasurementOfGivenPatient(
			int patientID) {

		if (!cacheByPID.containsKey(patientID)) {
			// not in cache, query the database
			List<LabMeasurement> ones = findLabMeasurementsByPatientIDFromDatabase(patientID);
			if (ones != null) {

				cacheByPID.put(patientID, ones);
			} else {

				// no such lab measurement
				return null;
			}

		}

		List<LabMeasurement> lms = cacheByPID.get(patientID);
		return lms;
	}

	protected synchronized void addNewLabMeasurementToCache(LabMeasurement lm) {

		if (lm == null)
			return;

		cacheByID.put(lm.getLabMeasurementID(), lm);
		List<LabMeasurement> lms = cacheByPID.get(lm.getPatientID());
		if (lms != null) {
			if (!lms.contains(lm))
				lms.add(lm);
		}

	}
	
	// CAUTION: only used for dummy lab measurements
	protected synchronized void addNewLabMeasurementToPIDCache(LabMeasurement lm) {

		if (lm == null)
			return;

		List<LabMeasurement> lms = cacheByPID.get(lm.getPatientID());
		if (lms != null) {
			if (!lms.contains(lm))
				lms.add(lm);
		} else {
			lms = new ArrayList<LabMeasurement>();
			lms.add(lm);
			cacheByPID.put(lm.getPatientID(), lms);
			
		}

	}

	protected synchronized void addNewLabMeasurementToDatabase(LabMeasurement lm) {

	}

	protected synchronized void updateLabMeasurementInDatabase(LabMeasurement lm) {

	}

	protected synchronized LabMeasurement findLabMeasurementByIDFromDatabase(
			int id) {

		return null;
	}

	protected synchronized List<LabMeasurement> findLabMeasurementsByPatientIDFromDatabase(
			int patientID) {

		return null;
	}

	protected synchronized int getNextID() {

		return idNext++;
	}
	
	protected void setupDummyLabMeasurement() {
		
		LabMeasurement[] lms = new LabMeasurement[10];
		
		for(int i = 0; i < 10; i++) {
			lms[i] = new LabMeasurement(i, "some type", "" + i, "some date", i/2, 0);
			addNewLabMeasurementToCache(lms[i]);
			addNewLabMeasurementToPIDCache(lms[i]);
		}
	}
}
