
package org.missionassetfund.apps.android.adapters;

import java.util.List;
import java.util.Locale;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LCDetailsActivity;
import org.missionassetfund.apps.android.interfaces.SaveCommentListener;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.PostType;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;
import org.missionassetfund.apps.android.utils.FormatterUtils;
import org.missionassetfund.apps.android.utils.MAFDateUtils;
import org.missionassetfund.apps.android.utils.ModelUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GoalPostsAdapter extends ArrayAdapter<Post> {

    private final Goal goal;

    public GoalPostsAdapter(Context context, Goal goal, List<Post> posts) {
        super(context, R.layout.item_lc_post, posts);
        this.goal = goal;

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post post = getItem(position);

        // TODO use viewholder
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_lc_post, parent, false);

        populatePostViews(post, convertView);

        // comments
        // LinearLayout llComments = (LinearLayout)
        // convertView.findViewById(R.id.llComents);
        // llComments.removeAllViews();
        //
        // List<Comment> comments = (List<Comment>) post.get("comments");
        // if (comments != null) {
        // for (Comment comment : comments) {
        // View commentView = LayoutInflater.from(getContext()).inflate(
        // R.layout.item_lc_comment, parent, false);
        // populateCommentView(comment, commentView);
        // llComments.addView(commentView);
        // }
        //
        // }

        // New Comment input
        // View newCommentView = LayoutInflater.from(getContext()).inflate(
        // R.layout.item_lc_create_comment, parent, false);
        //
        // populateNewCommentInputView(newCommentView, post);
        // llComments.addView(newCommentView);
        //
        return convertView;
    }

    private void populatePostViews(Post post, View convertView) {
        ImageView ivPosterProfile = (ImageView) convertView.findViewById(R.id.ivPosterProfile);
        TextView tvNumberOfComments = (TextView) convertView.findViewById(R.id.tvNumberOfComments);
        ImageView ivPostType = (ImageView) convertView.findViewById(R.id.ivPostType);
        TextView tvPostText = (TextView) convertView.findViewById(R.id.tvPostText);
        TextView tvPaymentDue = (TextView) convertView.findViewById(R.id.tvPaymentDue);
        TextView tvPostReminderText = (TextView) convertView.findViewById(R.id.tvPostReminderText);

        if (post.getType() == PostType.REMINDER) {
            // special MAF post render differently
            ivPosterProfile.setImageResource(R.drawable.profile_13);

            // set human-friendly due dates
            tvPostReminderText.setText(getContext().getString(R.string.lc_item_due_date,
                    FormatterUtils.formatMonthDate(goal.getNextPaymentDate())));
            
            tvPostReminderText.setVisibility(View.VISIBLE);
            tvPostText.setVisibility(View.GONE);

            // Payment Due for reminders
            tvPaymentDue.setText(CurrencyUtils.getCurrencyValueFormatted(goal.getPaymentAmount()));
            tvPaymentDue.setVisibility(View.VISIBLE);
        } else if (post.getType() == PostType.EVENT) {
            // TODO: clean this up. Hack for demo day.
            
            // special MAF post render differently
            ivPosterProfile.setImageResource(R.drawable.profile_13);
            
            // Post type
            ivPostType.setImageResource(ModelUtils.getImageForPostType(post));
            
            // Post content
            tvPostText.setText(post.getContent());

            tvPaymentDue.setVisibility(View.GONE);
            
        } else {
            // Profile Image
            ivPosterProfile.setImageResource(ModelUtils.getImageResourceForUser(post.getUser()));

            // Has comments?
            List<Comment> comments = (List<Comment>) post.get("comments");
            if (comments != null && comments.size() > 0) {
                tvNumberOfComments.setText(String.valueOf(comments.size()));
                tvNumberOfComments.setVisibility(View.VISIBLE);
            }

            // Post type
            ivPostType.setImageResource(ModelUtils.getImageForPostType(post));

            // Post content
            tvPostText.setText(post.getContent());

            tvPaymentDue.setVisibility(View.GONE);
        }
    }

    private void populateCommentView(Comment comment, View commentView) {
        // Commenter profile Image
        ImageView ivCommenterProfile = (ImageView) commentView
                .findViewById(R.id.ivCommenterProfile);
        ivCommenterProfile.setImageResource(ModelUtils.getImageResourceForUser(comment.getUser()));

        // Commenter Name
        TextView tvCommenterName = (TextView)
                commentView.findViewById(R.id.tvCommenterName);
        tvCommenterName.setText(comment.getUser().getName());

        // comment Text
        TextView tvCommentText = (TextView)
                commentView.findViewById(R.id.tvCommentText);
        tvCommentText.setText(comment.getContent());

    }

    private void populateNewCommentInputView(View newCommentView, final Post post) {
        // User's image
        ImageView ivPosterProfile = (ImageView) newCommentView.findViewById(R.id.ivPosterProfile);
        ivPosterProfile.setImageResource(ModelUtils.getImageResourceForUser((User) User
                .getCurrentUser()));

        final EditText etComment = (EditText) newCommentView.findViewById(R.id.etComment);

        etComment.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    SaveCommentListener saveCommentListener = (SaveCommentListener) (LCDetailsActivity) getContext();
                    saveCommentListener.onSaveComment(
                            post.getObjectId(), etComment.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

    }

}
