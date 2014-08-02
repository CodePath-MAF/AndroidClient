
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
import org.missionassetfund.apps.android.models.User;

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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LCDetailsActivity extends Activity {
    private static final String TAG = "LCDetails";

    FrameLayout circleOfLC;
    List<User> usersOfLC;

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

        circleOfLC = (FrameLayout) findViewById(R.id.circleOfLC);
        setUpCircle();

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
