package project.deephole;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

//TO JEST GŁÓWNA AKTYWNOŚĆ PANOWIE, OTWIERA SIĘ PO URUCHOMIENIU

public class DeepHoleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_hole_layout);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_deep_hole, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onOverview(View view) {
		Intent intent = new Intent(this, OverviewActivity.class);
		startActivity(intent);
	}

	public void onFormulate(View view) {
		Intent intent = new Intent(this, FormActivity.class);
		startActivity(intent);
	}

	public void onDraft(View view) {
		//metoda uruchamia aktywność, w której użytkownik może wypełnić ostatni nieskończony formularz
	}
}
