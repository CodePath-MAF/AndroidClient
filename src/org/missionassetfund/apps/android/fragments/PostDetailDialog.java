
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
    ImageButton btnComment;

    public PostDetailDialog() {
        // Empty constructor required for DialogFragment
    }

    public static PostDetailDialog newInstance(Post post) {
        PostDetailDialog frag = new PostDetailDialog();
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        frag.post = post;
        return frag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comments = (List<Comment>) post.get("comments");
        acomments = new CommentsAdapter(getActivity(), comments);
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
        btnComment = (ImageButton) view.findViewById(R.id.btnComment);
        btnComment.setEnabled(false);

        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        window.setLayout(400, 400);
        window.setGravity(Gravity.CENTER);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setListeners();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.PostDetailAnimation;
    }

    private void setListeners() {
        btnComment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveComment();
            }
        });

        etComment.addTextChangedListener(commentWatcher);

        etComment.setOnEditorActionListener(sendKeyListner);

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

    private TextWatcher commentWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no-op
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no-op
        }

        @Override
        public void afterTextChanged(Editable s) {
            int totalCharacters = s.length();
            btnComment.setEnabled(totalCharacters > 0 ? true : false);
        }
    };

    private OnEditorActionListener sendKeyListner = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                saveComment();
                handled = true;
            }
            return handled;
        }
    };

    protected void saveComment() {
        SaveCommentListener saveCommentListener = (SaveCommentListener) getActivity();
        saveCommentListener.onSaveComment(
                post.getObjectId(), etComment.getText().toString());
        dismiss();
    }

}
