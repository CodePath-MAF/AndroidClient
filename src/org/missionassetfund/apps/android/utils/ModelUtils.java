
package org.missionassetfund.apps.android.utils;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.LendingCircleFriends;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.PostType;
import org.missionassetfund.apps.android.models.User;

import android.util.Log;

public class ModelUtils {
    public static int getImageResourceForUser(User user) {
        if (user.getProfileImageId() != 0) {
            Log.d("DEBUG", "Profile Image ID: " + user.getProfileImageId());
            return LendingCircleFriends.get(user.getProfileImageId() - 1).getDpDrawableId();
        }
        return R.drawable.profile_11;
    }

    public static int getImageForPostType(Post post) {
        // TODO: move this to Post Object later
        if (post.getType() != null) {
            PostType type = post.getType();
            switch (type) {
                case EVENT:
                    return R.drawable.img_newpost_event;
                case MESSAGE:
                    return R.drawable.img_newpost_message;
                case QUESTION:
                    return R.drawable.img_newpost_question;
                case REMINDER:
                    return R.drawable.img_newpost_post;
                default:
                    return R.drawable.img_newpost_message;
            }
        } else {
            return R.drawable.img_newpost_message;
        }
    }

}
