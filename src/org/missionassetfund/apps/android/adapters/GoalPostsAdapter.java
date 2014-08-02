
package org.missionassetfund.apps.android.adapters;

import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Input;
import org.missionassetfund.apps.android.models.Post;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GoalPostsAdapter extends ArrayAdapter<Post> {

    public GoalPostsAdapter(Context context, List<Post> posts) {
        super(context, R.layout.item_lc_post, posts);

    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Post post = getItem(position);

        // if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lc_post, parent,
                false);
        // }

        TextView tvPostText = (TextView) convertView.findViewById(R.id.tvPostText);
        tvPostText.setText(post.getContent());

        LinearLayout llComments = (LinearLayout) convertView.findViewById(R.id.llComents);
        llComments.removeAllViews();

        List<Comment> comments = (List<Comment>) post.get("comments");
        if (comments != null) {
            for (Comment comment : comments) {
                View commentView = LayoutInflater.from(getContext()).inflate(
                        R.layout.item_lc_comment, parent, false);
                TextView tvCommentText = (TextView)
                        commentView.findViewById(R.id.tvCommentText);
                tvCommentText.setText(comment.getContent());
                llComments.addView(commentView);
            }

        }

        View newCommentView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_lc_create_comment, parent, false);
        llComments.addView(newCommentView);

        return convertView;
    }

}
