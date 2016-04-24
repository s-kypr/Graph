package user;


import item.ItemNode;

import java.util.HashSet;

/**
 * Created by sofia on 4/17/16.
 */
public class UserNode {
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
}

