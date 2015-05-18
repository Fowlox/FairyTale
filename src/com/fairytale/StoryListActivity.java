package com.fairytale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

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
        mLog.d("model OK");
        layout.setListener(this);
        layout.setModel(model);
        layout.genList(this);
        setContentView(layout.getView());
    }

	@Override
	public void goAdditionalStory() {
		mLog.i("Change to Additional Story Activity");
		//startActivity(new Intent(StoryListActivity.this, AdditionalStoryListActivity.class));
		Toast toast = Toast.makeText(getApplicationContext(), "踰좏� 踰꾩쟾�뿉�꽌�뒗 �젣怨듬릺吏� �븡�뒿�땲�떎.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER,0,0);
		toast.show();
	}

	@Override
	public void startStory(int story_id) {
		Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
		intent.putExtra(StoryActivity.STORY_ID,story_id);
		startActivity(intent);
	}
}
