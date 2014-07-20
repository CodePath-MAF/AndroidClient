
package org.missionassetfund.apps.android.models;

import java.io.Serializable;

public class Input implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String INPUT_KEY = "input";

    private String name;
    private String value;
    private String hint;
    private int pos;
    private Class<?> fragmentClass;

    public Input(String name, String hint, int pos, Class<?> fragmentClass) {
        super();
        this.name = name;
        this.hint = hint;
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

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Class<?> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<?> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }
}
