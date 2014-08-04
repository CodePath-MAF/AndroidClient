
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.SavePostListener;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.ModelUtils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NewPostDialog extends DialogFragment {

    private EditText etPost;
    private ImageButton btnPost;
    private ImageView ivUserProfile;
    private TextView tvUserName;

    public NewPostDialog() {
        // Empty constructor required for DialogFragment
    }

    public static NewPostDialog newInstance(String title) {
        NewPostDialog frag = new NewPostDialog();
        frag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container);

        ivUserProfile = (ImageView) view.findViewById(R.id.ivUserProfile);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        etPost = (EditText) view.findViewById(R.id.etPost);
        btnPost = (ImageButton) view.findViewById(R.id.btnPost);
        btnPost.setEnabled(false);
        etPost.addTextChangedListener(watcher);
        
        User user = (User) User.getCurrentUser();
        tvUserName.setText(user.getName());
        ivUserProfile.setImageResource(ModelUtils.getImageResourceForUser(user));

        // Show soft keyboard automatically
        etPost.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnPost.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SavePostListener listener = (SavePostListener) getActivity();
                listener.onSavePost(etPost.getText().toString());
                dismiss();
            }
        });

        return view;
    }
    
    private TextWatcher watcher = new TextWatcher() {
        
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
            btnPost.setEnabled(totalCharacters > 0 ? true : false);
        }
    };


}
