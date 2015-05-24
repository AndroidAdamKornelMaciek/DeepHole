package project.deephole;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;


public class LoginActivity extends Activity {
	SQLiteDeepHoleHelper db;
	List<AccountForm> forms;
    static final String KEY_ACCTUAL_ACCOUNT = "accAcc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		db = new SQLiteDeepHoleHelper(this);

		forms = db.getAllAccountForms();
		Log.d("Login","Mam konta");
		String res = "";
		for (AccountForm x : forms) {
			res += x.getId() + " " + x.getName() + "\n";
		}

		((TextView)findViewById(R.id.textView)).setText(res);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_login, menu);
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

	public void onLogin(View view) {
		String login = ((TextView)findViewById(R.id.nameText)).getText().toString();
		String password = ((TextView)findViewById(R.id.passwordText)).getText().toString();

		boolean logged = false;
		for (AccountForm x : forms) {
			if (x.getName().equals(login)) {
				if (x.getPassword().equals(password)) {
                    DeepHoleActivity.id = (int)x.getId() - 1;
					Toast.makeText(this, "Logged In, ID = " + DeepHoleActivity.id, Toast.LENGTH_LONG).show();

					finish();
                    return;
				} else {
                    Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show();
                    ((TextView)findViewById(R.id.passwordText)).setText("");
                    return;
				}
			}
		}
        Toast.makeText(this, "Name not found", Toast.LENGTH_LONG).show();
    }

	public void onLogout(View view) {
        DeepHoleActivity.id = -1;
	}
}
