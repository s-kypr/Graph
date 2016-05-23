package algorithms;

import graph.Graph;
import graph.nodes.Node;
import graph.nodes.item.ItemNode;
import graph.nodes.session.SessionData;
import graph.nodes.session.SessionNode;
import graph.nodes.user.UserNode;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sofia on 5/16/16.
 */

/*
    Injected Preference Fusion
    to make recommendation for active user u at time t.

    Data: STG G, user u, time t
    Result: Recommendation for user u at time t
 */

public class IPF {
    private Graph graph;
    private String username;
    private Timestamp time;

    private double b;
    private double h;
    private double r;

    private int N;

    public IPF(Graph g, String u, Timestamp t, int n) {
        this.graph = g;
        this.username = u;
        this.time = t;
        this.N = n;
    }

    public void setParameters(double b, double h, double r){
        setB(b);
        setH(h);
        setR(r);
    }

    public void run(){

        //initialize nodes
        UserNode userNode = graph.getUserNodes().get(username);
        SessionNode sessionNode = getSessionActive(username, time);

        if (sessionNode == null){
            System.out.println("User \" "+username+" \" is not active at time: "+time);
            return;
        }
        else {}
//            System.out.println("items: "+ sessionNode.getItems());

        Queue<Node> Q = new LinkedList<Node>();
        HashSet<Node> V = new HashSet<Node>();

        Q.add(userNode);
        Q.add(sessionNode);

        Map<Node, Integer> distance = new HashMap<Node, Integer>();
        distance.put(userNode, 0);
        distance.put(sessionNode, 0);

        Map<Node, Double> rank = new HashMap<Node, Double>();
        rank.put(userNode, b);
        rank.put(sessionNode, 1-b);

        while (!Q.isEmpty()){
            Node n = Q.poll();

            if (V.contains(n))
                continue;
            if (distance.get(n) > 3)
                break;

            V.add(n);

            for (Node nd : n.out()){
                if (!V.contains(nd)){
                    distance.put(nd, distance.get(n) + 1);          //distance[v' ] = distance[v] + 1;
                    Q.add(nd);
                }
                if (distance.get(n) < distance.get(nd)) {             //if distance[v] < distance[v ] then
                    double newRank;

                    if (rank.containsKey(nd))
                        newRank = rank.get(nd) + rank.get(n) * propagationFunc(n, nd);
                    else
                        newRank = rank.get(n) * propagationFunc(n, nd);
                    rank.put(nd, newRank);
                }
            }
        }


//        System.out.println(rank.size());

        //keep only Unknown items -> for example only first item is known
        for(Iterator<Map.Entry<Node, Double>> it = rank.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Node, Double> entry = it.next();
            if(entry.getKey() instanceof SessionNode || entry.getKey() instanceof UserNode) {
                it.remove();
            }
//            else if (entry.getKey() instanceof ItemNode){
//                if (userNode.getItems().contains(entry.getKey()))
//                    it.remove();
//            }
            else if (entry.getKey() instanceof ItemNode){
                if (sessionNode.getItems().contains(entry.getKey()))
                    it.remove();
            }

        }
//        System.out.println(rank.size());

        //rank items
        Map<Node,Double> rankSorted = sort(rank);

        //return top-N unknown items;
        int n=1;
        for (Node node : rankSorted.keySet()){

            int itemID = ((ItemNode) node).getItemID();
//            if (itemID == 114719 || itemID == 167557 || itemID == 176994 || itemID == 218147 || itemID == 99857)
            System.out.println(n+": "+itemID +" "+ rankSorted.get(node));
            n++;

            if (n > N)
                break;
        }

    }

    public double propagationFunc(Node u1, Node u2){
        double prop = 0;

        //u1 is User/Session, u2 is Item
        if ( (u1 instanceof UserNode || u1 instanceof SessionNode) && (u2 instanceof ItemNode)) {
                prop = 1 / Math.pow(u1.outDegree(), r);
//                System.out.println("CASE 1");
//                System.out.println(u1.outDegree() + ": \t"+prop);
        }
        //u1 is Item, u2 is User
        else if (u1 instanceof ItemNode && u2 instanceof UserNode) {
            double denominator = h*((ItemNode) u1).outUserDegree() + ((ItemNode) u1).outSessionDegree();
            prop = Math.pow(h/denominator, r);
//            System.out.println("CASE 2");
//            System.out.println("degrees: "+((ItemNode) u1).outUserDegree() + " "+((ItemNode) u1).outSessionDegree());
//            System.out.println(denominator+" "+prop);
        }
        //u1 is Item, u2 is Session
        else if (u1 instanceof ItemNode && u2 instanceof SessionNode){
            double denominator = h*((ItemNode) u1).outUserDegree() + ((ItemNode) u1).outSessionDegree();
            prop = Math.pow(1/denominator, r);
//            System.out.println("CASE 3");
//            System.out.println("degrees: "+((ItemNode) u1).outUserDegree() + " "+((ItemNode) u1).outSessionDegree());
//            System.out.println(denominator+" "+prop);
        }

        return prop;
    }



    public SessionNode getSessionActive(String username, Timestamp timestamp){

        //find sessionNo
        int sessionNo = Graph.findSessionNo(timestamp);

        SessionData sessionData = new SessionData(username, sessionNo);
        if (graph.getSessionNodes().containsKey(sessionData)) {
//            System.out.println("tms:" +graph.getSessionNodes().get(sessionData));
            return graph.getSessionNodes().get(sessionData);
        }
        else
            return null;
    }

    //http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private Map<Node, Double> sort(Map<Node, Double> unsortMap)
    {

        List<Map.Entry<Node, Double>> list = new LinkedList<Map.Entry<Node, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Node, Double>>()
        {
            public int compare(Map.Entry<Node, Double> o1,
                               Map.Entry<Node, Double> o2)
            {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Node, Double> sortedMap = new LinkedHashMap<Node, Double>();
        for (Map.Entry<Node, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
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
