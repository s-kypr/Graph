package algorithms;

import graph.Graph;
import graph.nodes.item.ItemNode;
import graph.nodes.session.SessionNode;
import graph.nodes.user.UserNode;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sofia on 5/19/16.
 */
public class PreFiltering {
    private Graph graph;
    private String username;
    private Timestamp time;
    private HashSet<String> tags;

    private double b;
    private double h;
    private double r;

    private int N;

    public PreFiltering(Graph graph, String username, Timestamp time, HashSet<String> tags, int n) {
        this.graph = graph;
        this.username = username;
        this.time = time;
        this.tags = tags;
        this.N = n;
    }

    public void setParameters(double b, double h, double r){
        setB(b);
        setH(h);
        setR(r);
    }

    public void run(){

        /***filter***/
        IPF ipf = new IPF(graph,username,time, N);
        ipf.setParameters(b,h,r);

        //keep only tags that are of interest to us
        UserNode userNode = graph.getUserNodes().get(username);
        SessionNode sessionNode = ipf.getSessionActive(username, time);

        HashSet<ItemNode> itemsToRemove = new HashSet<ItemNode>();

        //remove all items in users that do not have the same tag
        for (ItemNode item : userNode.getItems()){
            if (!containsTags(tags, item.getTags().keySet()))
                    itemsToRemove.add(item);
        }

        //remove all items in sessions that do not have the same tag
        for (ItemNode item : sessionNode.getItems()){
            if (!containsTags(tags, item.getTags().keySet()))
                    itemsToRemove.add(item);
        }

        for (ItemNode item: itemsToRemove)
            System.out.println("Remove: "+item.getItemID()+" "+item.getTags());

        //run IPF
        ipf.setGraph(graph);
        ipf.run();

    }

    //check if item has tags in the list
    public boolean containsTags(Set<String> tags, Set<String> itemTags){
        int contains = 0;

        for (String tag : tags){
            if (itemTags.contains(tag))
                contains ++;
        }

        return (contains > 0) ? true : false;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags = tags;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}
