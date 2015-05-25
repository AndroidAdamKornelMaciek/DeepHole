package project.deephole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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


public class RegisterAccountActivity extends Activity {

	private SQLiteDeepHoleHelper db;
	List<AccountForm> forms;

	static final String NAME_KEY = "name";
	static final String PASSWORD_KEY = "password";
	static final String EMAIL_KEY = "email";
	static final String PHONE_KEY = "phone";
	static final String PESEL_LOGIN_KEY = "pesel";
	String name = "";
	String password = "";
	String email = "";
	String phone = "";
	String pesel = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);

		db = new SQLiteDeepHoleHelper(this);
		forms = db.getAllAccountForms();
/*
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

		if (!saved) {
			if (sharedPref.contains(NAME_KEY)) {
				name = sharedPref.getString(NAME_KEY, null);
				((TextView)findViewById(R.id.nameText)).setText(name);
			}
			if (sharedPref.contains(PASSWORD_KEY)) {
				password = sharedPref.getString(PASSWORD_KEY, null);
				((TextView)findViewById(R.id.passwordText)).setText(password);
			}
			if (sharedPref.contains(EMAIL_KEY)) {
				email = sharedPref.getString(EMAIL_KEY, null);
				((TextView)findViewById(R.id.emailText)).setText(email);
			}
			if (sharedPref.contains(PHONE_KEY)) {
				phone = sharedPref.getString(PHONE_KEY, null);
				((TextView)findViewById(R.id.phoneText)).setText(phone);
			}
			if (sharedPref.contains(PESEL_LOGIN_KEY)) {
				pesel = sharedPref.getString(PESEL_LOGIN_KEY, null);
				((TextView)findViewById(R.id.peselText)).setText(pesel);
			}
		}
*/	}

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*	if (!saved) {
				SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();

				name = ((TextView) findViewById(R.id.nameText)).getText().toString();
				password = ((TextView) findViewById(R.id.passwordText)).getText().toString();
				email = ((TextView) findViewById(R.id.emailText)).getText().toString();
				phone = ((TextView) findViewById(R.id.phoneText)).getText().toString();
				pesel = ((TextView) findViewById(R.id.peselText)).getText().toString();

				editor.putString(NAME_KEY, name);
				editor.putString(PASSWORD_KEY, password);
				editor.putString(EMAIL_KEY, email);
				editor.putString(PHONE_KEY, phone);
				editor.putString(PESEL_LOGIN_KEY, pesel);
				editor.apply();
			} else {
            }
		*/
	}

	public void onSave(View view) {
		name = ((TextView)findViewById(R.id.nameText)).getText().toString();
		password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
		email = ((TextView)findViewById(R.id.emailText)).getText().toString();
		phone = ((TextView)findViewById(R.id.phoneText)).getText().toString();
		pesel = ((TextView)findViewById(R.id.peselText)).getText().toString();

		for (AccountForm x : forms) {
			if (x.getName().equals(name)) {
				Toast.makeText(getApplicationContext(), "Account name already used.", Toast.LENGTH_LONG).show();
				return;
			}
		}

        if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Wrong EMail, @ not found!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!email.contains(".")) {
            Toast.makeText(getApplicationContext(), "Wrong EMail, . not found!", Toast.LENGTH_LONG).show();
            return;
        }

		if (pesel.length() != 11) {
			Toast.makeText(getApplicationContext(), "Wrong length of PESEL!", Toast.LENGTH_LONG).show();
			return;
		} else {
			int res = 0;
		//    a+3b+7c+9d+e+3f+7g+9h+i+3j+k
			int tab[] = {1,3,7,9,1,3,7,9,1,3,1};
			for (int i=0; i < 11; i++) {
				res += tab[i] * (pesel.charAt(i) - '0');
			}
			if (res % 10 != 0) {
				Toast.makeText(getApplicationContext(), "Invalid PESEL!", Toast.LENGTH_LONG).show();
				return;
			}
		}

		AccountForm form = new AccountForm(0, name, password, email, phone, pesel);

		Log.d("Wygenerowany form konta", form.toString());

        db.insertAccountForm(form);

		Toast.makeText(getApplicationContext(), "Account created and saved. Please log in", Toast.LENGTH_LONG).show();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(NAME_KEY, name);
        setResult(RESULT_OK, returnIntent);
        finish();
	}
}
