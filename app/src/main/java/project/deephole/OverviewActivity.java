package project.deephole;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class OverviewActivity extends ListActivity {

	static final String PATH_KEY = "path";
	static final String DESC_KEY = "description";
	static final String LOCAL_KEY = "localization";
	static final String READ_LOCAL_KEY = "readable_localization";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview_layout);
		Log.d("DEBUG", "overview oncreate");
		SQLiteDeepHoleHelper db = new SQLiteDeepHoleHelper(this);

		ListView listView = getListView();
		final ArrayList<Hole> holes = db.getAllMiniHoles();
		OverviewArrayAdapter adapter = new OverviewArrayAdapter(this, holes);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
									int position, long id) {
				Hole hole = holes.get(position);
				Intent fullScreen = new Intent(getApplicationContext(), FullScreenHoleActivity.class);

				fullScreen.putExtra(PATH_KEY, hole.getPhotoPath());
				fullScreen.putExtra(DESC_KEY, hole.getDesc());
				fullScreen.putExtra(LOCAL_KEY, hole.getLocation());
				fullScreen.putExtra(READ_LOCAL_KEY, hole.getReadableLocation());

				startActivity(fullScreen);
			}

		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
}
