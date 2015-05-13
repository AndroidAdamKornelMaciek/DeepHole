package project.deephole;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//KLASA ODPOWIADA ZA KOMUNIKACJĘ Z BAZĄ DANYCH
public class SQLiteDeepHoleHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DeepHoleDB";

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
		String CREATE_DEEP_HOLE_TABLE = "CREATE TABLE DeepHoleForms ( " +
				"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"PhotoPath TEXT, " +
				"Description TEXT )" +
				"Recipient TEXT )" +
				"Localization TEXT )" +
				"Signature TEXT )" +
				"TelephoneNumber INTEGER )";

		db.execSQL(CREATE_DEEP_HOLE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS DeepHoleForms");
		this.onCreate(db);
	}

	public void addForm(Form form){
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PHOTO_PATH, form.getPhotoPath());
		values.put(KEY_DESCRIPTION, form.getDescription());
		values.put(KEY_RECIPIENT, form.getRecipient());
		values.put(KEY_LOCALIZATION, form.getLocalization());
		values.put(KEY_SIGNATURE, form.getSignature());
		values.put(KEY_TELEPHONE_NUMBER, form.getTelephone());

		db.insert(TABLE_DEEP_HOLE_FORMS, null, values);
		db.close();

		Log.d("addForm", form.toString());
	}

	public Form getForm(int id){
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor =
				db.query(TABLE_DEEP_HOLE_FORMS,
						COLUMNS,
						" ID = ?",
						new String[] { String.valueOf(id) },
						null, // group by
						null, // having
						null, // order by
						null); // limit

		if (cursor != null)
			cursor.moveToFirst();

		Form form = new Form();
		form.setId(Integer.parseInt(cursor.getString(0)));
		form.setPhotoPath(cursor.getString(1));
		form.setDescription(cursor.getString(2));
		form.setRecipient(cursor.getString(3));
		form.setLocalization(cursor.getString(4));
		form.setSignature(cursor.getString(5));
		form.setTelephone(Integer.parseInt(cursor.getString(6)));

		Log.d("getBook("+id+")", form.toString());

		return form;
	}
}
