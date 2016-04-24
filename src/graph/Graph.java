package graph;

import item.ItemDB;
import item.ItemNode;
import session.SessionDB;
import session.SessionData;
import session.SessionNode;
import user.UserDB;
import user.UserNode;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sofia on 4/17/16.
 */
public class Graph {
    private HashMap<Integer, ItemNode> itemNodes;
    private HashMap<SessionData, SessionNode> sessionNodes;
    private HashMap<String, UserNode> userNodes;
    private long timeWindow;                            // n day to milliseconds.
    private Timestamp firstDay;


    public Graph(int timeWindow, Timestamp firstDay) {
        this.timeWindow = TimeUnit.DAYS.toMillis(timeWindow);
        this.firstDay = firstDay;

        this.userNodes = new HashMap<String, UserNode>();
        this.itemNodes = new HashMap<Integer, ItemNode>();
        this.sessionNodes = new HashMap<SessionData, SessionNode>();
    }

    public void createUserNodes(ArrayList<String> usernames){

        for (String username : usernames){
            UserNode node = new UserNode(username);
            userNodes.put(username,node);
        }
    }

    public void createItemNodes(ArrayList<Integer> items){

        for (Integer itemID : items){
            ItemNode node = new ItemNode(itemID);
            itemNodes.put(itemID, node);
        }
    }

    public void createSessionNodes(SessionDB sessionDB){

        for (String username : userNodes.keySet()){
            ArrayList<Timestamp> timestamps = null;
            try {
                timestamps = sessionDB.getUserTimestamps(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            createSessionNode(timestamps, username);
        }
    }

    public void createSessionNode(ArrayList<Timestamp> timestamps, String username){

        for (Timestamp time : timestamps){
            long diff = time.getTime() - firstDay.getTime();    // find the days in mill sec
            int sessionNo = (int) (diff / timeWindow);                    //divide to find the session

            SessionData sessionData = null;

            if (sessionNo == 0 && (diff % timeWindow != 0))
                sessionData = new SessionData(username, sessionNo);     //in case there is mod -> bucket 0
            else
                sessionData = new SessionData(username, sessionNo + 1); //in all other add 1

            if (!sessionNodes.containsKey(sessionData)) {
                Timestamp firstDay = new Timestamp(this.firstDay.getTime() + sessionNo * timeWindow);
                SessionNode sessionNode = new SessionNode(sessionData, firstDay);
                sessionNodes.put(sessionData, sessionNode);
            }
        }

    }

    public void updateUserNodes(UserDB userDB){
        for (String username : userNodes.keySet()){
            UserNode node = userNodes.get(username);
            ArrayList<Integer> itemList = null;

            try {
                itemList = userDB.getUserItems(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (Integer item: itemList){
                ItemNode itemNode = itemNodes.get(item);
                node.addItem(itemNode);
            }

            userNodes.put(username, node);
        }
    }

    public void updateSessionNodes(SessionDB sessionDB){

        for (SessionData sessionData : sessionNodes.keySet()){
            SessionNode node = sessionNodes.get(sessionData);

            ArrayList<Integer> itemList = null;

            Timestamp lastDay = new Timestamp(node.getFirstDay().getTime() + timeWindow);
            try {
                itemList = sessionDB.getSessionItems(sessionData.getUsername(), node.getFirstDay(), lastDay);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (Integer item: itemList){
                ItemNode itemNode = itemNodes.get(item);
                node.addItem(itemNode);
            }

            sessionNodes.put(sessionData, node);
        }

    }

    //TODO: update items with USERS and SESSIONS
    public void updateItemNodes(ItemDB itemDB){

        for (Integer itemID : itemNodes.keySet()){
            ItemNode node = itemNodes.get(itemID);

            ArrayList<String> usernames = null;

            try {
                usernames = itemDB.getItemUsers(itemID);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            /* add user nodes */
            for (String username : usernames){
                UserNode userNode = userNodes.get(username);
                node.addUser(userNode);
            }

            /* add session nodes*/

            //TODO SESSIONS

        }

    }

    @Override
    public String toString() {
        return "Graph{" +
                "\nitemNodes=" + printItemNodes() +
                ", \nsessionNodes=" + sessionNodes +
                ", \nuserNodes=" + printUserNodes() +
                ", \ntimeWindow=" + timeWindow +
                ", \nfirstDay=" + firstDay +
                '}';
    }

    public String printUserNodes(){
        String toPrint = null;
        for (String username : userNodes.keySet())
            toPrint = toPrint+ "\n"+username +" " + userNodes.get(username).toString();
        return toPrint;
    }

    public String printItemNodes(){
        String toPrint = null;
        for (Integer itemID : itemNodes.keySet())
            toPrint = toPrint+ "\n"+itemID +" " + itemNodes.get(itemID).toString();
        return toPrint;
    }

    public String printSessionNodes(){
        String toPrint = null;
            for (SessionData sessionData: sessionNodes.keySet())
                toPrint = toPrint+ "\n"+ " " + sessionNodes.get(sessionData).toString();
        return toPrint;
    }

    public HashMap<Integer, ItemNode> getItemNodes() {
        return itemNodes;
    }

    public void setItemNodes(HashMap<Integer, ItemNode> itemNodes) {
        this.itemNodes = itemNodes;
    }

    public HashMap<SessionData, SessionNode> getSessionNodes() {
        return sessionNodes;
    }

    public void setSessionNodes(HashMap<SessionData, SessionNode> sessionNodes) {
        this.sessionNodes = sessionNodes;
    }

    public HashMap<String, UserNode> getUserNodes() {
        return userNodes;
    }

    public void setUserNodes(HashMap<String, UserNode> userNodes) {
        this.userNodes = userNodes;
    }

    public long getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(int timeWindow) {
        this.timeWindow = timeWindow;
    }

    public Timestamp getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Timestamp firstDay) {
        this.firstDay = firstDay;
    }

    //    public boolean addItemNode(ItemNode node) {
//        /* If the node already exists, don't do anything. */
//        if (mGraph.containsKey(node))
//            return false;
//
//        /* Otherwise, add the node with an empty set of outgoing edges. */
//        mGraph.put(node, new HashSet<T>());
//        return true;
//    }

}