
package org.missionassetfund.apps.android.models;

import java.util.ArrayList;

import org.missionassetfund.apps.android.R;

public class LendingCircleFriends {
    private static final String NAME_1 = "A. B.";
    private static final String NAME_2 = "C. D.";
    private static final String NAME_3 = "E. F.";
    private static final String NAME_4 = "G. H.";
    private static final String NAME_5 = "I. J.";
    private static final String NAME_6 = "K. L.";
    private static final String NAME_7 = "M. N.";
    private static final String NAME_8 = "O. P.";
    private static final String NAME_9 = "Q. R.";
    private static final String NAME_10 = "S. T.";
    private static final String NAME_11 = "U. V.";
    private static final String NAME_12 = "W. X.";
    public static ArrayList<LendingCircleFriend> FRIENDS = new ArrayList<LendingCircleFriend>();

    static {
        FRIENDS.add(new LendingCircleFriend(NAME_1, R.drawable.profile_1));
        FRIENDS.add(new LendingCircleFriend(NAME_2, R.drawable.profile_2));
        FRIENDS.add(new LendingCircleFriend(NAME_3, R.drawable.profile_3));
        FRIENDS.add(new LendingCircleFriend(NAME_4, R.drawable.profile_4));
        FRIENDS.add(new LendingCircleFriend(NAME_5, R.drawable.profile_5));
        FRIENDS.add(new LendingCircleFriend(NAME_6, R.drawable.profile_6));
        FRIENDS.add(new LendingCircleFriend(NAME_7, R.drawable.profile_7));
        FRIENDS.add(new LendingCircleFriend(NAME_8, R.drawable.profile_8));
        FRIENDS.add(new LendingCircleFriend(NAME_9, R.drawable.profile_9));
        FRIENDS.add(new LendingCircleFriend(NAME_10, R.drawable.profile_10));
        FRIENDS.add(new LendingCircleFriend(NAME_11, R.drawable.profile_11));
        FRIENDS.add(new LendingCircleFriend(NAME_12, R.drawable.profile_12));
    }

    public static LendingCircleFriend get(int index) {
        return FRIENDS.get(index);
    }
}
