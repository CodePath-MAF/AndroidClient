package org.missionassetfund.apps.android.models.dao;

import java.util.ArrayList;
import java.util.List;

import org.missionassetfund.apps.android.models.Category;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CategoryDao {

    private static final String TAG = "CategoryDao";
    private ParseQuery<Category> query;
    
    public CategoryDao() {
        query = ParseQuery.getQuery(Category.class);
        query.orderByAscending(Category.NAME_KEY);
    }
    
    public void pin() {
        query.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException exception) {
                if (exception != null) {
                    Log.d(TAG, "Error trying to pin all categories", exception);
                    return;
                }
                
                ParseObject.pinAllInBackground(categories);
            }
        });
    }
    
    public List<Category> getAll() {
        query.fromLocalDatastore();
        try {
            return query.find();
        } catch (ParseException e) {
            Log.d(TAG, "Error querying local datastorage for Categories", e);
            return new ArrayList<Category>();
        }
    }

    public ParseQuery<Category> getLocalQuery() {
        query.fromLocalDatastore();
        return query;
    }
}
