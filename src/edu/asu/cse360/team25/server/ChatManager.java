package edu.asu.cse360.team25.server;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class ChatManager {

	protected int idNext = 100;

	WeakHashMap<Integer, Chat> cacheByID = new WeakHashMap<Integer, Chat>();
	WeakHashMap<Integer, List<Chat>> cacheByCaseID = new WeakHashMap<Integer, List<Chat>>();

	public void logChat(int caseID, int patientID, int doctorID,
			boolean direction, String content) {

		int id = getNextID();

		Chat c = new Chat(id, caseID, patientID, doctorID, direction, content);

		addNewChatToCache(c);
		addNewChatToDatabase(c);

	}

	public synchronized Chat findChatByID(int id) {

		if (!cacheByID.containsKey(id)) {
			// not in cache, query the database
			Chat one = findChatByIDFromDatabase(id);
			if (one != null) {

				cacheByID.put(id, one);
			} else {

				// no such user
				return null;
			}

		}

		Chat c = cacheByID.get(id);
		return c;
	}

	public synchronized List<Chat> findChatHistory(int caseID) {

		if (!cacheByCaseID.containsKey(caseID)) {
			// not in cache, query the database
			List<Chat> ones = findChatsByCaseIDFromDatabase(caseID);
			if (ones != null) {

				cacheByCaseID.put(caseID, ones);
			} else {

				// no such chat
				return null;
			}

		}

		List<Chat> cs = cacheByCaseID.get(caseID);
		return cs;
	}

	protected synchronized void addNewChatToCache(Chat c) {

		if (c == null)
			return;

		cacheByID.put(c.getCaseID(), c);
		List<Chat> cs = cacheByCaseID.get(c.getCaseID());
		if (cs != null) {
			if(!cs.contains(c))
				cs.add(c);
		}

	}
	
	// CAUTION: only used for dummy chats
	protected synchronized void addNewChatToCaseIDCache(Chat c) {

		if (c == null)
			return;

		List<Chat> cs = cacheByCaseID.get(c.getCaseID());
		if (cs != null) {
			if(!cs.contains(c))
				cs.add(c);
		} else {
			cs = new ArrayList<Chat>();
			cs.add(c);
			cacheByCaseID.put(c.getCaseID(), cs);
		}

	}


	protected synchronized void addNewChatToDatabase(Chat c) {

	}

	protected synchronized Chat findChatByIDFromDatabase(int id) {

		return null;
	}

	protected synchronized List<Chat> findChatsByCaseIDFromDatabase(int caseID) {

		return null;
	}

	protected synchronized int getNextID() {

		return idNext++;
	}

	protected void setupDummyChats() {
		
		Chat[] cs = new Chat[17];
		
		cs[0] = new Chat(0, 0, 0, 0, true, "Hi, Dr. Hanayo.");
		cs[1] = new Chat(1, 0, 0, 0, false, "Oh, hi, Haruka.");
		cs[2] = new Chat(2, 0, 0, 0, true, "How are you doing?");
		cs[3] = new Chat(3, 0, 0, 0, false, "I'm doing alright.  How about you?");
		cs[4] = new Chat(4, 0, 0, 0, true, "Not too bad.  The weather is great isn't it?");
		cs[5] = new Chat(5, 0, 0, 0, false, "Yes.  It's absolutely beautiful today.");
		cs[6] = new Chat(6, 0, 0, 0, true, "I wish it was like this more frequently.");
		cs[7] = new Chat(7, 0, 0, 0, false, "Me too.");
		cs[8] = new Chat(8, 0, 0, 0, true, "So where are you going now?");
		cs[9] = new Chat(9, 0, 0, 0, false, "I'm going to meet a friend of mine at the department store.");

		cs[10] = new Chat(10, 0, 0, 0, true, "Going to do a little shopping?");
		cs[11] = new Chat(11, 0, 0, 0, false, "Yeah, I have to buy some presents for my parents.");
		cs[12] = new Chat(12, 0, 0, 0, false, "What's the occasion?");
		cs[13] = new Chat(13, 0, 0, 0, false, "It's their anniversary.");
		cs[14] = new Chat(14, 0, 0, 0, false, "That's great.  Well, you better get going.  You don't want to be late.");
		cs[15] = new Chat(15, 0, 0, 0, false, "I'll see you next time.");
		cs[16] = new Chat(16, 0, 0, 0, false, "Sure.  Bye.");

		
		for(int i = 0; i < cs.length; i++) {
			
			
			addNewChatToCache(cs[i]);
			addNewChatToCaseIDCache(cs[i]);
			
		}
		
	}
	
	static class IDPair {
		int patientID;
		int doctorID;

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof IDPair) {
				IDPair pair = (IDPair) obj;
				return (this.patientID == pair.patientID)
						&& (this.doctorID == pair.doctorID);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {

			long idMix = ((long) patientID << 32)
					| ((long) doctorID & 0x00000000FFFFFFFFL);
			return Long.valueOf(idMix).hashCode();
		}

	}
}
