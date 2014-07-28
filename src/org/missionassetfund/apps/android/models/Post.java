
package org.missionassetfund.apps.android.models;

import com.parse.ParseFile;
import com.parse.ParseObject;

public class Post extends ParseObject {
    public static final String POST_KEY = "post";
    public static final String USER_KEY = "user";
    public static final String GOAL_KEY = "goal";
    public static final String CONTENT_KEY = "content";
    public static final String PHOTO_KEY = "photo";

    public Post() {
    }

    public User getUser() {
        return (User) getParseUser(USER_KEY);
    }

    public void setUser(User user) {
        put(USER_KEY, user);
    }

    public Goal getGoal() {
        return (Goal) getParseObject(GOAL_KEY);
    }

    public void setGoal(Goal goal) {
        put(GOAL_KEY, goal);
    }

    public String getContent() {
        return getString(CONTENT_KEY);
    }

    public void setContent(String content) {
        put(CONTENT_KEY, content);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }
}
