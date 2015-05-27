package project.deephole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends Activity {
	SQLiteDeepHoleHelper db;
	List<AccountForm> forms;
	static final int EXIT_CODE = -1;
	static final int REGISTER_CODE = 1;
	static final String KEY_LOG_ID = "accLogId";
	static final String NAME_KEY = "name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		db = new SQLiteDeepHoleHelper(this);
		populateAccountsList();

		SharedPreferences sp = getSharedPreferences("shpr", Context.MODE_PRIVATE);

        int id = sp.getInt(KEY_LOG_ID, -1);
        if (id != -1) {
            login(id);
        }
	}

    @Override
    protected void onRestart() {
        super.onRestart();
        ((TextView)findViewById(R.id.nameText)).setText("");
        ((TextView)findViewById(R.id.passwordText)).setText("");

        SharedPreferences sp = getSharedPreferences("shpr", Context.MODE_PRIVATE);

        int id = sp.getInt(KEY_LOG_ID, -1);
        if (id != -1) {
            finish();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("tag", requestCode + " " + resultCode);
		if (requestCode == EXIT_CODE) {
			if (resultCode == RESULT_OK) {
                Log.d("log", "user logged out");
			} else {
                finish();
            }
		} else if (requestCode == REGISTER_CODE) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(NAME_KEY);
                ((TextView) findViewById(R.id.nameText)).setText(name);
                populateAccountsList();
            }
		} else {
            finish();
        }
	}

	public void populateAccountsList() {
		forms = db.getAllAccountForms();
		Log.d("Login","Mam konta");
		String res = "";
		for (AccountForm x : forms) {
			res += x.getId() + " " + x.getName() + "\n";
		}

		((TextView)findViewById(R.id.textView)).setText("");
	}

	public void onLogin(View view) {
		String login = ((TextView)findViewById(R.id.nameText)).getText().toString();
		String password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
		populateAccountsList();

		for (AccountForm x : forms) {
			if (x.getName().equals(login)) {
				if (x.getPassword().equals(password)) {
					login((int)(x.getId()-1));
					return;
				} else {
					Toast.makeText(this, getResources().getString(R.string.wrongPassword),
							Toast.LENGTH_LONG).show();
					((TextView)findViewById(R.id.passwordText)).setText("");
					return;
				}
			}
		}
		Toast.makeText(this, getResources().getString(R.string.wrongName),
				Toast.LENGTH_LONG).show();
	}

	public void onRegister(View view) {
		Intent intent = new Intent(this, RegisterAccountActivity.class);
		startActivityForResult(intent, REGISTER_CODE);
	}

	private void login(int id) {
		SharedPreferences sharedPref = getSharedPreferences("shpr", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.putInt(KEY_LOG_ID, id);
		editor.apply();

		AccountForm account = db.selectAccountForm(id);

		Toast.makeText(this, getResources().getString(R.string.loggedIn)
				+ " " + account.getName(), Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, DeepHoleActivity.class);
		startActivityForResult(intent, EXIT_CODE);
	}
}