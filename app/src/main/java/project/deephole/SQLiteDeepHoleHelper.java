package project.deephole;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteDeepHoleHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DeepHoleDB";

	private static final String TABLE_ACCOUNTS = "Accounts";
	private static final String KEY_ACC_ID = "ID";
	private static final String KEY_NAME = "Name";
	private static final String KEY_PASSWORD = "Password";
	private static final String KEY_EMAIL = "EMail";
	private static final String KEY_PHONE = "Phone";
	private static final String KEY_PESEL = "Pesel";
	private static final String[] ACCOUNT_COLUMNS = {KEY_NAME, KEY_PASSWORD, KEY_EMAIL, KEY_PHONE, KEY_PESEL};

	private static final String TABLE_DEEP_HOLE_FORMS = "DeepHoleForms";
	private static final String KEY_ID = "ID";
	private static final String KEY_PHOTO_PATH = "PhotoPath";
	private static final String KEY_DESCRIPTION = "Description";
	private static final String KEY_RECIPIENT = "Recipient";
	private static final String KEY_LOCALIZATION = "Localization";
	private static final String KEY_SIGNATURE = "Signature";
	private static final String KEY_TELEPHONE_NUMBER = "TelephoneNumber";
	private static final String[] COLUMNS = {KEY_ID, KEY_PHOTO_PATH, KEY_DESCRIPTION, KEY_RECIPIENT,
			KEY_LOCALIZATION, KEY_SIGNATURE, KEY_TELEPHONE_NUMBER};

	public SQLiteDeepHoleHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + " ( " +
                KEY_ACC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT, " +
                KEY_PASSWORD + " TEXT, " +
                KEY_EMAIL + " TEXT, " +
                KEY_PHONE + " TEXT, " +
                KEY_PESEL + " TEXT )";

        db.execSQL(CREATE_ACCOUNTS_TABLE);

		String CREATE_DEEP_HOLE_TABLE = "CREATE TABLE DeepHoleForms ( " +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"PhotoPath TEXT, " +
				"Description TEXT ," +
				"Recipient TEXT ," +
				"Localization TEXT ," +
				"Signature TEXT ," +
				"TelephoneNumber TEXT )";

		db.execSQL(CREATE_DEEP_HOLE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS DeepHoleForms");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		this.onCreate(db);
	}

	public void insertForm(Form form){
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = contentValuesBuilder(form);

		db.insert(TABLE_DEEP_HOLE_FORMS, null, values);
		db.close();

		Log.d("insertForm", form.toString());
	}

    public void insertAccountForm(AccountForm form){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = contentValuesBuilderForAccounts(form);

        db.insert(TABLE_ACCOUNTS, null, values);
        db.close();

        Log.d("insertForm", form.toString());
    }

	public Form selectForm(int id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor =
				db.query(TABLE_DEEP_HOLE_FORMS,
                        COLUMNS,
                        KEY_ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null, // group by
                        null, // having
                        null, // order by
                        null); // limit

		if (cursor != null)
			cursor.moveToFirst();
		else {
			Log.d("selectForm", "cursor == null, brak formularza o zadanym id!");
			return null;
		}

		Form form = formBuilder(cursor);

		Log.d("selectForm(" + id + ")", form.toString());
		return form;
	}

	public AccountForm selectAccountForm(int id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor =
				db.query(TABLE_ACCOUNTS,
						ACCOUNT_COLUMNS,
						KEY_ID + " = ?",
						new String[]{String.valueOf(id + 1)},
						null, // group by
						null, // having
						null, // order by
						null); // limit

		if (cursor != null) {
			Log.d("selectAccount", "ID = " + id + 1);
			cursor.moveToFirst();
		} else {
			Log.d("selectAccount", "cursor == null, brak konta o zadanym id!");
			return null;
		}

		AccountForm account = new AccountForm();
		account.setName(cursor.getString(0));
		account.setPassword(cursor.getString(1));
		account.setEmail(cursor.getString(2));
		account.setPhone(cursor.getString(3));
		account.setPesel(cursor.getString(4));

		Log.d("selectAccount(" + id + ")", account.toString());
		return account;
	}

	public ArrayList<Form> getAllForms() {
		ArrayList<Form> forms = new ArrayList<>();

		String query = "SELECT  * FROM " + TABLE_DEEP_HOLE_FORMS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {
				Form form = formBuilder(cursor);
				forms.add(form);
			} while (cursor.moveToNext());
		}

		Log.d("getAllForms()", forms.toString());
		return forms;
	}
    public ArrayList<Hole> getAllMiniHoles(){
        ArrayList<Hole> holes = new ArrayList<>();

        String query = "SELECT " + KEY_PHOTO_PATH + ", " + KEY_DESCRIPTION
                +", " + KEY_LOCALIZATION + " FROM " + TABLE_DEEP_HOLE_FORMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String photoPath = cursor.getString(0);
                String description = cursor.getString(1);
                String location = cursor.getString(2);
                Hole hole = new Hole(photoPath.substring(0,photoPath.length()-4)+"mini.jpg", description, location);
                holes.add(hole);
            } while (cursor.moveToNext());
        }

        Log.d("getAllHoles()", holes.toString());
        return holes;
    }
    public ArrayList<Hole> getAllHoles() {
        ArrayList<Hole> holes = new ArrayList<>();

        String query = "SELECT " + KEY_PHOTO_PATH + ", " + KEY_DESCRIPTION
                +", " + KEY_LOCALIZATION + " FROM " + TABLE_DEEP_HOLE_FORMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String photoPath = cursor.getString(0);
                String description = cursor.getString(1);
                String location = cursor.getString(2);
                Hole hole = new Hole(photoPath, description, location);
                holes.add(hole);
            } while (cursor.moveToNext());
        }

        Log.d("getAllHoles()", holes.toString());
        return holes;
    }

    public ArrayList<AccountForm> getAllAccountForms() {
        ArrayList<AccountForm> forms = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                AccountForm form = accountFormBuilder(cursor);
                forms.add(form);
            } while (cursor.moveToNext());
        }

        Log.d("getAllAccountForms()", forms.toString());
        return forms;
    }

    public AccountForm getAccountByID(int id) {
        ArrayList<AccountForm> ar = getAllAccountForms();
        return ar.get(id);
    }

	public int updateForm(Form form) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = contentValuesBuilder(form);

		int i = db.update(TABLE_DEEP_HOLE_FORMS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(form.getId())});

		db.close();
		return i;
	}

	public void deleteForm(Form form) {
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete(TABLE_DEEP_HOLE_FORMS,
				KEY_ID + " = ?",
				new String[] { String.valueOf(form.getId()) });

		db.close();
		Log.d("deleteForm", form.toString());
	}

    public ContentValues contentValuesBuilder(Form form) {
        ContentValues values = new ContentValues();
        values.put(KEY_PHOTO_PATH, form.getPhotoPath());
        values.put(KEY_DESCRIPTION, form.getDescription());
        values.put(KEY_RECIPIENT, form.getRecipient());
        values.put(KEY_LOCALIZATION, form.getLocalization());
        values.put(KEY_SIGNATURE, form.getSignature());
        values.put(KEY_TELEPHONE_NUMBER, form.getTelephone());

        return values;
    }

    public ContentValues contentValuesBuilderForAccounts(AccountForm form) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, form.getName());
        values.put(KEY_PASSWORD, form.getPassword());
        values.put(KEY_EMAIL, form.getEmail());
        values.put(KEY_PHONE, form.getPhone());
        values.put(KEY_PESEL, form.getPesel());

        return values;
    }

    public Form formBuilder(Cursor cursor) {
        Form form = new Form();
        form.setId(Integer.parseInt(cursor.getString(0)));
        form.setPhotoPath(cursor.getString(1));
        form.setDescription(cursor.getString(2));
        form.setRecipient(cursor.getString(3));
        form.setLocalization(cursor.getString(4));
        form.setSignature(cursor.getString(5));
        form.setTelephone(cursor.getString(6));

        return form;
    }

    public AccountForm accountFormBuilder(Cursor cursor) {
        AccountForm form = new AccountForm();
        form.setId(Integer.parseInt(cursor.getString(0)));
        form.setName(cursor.getString(1));
        form.setPassword(cursor.getString(2));
        form.setEmail(cursor.getString(3));
        form.setPhone(cursor.getString(4));
        form.setPesel(cursor.getString(5));

        return form;
    }
}
