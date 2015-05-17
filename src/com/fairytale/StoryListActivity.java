package com.fairytale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StoryListActivity extends Activity implements StoryListLayout.Listener {
	private DatabaseAccessModel dba;
	private StoryListLayout layout;
	private StoryListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLog.d("Story List Activity is started!");
        dba = new DatabaseAccessModel(this);
        layout = new StoryListLayout(this);
        model = new StoryListModel(this,dba);
        
        layout.setListener(this);
        layout.setModel(model);
        layout.genList(this);
        setContentView(layout.getView());
    }

	@Override
	public void goAdditionalStory() {
		mLog.i("Change to Additional Story Activity");
		startActivity(new Intent(StoryListActivity.this, AdditionalStoryListActivity.class));
	}

	@Override
	public void startStory(int story_id) {
		Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
		intent.putExtra(StoryActivity.STORY_ID,story_id);
		startActivity(intent);
	}
}
