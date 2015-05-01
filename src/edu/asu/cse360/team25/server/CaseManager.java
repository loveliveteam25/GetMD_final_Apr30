package edu.asu.cse360.team25.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.WeakHashMap;

public class CaseManager {

	protected int idNext = 10;

	WeakHashMap<Integer, Case> cacheByID = new WeakHashMap<Integer, Case>();
	WeakHashMap<Integer, List<Case>> cacheByPID = new WeakHashMap<Integer, List<Case>>();

	public Case createCase(int patientID, int doctorID, String symptom,
			String painLevel) {

		int id = getNextID();

		Case c = new Case(id, painLevel, symptom, patientID, doctorID, Calendar
				.getInstance().getTime().toString());

		addNewCaseToCache(c);
		addNewCaseToDatabase(c);

		return c;
	}

	public synchronized Case findCaseByID(int id) {

		if (!cacheByID.containsKey(id)) {
			// not in cache, query the database
			Case one = findCaseByIDFromDatabase(id);
			if (one != null) {

				cacheByID.put(id, one);
			} else {

				// no such user
				return null;
			}

		}

		Case c = cacheByID.get(id);
		return c;
	}

	public synchronized List<Case> listCasesByPatientID(int patientID) {

		// list all the cases for the given patient denoted by the patientID.
		if (!cacheByPID.containsKey(patientID)) {
			// not in cache, query the database
			List<Case> ones = findCasesByPatientIDFromDatabase(patientID);
			if (ones != null) {

				cacheByPID.put(patientID, ones);
			} else {

				// no such user
				return null;
			}

		}

		List<Case> cs = cacheByPID.get(patientID);
		return cs;
	}

	public boolean markCaseProcessing(int id) {

		Case c = findCaseByID(id);
		if (c != null) {
			c.markProcessing();
			return true;
		} else {
			return false;
		}
	}

	public boolean suspendCase(int id) {

		Case c = findCaseByID(id);
		if (c != null) {
			c.markSuspended();
			return true;
		} else {
			return false;
		}
	}

	public boolean finishCase(int id, String finalDiagnose) {

		Case c = findCaseByID(id);
		if (c != null) {
			c.markFinished();
			c.setFinalDiagnose(finalDiagnose);
			return true;
		} else {
			return false;
		}
	}

	public boolean linkRefCase(int caseID, int refID) {

		Case c = findCaseByID(caseID);
		Case ref = findCaseByID(refID);
		if (c != null && ref != null) {
			c.linkCase(refID);
			return true;
		} else {
			return false;
		}
	}

	// CAUTION: We are unable to verify refID here, make sure refID is valid
	// before call this function.
	public boolean linkRefLabMeasurement(int caseID, int refID) {

		Case c = findCaseByID(caseID);
		if (c != null) {
			c.linkLabMeasurement(refID);
			return true;
		} else {
			return false;
		}
	}

	protected synchronized void addNewCaseToCache(Case c) {

		if (c == null)
			return;

		cacheByID.put(c.getCaseID(), c);
		List<Case> cs = cacheByPID.get(c.getPatientID());
		if (cs != null) {
			if(!cs.contains(c))
				cs.add(c);
		}

	}
	
	// CAUTION: only used for dummy cases
	protected synchronized void addNewCaseToPIDCache(Case c) {

		if (c == null)
			return;

		List<Case> cs = cacheByPID.get(c.getPatientID());
		if (cs != null) {
			if(!cs.contains(c))
				cs.add(c);
		} else {
			cs = new ArrayList<Case>();
			cs.add(c);
			cacheByPID.put(c.getPatientID(), cs);
		}

	}

	protected synchronized void addNewCaseToDatabase(Case c) {

	}

	protected synchronized void updateCaseInDatabase(Case cd) {

	}

	protected synchronized Case findCaseByIDFromDatabase(int id) {

		return null;
	}

	protected synchronized List<Case> findCasesByPatientIDFromDatabase(
			int patientID) {

		return null;
	}

	protected synchronized int getNextID() {

		return idNext++;
	}

	public void setupDummyCase() {

		Case[] cs = new Case[6];

		String dataTime = Calendar.getInstance().getTime().toString();

		cs[0] = new Case(0, "Unknown", "Headache", 0, 0, dataTime);

		cs[1] = new Case(1, "Unknown", "Liveache", 0, 1, dataTime);

		cs[2] = new Case(2, "Unknown", "Stomache", 0, 0, dataTime);

		cs[3] = new Case(3, "Unknown", "Lungache", 1, 1, dataTime);

		cs[4] = new Case(4, "Unknown", "Allache", 1, 2, dataTime);

		cs[5] = new Case(5, "Unknown", "Homesick", 1, 3, dataTime);

		
		
		cs[2].markSuspended();
		
		for(int i = 0; i < cs.length; i++) {
			addNewCaseToCache(cs[i]);
			addNewCaseToPIDCache(cs[i]);
		}
	}

}
