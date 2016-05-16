package item;


import session.SessionNode;
import user.UserNode;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by sofia on 4/17/16.
 */
public class ItemNode {
    private int itemID;
    private HashMap<String ,Integer> tags;
    private HashSet<SessionNode> sessions;
    private HashSet<UserNode> users;


    public ItemNode(int itemID, HashMap<String, Integer> tags) {
        this.itemID = itemID;
        this.tags = tags;
        this.sessions = new HashSet<SessionNode>();
        this.users = new HashSet<UserNode>();
    }

    public void addUser(UserNode userNode){
        if (!users.contains(userNode))
            users.add(userNode);
    }

    public void addSession(SessionNode sessionNode){
        if (!sessions.contains(sessionNode))
            sessions.add(sessionNode);
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public HashMap<String, Integer> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, Integer> tags) {
        this.tags = tags;
    }

    public HashSet<SessionNode> getSessions() {
        return sessions;
    }

    public void setSessions(HashSet<SessionNode> sessions) {
        this.sessions = sessions;
    }

    public HashSet<UserNode> getUsers() {
        return users;
    }

    public void setUsers(HashSet<UserNode> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        String sessionString = null;
        String userString = null;
        String tagString = null;

        for (SessionNode sessionNode : sessions) {
            if (sessionNode != null) {
                if (sessionString != null)
                    sessionString += ", " + sessionNode.getSessionData().toString();
                else
                    sessionString = sessionNode.getSessionData().toString();
            }
        }
        for (UserNode userNode : users) {
            if (userString != null)
                userString += ", " + userNode.getUsername();
            else
                userString = userNode.getUsername();
        }

        for (String tag : tags.keySet())
            if (tagString != null)
                tagString += ", "+"("+tag+","+tags.get(tag)+")";
            else
                tagString = "("+tag+","+tags.get(tag)+")";

        return itemID+"-> \n\tusers: "+userString+" \n\tsessions: "+sessionString+" \ttags: "+tagString+"\n";
    }
}

