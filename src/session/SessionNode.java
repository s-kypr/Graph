package session;

import item.ItemNode;

import java.sql.Timestamp;
import java.util.HashSet;

/**
 * Created by sofia on 4/17/16.
 */
public class SessionNode {
    private SessionData sessionData;    //username and session number
    private Timestamp firstDay;
    private HashSet<ItemNode> items;

    public SessionNode(SessionData sessionData, Timestamp firstDay) {
        this.sessionData = sessionData;
        this.firstDay = firstDay;
        this.items = new HashSet<ItemNode>();
    }

    public void addItem(ItemNode item){
        if (!items.contains(item))
            items.add(item);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public Timestamp getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Timestamp firstDay) {
        this.firstDay = firstDay;
    }

    public HashSet<ItemNode> getItems() {
        return items;
    }

    public void setItems(HashSet<ItemNode> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "SessionNode{" +
                "sessionData=" + sessionData.toString() +
                ", firstDay=" + firstDay +
                ", items=" + items +
                '}';
    }
}
