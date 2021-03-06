package graph;

import graph.nodes.item.ItemDB;
import graph.nodes.item.ItemNode;
import graph.nodes.session.SessionDB;
import graph.nodes.session.SessionData;
import graph.nodes.session.SessionNode;
import graph.nodes.user.UserDB;
import graph.nodes.user.UserNode;

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
    private static long timeWindow;                            // n day to milliseconds.
    private static Timestamp firstDay;


    public Graph(int timeWindow, Timestamp firstDay) {
        Graph.timeWindow = TimeUnit.DAYS.toMillis(timeWindow);
        Graph.firstDay = firstDay;

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

    public void createItemNodes(ArrayList<Integer> items, ItemDB itemDB){

        for (Integer itemID : items){
            HashMap<String,Integer> tags = new HashMap<String, Integer>();
            try {
                tags = itemDB.getItemTags(itemID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ItemNode node = new ItemNode(itemID, tags);
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
            int sessionNo = findSessionNo(time);

            SessionData sessionData = new SessionData(username, sessionNo);

            if (!sessionNodes.containsKey(sessionData)) {

                /*if (username.equals("7a4b9f0cd9a289c216dedbd8a3cb4609")){
                    System.out.println(sessionNo);
                    System.out.println(new Timestamp(Graph.firstDay.getTime() + sessionNo * timeWindow));
                }*/

                Timestamp firstDay = new Timestamp(Graph.firstDay.getTime() + sessionNo * timeWindow);
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

    public void updateItemNodes(ItemDB itemDB){

        for (Integer itemID : itemNodes.keySet()){
            ItemNode node = itemNodes.get(itemID);

            /* add graph.nodes.user nodes */
            ArrayList<String> usernames = null;
            try {
                usernames = itemDB.getItemUsers(itemID);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (String username : usernames){
                UserNode userNode = userNodes.get(username);
                node.addUser(userNode);
            }

            /* add graph.nodes.session nodes*/
            ArrayList<SessionData> sessions = null;

            try {
                sessions = itemDB.getItemSessions(itemID);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            for (SessionData sessionData: sessions){
                SessionNode sessionNode = sessionNodes.get(sessionData);
                node.addSession(sessionNode);
            }
        }

    }

    public static int findSessionNo(Timestamp timestamp){
        long diff = timestamp.getTime() - firstDay.getTime();    // find the days in mill sec
        int sessionNo = (int) (diff / timeWindow);                    //divide to find the graph.nodes.session

       if (sessionNo == 0 && (diff % timeWindow != 0))
            sessionNo = 0;                                       //in case there is mod -> bucket 0
        else
            sessionNo = sessionNo ;                          //in all other add 1

        return sessionNo;
    }

    public int getUserItemCon(){
        int pairs = 0;
        for (String username : userNodes.keySet()){
            UserNode node = userNodes.get(username);
            pairs += node.getItems().size();
        }
        return pairs;
    }

    public int getItemSessionCon(){
        int pairs = 0;
        for (int articleID : itemNodes.keySet()){
            ItemNode node = itemNodes.get(articleID);
            pairs += node.getSessions().size();
        }
        return pairs;
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
        Graph.timeWindow = timeWindow;
    }

    public Timestamp getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Timestamp firstDay) {
        Graph.firstDay = firstDay;
    }

}
