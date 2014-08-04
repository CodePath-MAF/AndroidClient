
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.SavePostListener;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.PostType;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.ModelUtils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailDialog extends DialogFragment {

    private Post post;

    ImageView ivPosterProfile;
    ImageView ivPostType;
    TextView tvPostText;
    TextView tvPaymentDue;

    public PostDetailDialog() {
        // Empty constructor required for DialogFragment
    }

    public static PostDetailDialog newInstance(String title) {
        PostDetailDialog frag = new PostDetailDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container);
        ivPosterProfile = (ImageView) view.findViewById(R.id.ivPosterProfile);
        ivPostType = (ImageView) view.findViewById(R.id.ivPostType);
        tvPostText = (TextView) view.findViewById(R.id.tvPostText);
        tvPaymentDue = (TextView) view.findViewById(R.id.tvPaymentDue);

        // String title = getArguments().getString("title", "Your Tweet");
        // getDialog().setTitle(title);
        // Show soft keyboard automatically
        return view;
    }

    private void populatePostViews() {
        // Profile Image
        ivPosterProfile.setImageResource(ModelUtils.getImageResourceForUser(post.getUser()));

        // Post type
        ivPostType.setImageResource(ModelUtils.getImageForPostType(post));

        // Post content
        tvPostText.setText(post.getContent());

        // Payment Due for reminders
        if (post.getType() != null && post.getType() == PostType.REMINDER) {
            // tvPaymentDue.setText(CurrencyUtils.getCurrencyValueFormatted(goal.getPaymentsDue()));
        }

    }

}
