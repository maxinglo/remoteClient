package view;

import javax.swing.*;
import java.util.Vector;

public class PlayerListComponent extends JList {
    public Vector<String> vector;
    public void SyncPlayers(String[] strings){
        vector = new Vector<String>();
        for (int i = 1; i < strings.length; i++) {
            vector.add(strings[i]);
        }
        this.setListData(vector);
    }
    public void RemovePlayer(String[] strings){
        vector.remove(strings[1]);
        this.setListData(vector);
    }
}
