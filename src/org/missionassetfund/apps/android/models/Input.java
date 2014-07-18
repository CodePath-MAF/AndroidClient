
package org.missionassetfund.apps.android.models;

public class Input {
    private String name;
    private String value;
    private int pos;
    private Class previousFragment;
    private Class nextFragment;

    public Input(String name, int pos, Class previousFragment, Class nextFragment) {
        super();
        this.name = name;
        this.pos = pos;
        this.previousFragment = previousFragment;
        this.nextFragment = nextFragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Class getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(Class previousFragment) {
        this.previousFragment = previousFragment;
    }

    public Class getNextFragment() {
        return nextFragment;
    }

    public void setNextFragment(Class nextFragment) {
        this.nextFragment = nextFragment;
    }
    
    

}
