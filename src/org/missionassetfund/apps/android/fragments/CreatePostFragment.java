
package org.missionassetfund.apps.android.fragments;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.interfaces.SavePostListener;

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

public class CreatePostFragment extends DialogFragment {

    private EditText etPost;
    private ImageButton btnPost;

    public CreatePostFragment() {
        // Empty constructor required for DialogFragment
    }

    public static CreatePostFragment newInstance(String title) {
        CreatePostFragment frag = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container);
        etPost = (EditText) view.findViewById(R.id.etPost);
        btnPost = (ImageButton) view.findViewById(R.id.btnPost);
        // String title = getArguments().getString("title", "Your Tweet");
        // getDialog().setTitle(title);
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

}
