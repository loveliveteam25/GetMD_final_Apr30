package edu.asu.cse360.team25.server;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class DoctorManager {

	protected int idNext = 10;

	WeakHashMap<Integer, Doctor> cacheByID = new WeakHashMap<Integer, Doctor>();
	// cache by department and expertise
	WeakHashMap<String, List<Doctor>> cacheByDnE = new WeakHashMap<String, List<Doctor>>();

	public int registerDoctor(String name, String department, String expertise,
			String password) {

		int id = getNextID();

		Doctor d = new Doctor(id, name, department, expertise, password);

		addNewDoctorToCache(d);
		addNewDoctorToDatabase(d);

		return id;
	}

	public synchronized Doctor findDoctorByID(int id) {

		if (!cacheByID.containsKey(id)) {
			// not in cache, query the database
			Doctor one = findDoctorByIDFromDatabase(id);
			if (one != null) {

				cacheByID.put(id, one);
			} else {

				// no such user
				return null;
			}

		}

		Doctor d = cacheByID.get(id);
		return d;
	}

	public synchronized List<Doctor> listDoctors(String department, String expertise) {

		String dne = department + "+" + expertise;

		if (!cacheByDnE.containsKey(dne)) {
			// not in cache, query the database
			List<Doctor> ones = findDoctorsByDnEFromDatabase(department,
					expertise);
			if (ones != null) {

				cacheByDnE.put(dne, ones);
			} else {

				// no such user
				return null;
			}

		}

		List<Doctor> ds = cacheByDnE.get(dne);
		return ds;
	}

	public boolean checkDoctorLoginRequest(int id, String password) {

		Doctor d = findDoctorByID(id);
		if (d != null)
			return d.getPassword().equals(password);
		else
			return false;
	}

	public boolean rateDoctor(int id, int rate) {

		Doctor d = findDoctorByID(id);
		if (d != null) {
			d.addRate(rate);
			updateDoctorInDatabase(d);
			return true;
		} else {
			return false;
		}
	}

	public boolean markDoctorOnline(int id) {

		Doctor d = findDoctorByID(id);
		if (d != null) {
			d.markOnline();
			updateDoctorInDatabase(d);
			return true;
		} else {
			return false;
		}
	}

	public boolean markDoctorOffline(int id) {

		Doctor d = findDoctorByID(id);
		if (d != null) {
			d.markOffline();
			return true;
		} else {
			return false;
		}
	}

	public boolean markDoctorBusy(int id) {

		Doctor d = findDoctorByID(id);
		if (d != null) {
			d.markBusy();
			updateDoctorInDatabase(d);
			return true;
		} else {
			return false;
		}
	}

	public boolean markDoctorFree(int id) {

		Doctor d = findDoctorByID(id);
		if (d != null) {
			d.markFree();
			updateDoctorInDatabase(d);
			return true;
		} else {
			return false;
		}
	}

	protected synchronized void addNewDoctorToCache(Doctor d) {

		if (d == null)
			return;

		//
		cacheByID.put(d.getDoctorID(), d);
		//
		List<Doctor> list = cacheByDnE.get(d.getDepartment() + "+"
				+ d.getExpertise());
		if (list != null) {
			if (!list.contains(d))
				list.add(d);
		}

		// handle stars in department and expertise

		List<Doctor> listDstar = cacheByDnE.get("*+" + d.getExpertise());
		if (listDstar != null) {
			if (!listDstar.contains(d))
				listDstar.add(d);
		}

		List<Doctor> listSstar = cacheByDnE.get(d.getDepartment() + "+*");
		if (listSstar != null) {
			if (!listSstar.contains(d))
				listSstar.add(d);
		}

		List<Doctor> listStarStar = cacheByDnE.get("*+*");
		if (listStarStar != null) {
			if (!listStarStar.contains(d))
				listStarStar.add(d);
		}

	}

	// CAUTION: only used for dummy doctors
	protected synchronized void addNewDoctorToDnECache(Doctor d) {

		if (d == null)
			return;

		List<Doctor> list = cacheByDnE.get(d.getDepartment() + "+"
				+ d.getExpertise());
		if (list == null) {
			list = new ArrayList<Doctor>();
			list.add(d);
			cacheByDnE.put(d.getDepartment() + "+" + d.getExpertise(), list);
		} else {
			if (!list.contains(d))
				list.add(d);
		}

		// handle stars in department and expertise

		List<Doctor> listDstar = cacheByDnE.get("*+" + d.getExpertise());
		if (listDstar == null) {
			listDstar = new ArrayList<Doctor>();
			listDstar.add(d);
			cacheByDnE.put("*+" + d.getExpertise(), listDstar);
		} else {
			if (!listDstar.contains(d))
				listDstar.add(d);
		}

		List<Doctor> listSstar = cacheByDnE.get(d.getDepartment() + "+*");
		if (listSstar == null) {
			listSstar = new ArrayList<Doctor>();
			listSstar.add(d);
			cacheByDnE.put(d.getDepartment() + "+*", listSstar);
		} else {
			if (!listSstar.contains(d))
				listSstar.add(d);
		}

		List<Doctor> listStarStar = cacheByDnE.get("*+*");
		if (listStarStar == null) {
			listStarStar = new ArrayList<Doctor>();
			listStarStar.add(d);
			cacheByDnE.put("*+*", listStarStar);
		} else {
			if (!listStarStar.contains(d))
				listStarStar.add(d);
		}

	}

	protected synchronized void addDoctorsToCache(List<Doctor> ds) {

		if (ds == null || ds.isEmpty())
			return;

		for (Doctor d : ds) {
			addNewDoctorToCache(d);
		}

	}

	protected synchronized void addNewDoctorToDatabase(Doctor d) {

	}

	protected synchronized void updateDoctorInDatabase(Doctor d) {

	}

	protected synchronized Doctor findDoctorByIDFromDatabase(int id) {

		return null;
	}

	protected synchronized List<Doctor> findDoctorsByDnEFromDatabase(
			String department, String expertise) {

		return null;
	}

	protected synchronized int getNextID() {

		return idNext++;
	}

	public void setupDummyDoctor() {

		Doctor[] d = new Doctor[9];

		d[0] = new Doctor(0, "Kousaka Honoka", "GAME", "GTA5", "123456");
		d[1] = new Doctor(1, "Minami Kotori", "GAME", "Naruto", "123456");
		d[2] = new Doctor(2, "Sonoda Umi", "GAME", "LoveLive", "123456");
		d[3] = new Doctor(3, "Koizumi Hanayo", "Cook", "meat ball", "123456");
		d[4] = new Doctor(4, "Hoshizora Rin", "Cook", "yogurt", "123456");
		d[5] = new Doctor(5, "Nishikino Maki", "Cook", "sandwith", "123456");
		d[6] = new Doctor(6, "Ayase Eli", "Cook", "burger", "123456");
		d[7] = new Doctor(7, "Toujyou Nozomi", "Cook", "BBQ", "123456");
		d[8] = new Doctor(8, "Yazawa Niko", "Cook", "pizza", "123456");

		for (int i = 0; i < d.length; i++) {
			addNewDoctorToCache(d[i]);
			addNewDoctorToDnECache(d[i]);
		}

	}

}
