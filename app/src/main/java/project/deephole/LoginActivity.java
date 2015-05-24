package project.deephole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {
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
		setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

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
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

        name = ((TextView)findViewById(R.id.nameText)).getText().toString();
        password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
        email = ((TextView)findViewById(R.id.emailText)).getText().toString();
        phone = ((TextView)findViewById(R.id.phoneText)).getText().toString();
        pesel = ((TextView)findViewById(R.id.peselText)).getText().toString();

        editor.putString(NAME_KEY, name);
        editor.putString(PASSWORD_KEY, password);
        editor.putString(EMAIL_KEY, email);
        editor.putString(PHONE_KEY, phone);
        editor.putString(PESEL_LOGIN_KEY, pesel);
        editor.commit();
	}

	public void onSave(View view) {
		name = ((TextView)findViewById(R.id.nameText)).getText().toString();
		password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
		email = ((TextView)findViewById(R.id.emailText)).getText().toString();
		phone = ((TextView)findViewById(R.id.phoneText)).getText().toString();
		pesel = ((TextView)findViewById(R.id.peselText)).getText().toString();


		Toast.makeText(getApplicationContext(), "Account created and saved.", Toast.LENGTH_LONG).show();
	}
}
