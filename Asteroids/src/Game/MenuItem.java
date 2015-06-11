package Game;

import java.io.Serializable;

public class MenuItem implements Serializable {
    String name;
    int val;

    public MenuItem(String name){
        this.name=name;
    }
    public MenuItem(String name, int val){
        this.name = name;
        this.val = val;
    }
}
