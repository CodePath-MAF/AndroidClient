
package org.missionassetfund.apps.android.models;

public class Input {
    private String name;
    private String value;
    private int pos;
    private Class fragmentClass;

    public Input(String name, int pos, Class fragmentClass) {
        super();
        this.name = name;
        this.pos = pos;
        this.fragmentClass = fragmentClass;
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

    public Class getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class fragmentClass) {
        this.fragmentClass = fragmentClass;
    }
}
