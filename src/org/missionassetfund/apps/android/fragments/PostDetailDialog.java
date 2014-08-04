
package org.missionassetfund.apps.android.fragments;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LCDetailsActivity;
import org.missionassetfund.apps.android.adapters.CommentsAdapter;
import org.missionassetfund.apps.android.interfaces.SaveCommentListener;
import org.missionassetfund.apps.android.interfaces.SavePostListener;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.PostType;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.ModelUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PostDetailDialog extends DialogFragment {

    private Post post;
    List<Comment> comments;
    CommentsAdapter acomments;

    ImageView ivPosterProfile;
    ImageView ivPostType;
    TextView tvPostText;
    TextView tvPaymentDue;

    ListView llComments;

    EditText etComment;
    InputMethodManager imm;

    public PostDetailDialog() {
        // Empty constructor required for DialogFragment
    }

    public static PostDetailDialog newInstance(Post post) {
        PostDetailDialog frag = new PostDetailDialog();
        frag.post = post;
        return frag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comments = (List<Comment>) post.get("comments");
        acomments = new CommentsAdapter(getActivity(), comments);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container);
        ivPosterProfile = (ImageView) view.findViewById(R.id.ivPosterProfile);
        ivPostType = (ImageView) view.findViewById(R.id.ivPostType);
        tvPostText = (TextView) view.findViewById(R.id.tvPostText);
        tvPaymentDue = (TextView) view.findViewById(R.id.tvPaymentDue);

        populatePostViews();

        llComments = (ListView) view.findViewById(R.id.llComents);
        llComments.setAdapter(acomments);

        etComment = (EditText) view.findViewById(R.id.etComment);

        etComment.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SaveCommentListener saveCommentListener = (SaveCommentListener) getActivity();
                    saveCommentListener.onSaveComment(
                            post.getObjectId(), etComment.getText().toString());
                    handled = true;
                    dismiss();
                }
                return handled;
            }
        });

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
