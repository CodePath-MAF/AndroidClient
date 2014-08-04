
package org.missionassetfund.apps.android.adapters;

import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.utils.ModelUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsAdapter extends ArrayAdapter<Comment> {

    public CommentsAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.item_lc_comment, comments);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comment comment = getItem(position);

        // TODO use viewholder
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_lc_comment, parent, false);

        populateCommentView(comment, convertView);

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

}
