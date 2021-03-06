package project.deephole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class RegisterAccountActivity extends Activity {

	private SQLiteDeepHoleHelper db;
	List<AccountForm> forms;

	static final String NAME_KEY = "name";

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void onSave(View view) {
		name = ((TextView)findViewById(R.id.nameText)).getText().toString();
		password = ((TextView)findViewById(R.id.passwordText)).getText().toString();
		email = ((TextView)findViewById(R.id.emailText)).getText().toString();
		phone = ((TextView)findViewById(R.id.phoneText)).getText().toString();
		pesel = ((TextView)findViewById(R.id.peselText)).getText().toString();

		for (AccountForm x : forms) {
			if (x.getName().equals(name)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.nameDuplicated),
						Toast.LENGTH_LONG).show();
				return;
			}
		}

        if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrongMail),
					Toast.LENGTH_LONG).show();
            return;
        }
        if (!email.contains(".")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrongMail),
					Toast.LENGTH_LONG).show();
            return;
        }

		if (pesel.length() != 11) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrongPeselLength),
					Toast.LENGTH_LONG).show();
			return;
		} else {
			int res = 0;
			int tab[] = {1,3,7,9,1,3,7,9,1,3,1};
			for (int i=0; i < 11; i++) {
				res += tab[i] * (pesel.charAt(i) - '0');
			}
			if (res % 10 != 0) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrongPesel),
						Toast.LENGTH_LONG).show();
				return;
			}
		}

		AccountForm form = new AccountForm(0, name, password, email, phone, pesel);

		Log.d("Wygenerowany form konta", form.toString());

        db.insertAccountForm(form);

		Toast.makeText(getApplicationContext(), getResources().getString(R.string.accountCreated),
				Toast.LENGTH_LONG).show();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(NAME_KEY, name);
        setResult(RESULT_OK, returnIntent);
        finish();
	}
}
