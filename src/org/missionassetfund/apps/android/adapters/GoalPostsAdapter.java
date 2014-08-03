
package org.missionassetfund.apps.android.adapters;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.activities.LCDetailsActivity;
import org.missionassetfund.apps.android.interfaces.SaveCommentListener;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.LendingCircleFriends;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.PostType;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GoalPostsAdapter extends ArrayAdapter<Post> {

    private final Goal goal;

    public GoalPostsAdapter(Context context, Goal goal, List<Post> posts) {
        super(context, R.layout.item_lc_post, posts);
        this.goal = goal;

    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post post = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_lc_post, parent, false);

        populatePostViews(post, convertView);

        // comments
        LinearLayout llComments = (LinearLayout) convertView.findViewById(R.id.llComents);
        llComments.removeAllViews();

        List<Comment> comments = (List<Comment>) post.get("comments");
        if (comments != null) {
            for (Comment comment : comments) {
                View commentView = LayoutInflater.from(getContext()).inflate(
                        R.layout.item_lc_comment, parent, false);
                populateCommentView(comment, commentView);
                llComments.addView(commentView);
            }

        }

        // New Comment input
        View newCommentView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_lc_create_comment, parent, false);

        populateNewCommentInputView(newCommentView, post);
        llComments.addView(newCommentView);

        return convertView;
    }

    private void populatePostViews(Post post, View convertView) {
        // Profile Image
        ImageView ivPosterProfile = (ImageView) convertView.findViewById(R.id.ivPosterProfile);
        ivPosterProfile.setImageResource(getImageResourceForUser(post.getUser()));

        // Post type
        TextView tvPostType = (TextView) convertView.findViewById(R.id.tvPostType);
        tvPostType.setText(getPostTypeText(post));
        tvPostType.setBackgroundColor(getBackgroundColorForPostType(post));

        // Post content
        TextView tvPostText = (TextView) convertView.findViewById(R.id.tvPostText);
        tvPostText.setText(post.getContent());

        // Payment Due for reminders
        if (post.getType() != null && post.getType() == PostType.REMINDER) {
            TextView tvPaymentDue = (TextView) convertView.findViewById(R.id.tvPaymentDue);
            tvPaymentDue.setText(CurrencyUtils.getCurrencyValueFormatted(goal.getPaymentsDue()));
        }

    }

    private void populateCommentView(Comment comment, View commentView) {
        // Commenter profile Image
        ImageView ivCommenterProfile = (ImageView) commentView
                .findViewById(R.id.ivCommenterProfile);
        ivCommenterProfile.setImageResource(getImageResourceForUser(comment.getUser()));

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
        ivPosterProfile.setImageResource(getImageResourceForUser((User) User.getCurrentUser()));

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

    private int getBackgroundColorForPostType(Post post) {
        if (post.getType() != null) {
            PostType type = post.getType();
            switch (type) {
                case EVENT:
                    break;
                case MESSAGE:
                    break;
                case QUESTION:
                    break;
                case REMINDER:
                    break;
                default:
                    break;
            }
        }
        return R.color.com_facebook_blue; // TODO create proper color
    }

    private CharSequence getPostTypeText(Post post) {
        if (post.getType() != null) {
            return post.getType().toString();
        }

        return PostType.MESSAGE.toString();
    }

    private int getImageResourceForUser(User user) {
        if (user.getProfileImageId() != 0) {
            return LendingCircleFriends.get(user.getProfileImageId()).getDpDrawableId();
        }
        return R.drawable.profile_11;
    }
}
