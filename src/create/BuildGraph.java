package create;


import algorithms.IPF;
import graph.Graph;
import graph.nodes.item.ItemDB;
import graph.nodes.session.SessionDB;
import graph.nodes.user.UserDB;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sofia on 4/13/16.
 */
public class BuildGraph {


    public static void main(String[] args) throws Exception {

        /******* BUILD GRAPH ***********/
        int timewindow = 30;

        /*** establish a connection ***/
        Database db = new Database("users_posted_papers");
        Connection connection = null;
        connection = Database.getConnection();

        System.out.println("Initializing graph..");

        /* init graph*/
        Graph graph = new Graph(timewindow, Database.getFistDate(connection, db.getTABLE()));
        System.out.println("timewindow: "+timewindow+" days");
        System.out.println("Creating nodes...");

        /** create userNodes -> without items **/
        UserDB userDB = new UserDB(connection, db.getTABLE());
        ArrayList<String> usernames = userDB.getDBusernames();
        graph.createUserNodes(usernames);

        /** create itemNodes -> without sessions, users**/
        ItemDB itemDB = new ItemDB(connection,db.getTABLE());
        ArrayList<Integer> items = itemDB.getDBitems();
        graph.createItemNodes(items, itemDB);


        /** create sessionNodes -> without items **/
        SessionDB sessionDB = new SessionDB(connection,db.getTABLE());
        graph.createSessionNodes(sessionDB);


        /*** MAKE CONNECTIONS***/

        System.out.println("Creating edges...");

        /* update graph.nodes.user nodes with items*/
        graph.updateUserNodes(userDB);

        /* update graph.nodes.session nodes with items*/
        graph.updateSessionNodes(sessionDB);

        /* update graph.nodes.item nodes with users*/
        graph.updateItemNodes(itemDB);

//        System.out.println(graph.printSessionNodes());

//        System.out.println(graph.printItemNodes());


        System.out.println("\nNodes:");
        System.out.println("Users   : "+graph.getUserNodes().size());
        System.out.println("Items   : "+graph.getItemNodes().size());
        System.out.println("Sessions: "+graph.getSessionNodes().size());

        System.out.println("\nPairs:");
        System.out.println("user    <-> item : "+graph.getUserItemCon());
        System.out.println("session <-> item : "+graph.getItemSessionCon());

        String u = "7a4b9f0cd9a289c216dedbd8a3cb4609";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse("2005-03-02");
        Timestamp t = new java.sql.Timestamp(parsedDate.getTime());

        IPF ipf = new IPF(graph,u,t);
        ipf.setParameters(0.5,0.5,0.6);

        ipf.run();

    }






}
