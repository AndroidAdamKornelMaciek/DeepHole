package project.deephole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//TO JEST GŁÓWNA AKTYWNOŚĆ PANOWIE, OTWIERA SIĘ PO URUCHOMIENIU

public class DeepHoleActivity extends Activity {
    private int id;
    static final String KEY_LOG_ID = "accLogId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_hole_layout);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt(KEY_LOG_ID, -1);

		/*SQLiteDeepHoleHelper db = new SQLiteDeepHoleHelper(this);
		ArrayList<Form> forms = db.getAllForms();
		for(Form f: forms)
			db.deleteForm(f);*/
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
        intent.putExtra(KEY_LOG_ID, id);
        startActivity(intent);
    }

    public void onLogout(View view) {
        Intent returnIntent = new Intent();
        setResult(RESULT_FIRST_USER, returnIntent);
        finish();
    }
}
