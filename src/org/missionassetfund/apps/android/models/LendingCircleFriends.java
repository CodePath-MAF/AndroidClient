
package org.missionassetfund.apps.android.models;

import java.util.ArrayList;

import org.missionassetfund.apps.android.R;

public class LendingCircleFriends {
    private static final String NAME_1 = "Sofia.R";
    private static final String NAME_2 = "Mark.S";
    private static final String NAME_3 = "Alyssa.T";
    private static final String NAME_4 = "Rob.C";
    private static final String NAME_5 = "Tom.G";
    private static final String NAME_6 = "Jairo.A";
    private static final String NAME_7 = "Eddie.F";
    private static final String NAME_8 = "Guy.M";
    private static final String NAME_9 = "Michael.H";
    private static final String NAME_10 = "Jose.M";
    private static final String NAME_11 = "Felipe.D";
    private static final String NAME_12 = "Amit.B";
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
