
package org.missionassetfund.apps.android.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.GoalPostsAdapter;
import org.missionassetfund.apps.android.fragments.CreatePostFragment;
import org.missionassetfund.apps.android.fragments.PeopleCircleFragment;
import org.missionassetfund.apps.android.fragments.PeopleCircleFragment.OnCreateViewListener;
import org.missionassetfund.apps.android.interfaces.SaveCommentListener;
import org.missionassetfund.apps.android.interfaces.SavePostListener;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.os.Bundle;
import android.provider.Contacts.PeopleColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LCDetailsActivity extends FragmentActivity implements SavePostListener,
        SaveCommentListener, OnCreateViewListener {
    private static final String TAG = "LCDetails";

    User currentUser;

    FrameLayout circleOfLC;
    List<User> usersOfLC;

    private Goal goal;
    private List<Post> posts;
    private GoalPostsAdapter aposts;
    ListView llGoalPosts;

    FloatingActionButton btnCreatePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_details);

        currentUser = (User) User.getCurrentUser();

        circleOfLC = (FrameLayout) findViewById(R.id.circleOfLC);
        setUpCircle();

        posts = new ArrayList<Post>();

        llGoalPosts = (ListView) findViewById(R.id.llGoalPosts);

        btnCreatePost = (FloatingActionButton) findViewById(R.id.btnCreatePost);
        btnCreatePost.listenTo(llGoalPosts);

        // TODO This is temporary
        String goalId = getIntent().getStringExtra(Goal.GOAL_KEY);
        ParseQuery<Goal> query = ParseQuery.getQuery(Goal.class);
        query.getInBackground(goalId, new GetCallback<Goal>() {

            @Override
            public void done(Goal g, ParseException e) {
                if (e == null) {
                    goal = g;
                    // Setup Activity title base on the goal name
                    setTitle(g.getName());
                    // call parseCloud to get all data elements for lending
                    // circle view
                    aposts = new GoalPostsAdapter(LCDetailsActivity.this, goal, posts);
                    llGoalPosts.setAdapter(aposts);

                    getLCDetailsData();
                } else {
                    Toast.makeText(LCDetailsActivity.this, R.string.parse_error_querying,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setUpCircle() {
        int numViews = 8;
        for (int i = 0; i < numViews; i++)
        {
            // Create some quick TextViews that can be placed.
            ImageView v = new ImageView(this);
            // Set a text and center it in each view.
            // v.setBackgroundColor(0xffff0000);
            v.setImageResource(R.drawable.profile_1);
            // Force the views to a nice size (150x100 px) that fits my display.
            // This should of course be done in a display size independent way.
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(150, 100);
            // Place all views in the center of the layout. We'll transform them
            // away from there in the code below.
            lp.gravity = Gravity.CENTER;
            // Set layout params on view.
            v.setLayoutParams(lp);

            // Calculate the angle of the current view. Adjust by 90 degrees to
            // get View 0 at the top. We need the angle in degrees and radians.
            float angleDeg = i * 360.0f / numViews - 90.0f;
            float angleRad = (float) (angleDeg * Math.PI / 180.0f);
            // Calculate the position of the view, offset from center (300 px
            // from center). Again, this should be done in a display size
            // independent way.
            v.setTranslationX(200 * (float) Math.cos(angleRad));
            v.setTranslationY(200 * (float) Math.sin(angleRad));
            // Set the rotation of the view.
            // v.setRotation(angleDeg + 90.0f);
            circleOfLC.addView(v);
        }

    }

    protected void getLCDetailsData() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());

        params.put("parentGoalId", goal.getParentGoal().getObjectId());
        params.put("goalId", goal.getObjectId());

        ParseCloud.callFunctionInBackground("goalDetailView", params,
                new FunctionCallback<HashMap<String, Object>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void done(HashMap<String, Object> result, ParseException exception) {
                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(Include.NON_NULL);

                        // final LCDetail lcDetail = mapper.convertValue(result,
                        // LCDetail.class);
                        Toast.makeText(LCDetailsActivity.this, "Hi", Toast.LENGTH_LONG).show();
                        Log.d("debug", result.toString());
                        posts.addAll((List<Post>) result.get("posts"));
                        aposts.notifyDataSetChanged();

                        for (Post post : posts) {
                            List<Comment> comments = (List<Comment>) post.get("comments");
                            if (comments != null) {
                                for (Comment comment : comments) {
                                    Log.d("debug", comment.getContent());
                                }

                            }
                        }

                    }
                });
    }

    public void onCreatePost(View v) {
        FragmentManager fm = getSupportFragmentManager();
        CreatePostFragment cpDialog = CreatePostFragment.newInstance("Create Post");
        cpDialog.show(fm, "fragment_compose");
    }

    @Override
    public void onSavePost(String inputText) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());

        // parent goal id
        params.put("goalId", goal.getParentGoal().getObjectId());
        params.put("content", inputText);
        // params.put("type", inputText);
        // params.put("toUserId", goal.getObjectId());

        ParseCloud.callFunctionInBackground("createPost", params,
                new FunctionCallback<HashMap<String, Object>>() {

                    @Override
                    public void done(HashMap<String, Object> result, ParseException exception) {
                        if (exception == null) {
                            final ObjectMapper mapper = new ObjectMapper();
                            mapper.setSerializationInclusion(Include.NON_NULL);
                            Log.d("debug", result.toString());
                            // add it to adapter
                            boolean success = (Boolean) result.get("success");
                            if (success) {
                                Post post = (Post) result.get("post");
                                aposts.insert(post, 0);
                            }
                        }
                    }
                });
    }

    @Override
    public void onSaveComment(String postId, String inputText) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());

        params.put("postId", postId);
        params.put("content", inputText);
        // params.put("type", inputText);
        // params.put("toUserId", goal.getObjectId());

        ParseCloud.callFunctionInBackground("createComment", params,
                new FunctionCallback<HashMap<String, Object>>() {

                    @Override
                    public void done(HashMap<String, Object> result, ParseException exception) {
                        if (exception == null) {
                            final ObjectMapper mapper = new ObjectMapper();
                            mapper.setSerializationInclusion(Include.NON_NULL);
                            Log.d("debug", result.toString());
                            // add it to adapter
                            boolean success = (Boolean) result.get("success");
                            if (success) {
                                Post post = (Post) result.get("comment");
                                post.setUser(currentUser);
                                aposts.remove(post);
                                aposts.insert(post, 0);
                            }
                        }
                    }
                });

    }

    @Override
    public void onCreateView(PeopleCircleFragment fragment) {
        fragment.setGoalAmount(CurrencyUtils.newCurrency(1350d));
        fragment.setGoalPaymentAmount(CurrencyUtils.newCurrency(300d));
        fragment.setTotalPeopleOnCircle(8);
    }
}
