package project.deephole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class DeepHoleActivity extends Activity {
    private int id;
    static final String KEY_LOG_ID = "accLogId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_hole_layout);

        SharedPreferences sharedPref = getSharedPreferences("shpr",Context.MODE_PRIVATE);
        id = sharedPref.getInt(KEY_LOG_ID, -1);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
    }

	public void onOverview(View view) {
		Intent intent = new Intent(this, OverviewActivity.class);
		startActivity(intent);
	}

    public void onFormulate(View view) {
        Intent intent = new Intent(this, FormActivity.class);
        startActivity(intent);
    }

    public void onLogout(View view) {
        SharedPreferences sharedPref = getSharedPreferences("shpr", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(KEY_LOG_ID, -1);
        editor.apply();

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
