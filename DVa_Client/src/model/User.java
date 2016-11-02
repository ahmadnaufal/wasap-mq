package model;

import java.util.ArrayList;

/**
 * Created by Julio Savigny on 11/2/2016.
 */
public class User {
    private  String username;
    private ArrayList<String> groupList; //Diisi sama group punya user ini (RPC)
    private ArrayList<String> friendList; //Diisi dengan friend user ini (RPC)

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<String> groupList) {
        this.groupList = groupList;
    }

    public void addGroupList(String s){
        this.groupList.add(s);
    }

    public void addFriendList(String s){
        this.friendList.add(s);
    }

    public void removeGroupList(String s){
        this.groupList.remove(s);
    }

    public void removeFriendList(String s){
        this.friendList.remove(s);
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

}
