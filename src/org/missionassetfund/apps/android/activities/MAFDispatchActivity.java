package org.missionassetfund.apps.android.activities;

import com.parse.ui.ParseLoginDispatchActivity;

public class MAFDispatchActivity extends ParseLoginDispatchActivity {

	@Override
	protected Class<?> getTargetClass() {
		return MainActivity.class;
	}

}
