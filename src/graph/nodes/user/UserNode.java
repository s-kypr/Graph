package graph.nodes.user;


import graph.nodes.Node;
import graph.nodes.item.ItemNode;

import java.util.HashSet;

/**
 * Created by sofia on 4/17/16.
 */
public class UserNode implements Node{
    private String username;
    private HashSet<ItemNode> items;

    public UserNode(String username) {
        this.username = username;
        this.items = new HashSet<ItemNode>();
    }

    public void addItem(ItemNode item){
        if (!items.contains(item))
            items.add(item);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashSet<ItemNode> getItems() {
        return items;
    }

    public void setItems(HashSet<ItemNode> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        String itemString = null;

        for (ItemNode itemNode : items) {
            if (itemString != null)
                itemString += ", " + itemNode.getItemID();
            else {
                itemString = Integer.toString(itemNode.getItemID());
            }
        }
        return username+"-> \n\titems:"+itemString;
    }

    @Override
    public int outDegree() {
        return items.size();
    }

    @Override
    public HashSet<Node> out() {
        HashSet<Node> itemSet = new HashSet<Node>(items);
        return itemSet;
    }
}

