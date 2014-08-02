
package org.missionassetfund.apps.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Category")
@JsonIgnoreProperties(value = { "objectId" }, ignoreUnknown = true)
public class Category extends ParseObject {
    public static final String NAME_KEY = "name";
    public static final String COLOR_KEY = "color";

    public Category() {
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }
    
    public String getColor() {
        return getString(COLOR_KEY);
    }
    
    public void setColor(String color) {
        put(COLOR_KEY, color);
    }
}
