
package org.missionassetfund.apps.android.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Comment")
@JsonIgnoreProperties(value = { "objectId" }, ignoreUnknown = true)
public class Comment extends ParseObject {
    public static final String COMMENT_KEY = "comment";
    public static final String USER_KEY = "user";
    public static final String POST_KEY = "post";
    public static final String CONTENT_KEY = "content";

    public Comment() {
    }

    public User getUser() {
        return (User) getParseUser(USER_KEY);
    }

    public void setUser(User user) {
        put(USER_KEY, user);
    }

    public Post getPost() {
        return (Post) getParseObject(POST_KEY);
    }

    public void setPost(Post post) {
        put(POST_KEY, post);
    }

    public String getContent() {
        return getString(CONTENT_KEY);
    }

    public void setContent(String content) {
        put(CONTENT_KEY, content);
    }
}
