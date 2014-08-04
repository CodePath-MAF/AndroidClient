
package org.missionassetfund.apps.android.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.adapters.GoalPostsAdapter;
import org.missionassetfund.apps.android.fragments.NewPostDialog;
import org.missionassetfund.apps.android.fragments.PeopleCircleFragment;
import org.missionassetfund.apps.android.fragments.PeopleCircleFragment.OnCreateViewListener;
import org.missionassetfund.apps.android.fragments.PostDetailDialog;
import org.missionassetfund.apps.android.interfaces.SaveCommentListener;
import org.missionassetfund.apps.android.interfaces.SavePostListener;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.LCDetail;
import org.missionassetfund.apps.android.models.Post;
import org.missionassetfund.apps.android.models.User;
import org.missionassetfund.apps.android.utils.CurrencyUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nirhart.parallaxscroll.views.ParallaxListView;
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
    // FrameLayout rootLayout;
    // FrameLayout circleOfLC;
    List<User> usersOfLC;

    private Goal goal;
    private List<Post> posts;
    private GoalPostsAdapter aposts;
    // ListView llGoalPosts;
    ParallaxListView lvLCDetails;

    FloatingActionButton btnCreatePost;

    private LCDetail mLendingCircleDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_details);

        // Add up action navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        currentUser = (User) User.getCurrentUser();
        posts = new ArrayList<Post>();

        // rootLayout = (FrameLayout) findViewById(R.id.flLCDetails);
        lvLCDetails = (ParallaxListView) findViewById(R.id.lvLCDetails);
        lvLCDetails.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = aposts.getItem(position - 1); // wtf?
                // Has comments?
                List<Comment> comments = (List<Comment>) post.get("comments");
                if (comments != null && comments.size() > 0) {
                    FragmentManager fm = getSupportFragmentManager();
                    PostDetailDialog pdDialog = PostDetailDialog.newInstance(post);
                    pdDialog.show(fm, "fragment_compose");
                } else {
                    // TODO show toast saying no comments
                    Log.d("debug", "No comments! you can open this...");
                }
            }

        });

        // llGoalPosts = (ListView) findViewById(R.id.llGoalPosts);

        btnCreatePost = (FloatingActionButton) findViewById(R.id.btnCreatePost);
        // btnCreatePost.listenTo(llGoalPosts);
        // btnCreatePost.listenTo(lvLCDetails);

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
                    aposts = new GoalPostsAdapter(LCDetailsActivity.this, goal, posts);
                    // llGoalPosts.setAdapter(aposts);

                    getLCDetailsData();
                } else {
                    Toast.makeText(LCDetailsActivity.this, R.string.parse_error_querying,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("InflateParams")
    private void setUpCircle() {
        View circleView = getLayoutInflater().inflate(R.layout.item_lc_circle_view, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        circleView.setLayoutParams(params);

        lvLCDetails.addParallaxedHeaderView(circleView);
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

                        mLendingCircleDetail = mapper.convertValue(result.get("goalDetails"),
                                LCDetail.class);
                        posts.addAll((List<Post>) result.get("posts"));
                        aposts.notifyDataSetChanged();

                        setUpCircle();
                        lvLCDetails.setAdapter(aposts);

                    }
                });
    }

    public void onCreatePost(View v) {
        FragmentManager fm = getSupportFragmentManager();
        NewPostDialog cpDialog = NewPostDialog.newInstance("Create Post");
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
    public void onSetupData(PeopleCircleFragment fragment) {
        if (goal != null && mLendingCircleDetail != null) {
            fragment.setGoalAmount(CurrencyUtils.newCurrency(goal.getAmount()));
            fragment.setGoalPaymentAmount(CurrencyUtils.newCurrency(goal.getPaymentAmount()));
            fragment.setCashOutSchedule(mLendingCircleDetail.getCashOutSchedule());
        }
    }
}
