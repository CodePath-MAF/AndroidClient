
package org.missionassetfund.apps.android.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.missionassetfund.apps.android.R;
import org.missionassetfund.apps.android.R.layout;
import org.missionassetfund.apps.android.adapters.GoalPostsAdapter;
import org.missionassetfund.apps.android.models.CategoryTotal;
import org.missionassetfund.apps.android.models.Chart;
import org.missionassetfund.apps.android.models.Comment;
import org.missionassetfund.apps.android.models.Dashboard;
import org.missionassetfund.apps.android.models.Goal;
import org.missionassetfund.apps.android.models.LCDetail;
import org.missionassetfund.apps.android.models.Post;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LCDetailsActivity extends Activity {
    private static final String TAG = "LCDetails";
    private Goal goal;
    private List<Post> posts;
    private GoalPostsAdapter aposts;
    ListView llGoalPosts;

    TextView tvPaymentDue;
    TextView tvDueDateHuman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_details);
        posts = new ArrayList<Post>();
        aposts = new GoalPostsAdapter(this, R.layout.item_lc_post, posts);

        llGoalPosts = (ListView) findViewById(R.id.llGoalPosts);
        llGoalPosts.setAdapter(aposts);

        tvPaymentDue = (TextView) findViewById(R.id.tvPaymentDue);
        tvDueDateHuman = (TextView) findViewById(R.id.tvDueDateHuman);

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
                    getLCDetailsData();
                } else {
                    Toast.makeText(LCDetailsActivity.this, R.string.parse_error_querying,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected void getLCDetailsData() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());

        params.put("parentGoalId", goal.getParentGoal().getObjectId());
        params.put("goalId", goal.getObjectId());

        ParseCloud.callFunctionInBackground("goalDetailView", params,
                new FunctionCallback<HashMap<String, Object>>() {

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
                            for (Comment comment : comments) {
                                Log.d("debug", comment.getContent());
                            }
                        }

                    }
                });

    }
}
