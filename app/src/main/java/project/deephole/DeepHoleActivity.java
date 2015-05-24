package project.deephole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//TO JEST GŁÓWNA AKTYWNOŚĆ PANOWIE, OTWIERA SIĘ PO URUCHOMIENIU

public class DeepHoleActivity extends Activity {
    //kto jest zalogowany, trzeba to przerobic na startactivityforresult, ale nie mam teraz juz sily tego robic
    public static int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deep_hole_layout);

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

	public void onOverview(View view) {
		Intent intent = new Intent(this, OverviewActivity.class);
		startActivity(intent);
	}

    public void onFormulate(View view) {
        Intent intent = new Intent(this, FormActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onRegister(View view) {
        Intent intent = new Intent(this, RegisterAccountActivity.class);
        startActivity(intent);
    }
}
