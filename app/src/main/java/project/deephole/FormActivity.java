package project.deephole;

//AKTYWNOŚĆ FORMULARZA, TUTAJ MOŻLIWOŚĆ DODANIA OPISU, ZDJĘCIA, WYBÓR ADRESATA I OKREŚLENIE GEOLOKALIZACJI

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Aktywność przeznaczona do zgłaszania ubytków w nawierzchni bitumicznej. W tej aktywności, można zrobić zdjęcie, dodać mu opis, ustalić pozycję oraz się podpisać i wysłać zgłoszenie mailem.
 * TODO ConnectionCallbacks,
 * TODO OnConnecttionFailedListener
 *
 * @version 1.0
 */
public class FormActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
	/**
	 * Stała przechowująca wartość request code dla akcji zdjęcie.
	 */
	static final int REQUEST_TAKE_PHOTO = 1;
	/**
	 * Stała przechowująca wartość request code dla akcji lokalizacja.
	 */
	static final int REQUEST_LOCATION = 2;
	/**
	 * Stała przechowująca wartość (String) klucza do Bundle. Wykorzystywana w onSaveInstanceState() oraz w onCreate().
	 */
	static final String ADDED_KEY = "added";
	/**
	 * Stała przechowująca wartość (String) klucza do Bundle. Wykorzystywana w onSaveInstanceState() oraz w onCreate().
	 */
	static final String PATH_KEY = "path";

	static final String DESC_KEY = "description";
	static final String RECIPIENT_KEY = "recipient";
	static final String LOC_PREF_KEY = "localization";
	static final String PESEL_KEY = "pesel";

	private SQLiteDeepHoleHelper db;
	/**
	 * Pole przechowujące ścieżkę zdjęcia, które zostało zrobione.
	 */
	private String currentPhotoPath;
	/**
	 * W photoPreview wyświetlamy miniaturę zdjęcia, które zostało wykonane.
	 */
	private ImageView photoPreview;
	/**
	 * Pole, w które użytkownik wpisuje opis dziury.
	 */
	private EditText descriptEditor;
	/**
	 * Pole, w którym użytkownik się podpisuje.
	 */
	private EditText signEditor;
	/**
	 * Spinner zawierający listę odbiorców maila.
	 */
	private Spinner recipientList;
	/**
	 * Boolean przechowujący wybór użytkownika. Wybór lokalizacji manualny lub automatyczny.
	 */
	private boolean manual;
	/**
	 * TODO
	 */
	private GoogleApiClient mGoogleApiClient;
	/**
	 * TODO
	 */
	private Location mLastKnownLocation;
	/**
	 * Boolean przechowujący flagę, czy zostało już wykonane zdjęcie do raportu.
	 */
	private boolean pictureAdded;
	/**
	 * Przechowuje lokalizację.
	 */
	LatLng location;

	/**
	 * W metodzie onCreate() polom przypisywane są odpowiednie referencje. Zastosowany został też wzorzec treeObserver. W związku z tym, że ImageView długo ma wymiary 0px x 0px
	 * nie jest możliwe przeskalowanie zdjęcia. Żeby użytkownik nie wysypał aplikacji zbyt dużym zdjęciem, dodajemy do imageView onGlobalLayoutListener. Jego metoda onGlobalLayout()
	 * wywołuje się, gdy layout robi się globalny. Aby kod w metodzie nie wywoływał się ciągle, pierwsza rzecz jaką robimy to usuwamy listenera, a potem wypełniamy photoPreview.
	 *
	 * @param savedInstanceState Bundle, który przy pierwszym tworzeniu aktywności jest nullem, a później przechowuje stan aplikacji sprzed zabicia.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_layout);
		db = new SQLiteDeepHoleHelper(this);
		pictureAdded = false;
		photoPreview = (ImageView) findViewById(R.id.hole_photo);
		photoPreview.setImageResource(R.drawable.camera_icon);

		descriptEditor = (EditText) findViewById(R.id.description);
		signEditor = (EditText) findViewById(R.id.sender_signature);

		recipientList = (Spinner) findViewById(R.id.recipient_list);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.recipient_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		recipientList.setAdapter(adapter);

		RadioGroup localizationMenu = (RadioGroup) findViewById(R.id.localization_menu);
		localizationMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				manual = (checkedId == R.id.manual_radio_button);
			}
		});

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		if(sharedPref.contains(ADDED_KEY))
			pictureAdded = sharedPref.getBoolean(ADDED_KEY, false);
		if (sharedPref.contains(PATH_KEY) && pictureAdded) {
			currentPhotoPath = sharedPref.getString(PATH_KEY, null);
			photoPreview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					// usunac observer, zeby odpalil tylko raz
					photoPreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);

					setPic();
				}
			});
		}
		if (sharedPref.contains(DESC_KEY))
			descriptEditor.setText(sharedPref.getString(DESC_KEY, ""));
		if (sharedPref.contains(RECIPIENT_KEY)) {
			int recipient = sharedPref.getInt(RECIPIENT_KEY, 0);
			Spinner recipients = (Spinner) findViewById(R.id.recipient_list);
			recipients.setSelection(recipient);
		}
		if (sharedPref.contains(LOC_PREF_KEY)) {
			int pref = sharedPref.getInt(LOC_PREF_KEY, 0);
			RadioGroup locMenu = (RadioGroup) findViewById(R.id.localization_menu);
			locMenu.check(pref);
		}
		if (sharedPref.contains(PESEL_KEY))
			signEditor.setText(sharedPref.getString(PESEL_KEY, ""));


		if (savedInstanceState == null) {
			return;
		}
		if (savedInstanceState.containsKey(ADDED_KEY)) {
			if (savedInstanceState.getBoolean(ADDED_KEY)) {
				try {
					currentPhotoPath = savedInstanceState.getString(PATH_KEY);

					photoPreview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							// usunac observer, zeby odpalil tylko raz
							photoPreview.getViewTreeObserver().removeOnGlobalLayoutListener(this);

							setPic();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			setPic();
//PÓKI CO NIE KASOWAĆ
			/*File photo = new File(currentPhotoPath);
			Bitmap bmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
			photoPreview.setImageBitmap(bmap);*/
			//photoPreview.setImageURI(Uri.fromFile(photo));

			/*Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			photoPreview.setImageBitmap(imageBitmap);*/
		} else if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
			Bundle coordinates = data.getParcelableExtra(LocationActivity.COORDINATES_KEY);
			location = coordinates.getParcelable(LocationActivity.LOCATION_KEY);

			Log.d("Powrót z aktywności map", location.toString());
			sendEmail();
			/*
			String desc = descriptEditor.getText().toString();
			String recipient = recipientList.getSelectedItem().toString();
			String signature = signEditor.getText().toString();
			TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = tMgr.getLine1Number();

			//uwaga: id ustawiamy na zero, bo jest kluczem autoinkrementującym się, czyli bez zmartwień
			Form form = new Form(0, currentPhotoPath, desc, recipient, location.toString(), signature, phoneNumber);

			Log.d("Wygenerowany formularz", form.toString());

			db.insertForm(form);

			//wysyłanie maila
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("text/plain");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pothole form");
			emailIntent.putExtra(Intent.EXTRA_TEXT, "Description: " + desc + "\n"
					+ "Localization: " + location.toString() + "\n"
					+ "Contact: " + phoneNumber + "\n"
					+ "Signature: " + signature);

			File root = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_PICTURES);
			//File file = new File(root, currentPhotoPath);
			File file = new File(currentPhotoPath);
			if (!file.exists() || !file.canRead()) {
				Log.d("Wczytywanie zdjęcia", "brak istniejącego pliku");
				return;
			}
			Uri uri = Uri.fromFile(file);
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

			try {
				startActivity(Intent.createChooser(emailIntent, "sending mail"));
				Toast.makeText(getApplicationContext(), "Form was sent successfully.",
						Toast.LENGTH_LONG).show();
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(getApplicationContext(), "There is no mail client installed.",
						Toast.LENGTH_LONG).show();
			}
			finish();
			*/
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.reset) {
			resetForm();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if(currentPhotoPath != null)
			editor.putString(PATH_KEY, currentPhotoPath);

		editor.putString(DESC_KEY, descriptEditor.getText().toString());

		Spinner spinner = (Spinner) findViewById(R.id.recipient_list);
		int recipient = spinner.getSelectedItemPosition();
		editor.putInt(RECIPIENT_KEY, recipient);

		RadioGroup locMenu = (RadioGroup) findViewById(R.id.localization_menu);
		int locPref = locMenu.getCheckedRadioButtonId();
		editor.putInt(LOC_PREF_KEY, locPref);

		editor.putString(PESEL_KEY, signEditor.getText().toString());
		editor.putBoolean(ADDED_KEY, pictureAdded);
		editor.commit();
	}

	/**
	 * TODO
	 * @param view
	 */
	public void takePhoto(View view) {
		dispatchPhotoIntent();
	}

	/**
	 * TODO
	 */
	private void dispatchPhotoIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(getPackageManager()) != null) {

			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				Log.d("dispatchPhotoIntent", "Problem z utworzeniem pliku!");
			}

			if (photoFile != null) {
				Uri currentPhotoUri = Uri.fromFile(photoFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						currentPhotoUri);
				startActivityForResult(intent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	/**
	 * TODO
	 * @return
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);

		currentPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * Metoda, która przeskalowuje zdjęcie w celu zapobiegnięcia zapełnieniu pamięci gigantyczną grafiką.
	 * Ustawia w photoPreview zdjęcie, które jest zapisane w currentPhotoPath
	 */
	private void setPic() {
		if(currentPhotoPath == null) {
			photoPreview.setImageResource(R.drawable.camera_icon);
			return;
		}

		int targetW = photoPreview.getWidth();
		int targetH = photoPreview.getHeight();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
		photoPreview.setImageBitmap(bitmap);
		pictureAdded = true;
	}

	/**
	 * Metoda sprawdzająca przed wysłaniem maila czy wszystkie dane są poprawne.
	 * @param view
	 */
	public void validateMailData(View view) {
		String recipient = recipientList.getSelectedItem().toString();

		if (currentPhotoPath == null) {
			Toast.makeText(getApplicationContext(), "Please take a picture!",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (recipient.equals("Recipient")) {
			Toast.makeText(getApplicationContext(), "Please choose a recipient!",
					Toast.LENGTH_LONG).show();
			return;
		}

		String pesel = signEditor.getText().toString();
		if (pesel.length() != 11) {
			Toast.makeText(getApplicationContext(), "Wrong length of PESEL!",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			int res = 0;
		//    a+3b+7c+9d+e+3f+7g+9h+i+3j+k
            int tab[] = {1,3,7,9,1,3,7,9,1,3,1};
			for (int i=0; i < 11; i++) {
                res += tab[i] * (pesel.charAt(i) - '0');
			}
            if (res % 10 != 0) {
                Toast.makeText(getApplicationContext(), "Invalid PESEL!",
                        Toast.LENGTH_LONG).show();
                return;
            }
		}

		if (manual) {
			Intent intent = new Intent(this, LocationActivity.class);
			startActivityForResult(intent, REQUEST_LOCATION);
		} else {
			if (mGoogleApiClient == null) {
				mGoogleApiClient = new GoogleApiClient.Builder(this)
						.addConnectionCallbacks(this)
						.addOnConnectionFailedListener(this)
						.addApi(LocationServices.API).build();
			}
			mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

			if (mLastKnownLocation != null) {
				//nie można pobrać lokalizacji nawet przy włączonym GPS
				Log.d("Lokalizacja", mLastKnownLocation.toString());
			} else {
				Intent intent = new Intent(this, LocationActivity.class);
				startActivityForResult(intent, REQUEST_LOCATION);
				Toast.makeText(getApplicationContext(), "Can't fetch your last location.",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	/**
	 * Metoda wysyłająca startująca chosera ACTION_SEND oraz pakująca zgłoszenie do bazy danych.
	 */
	public void sendEmail(){
		String desc = descriptEditor.getText().toString();
		String recipient = recipientList.getSelectedItem().toString();
		String signature = signEditor.getText().toString();
		TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();

		//uwaga: id ustawiamy na zero, bo jest kluczem autoinkrementującym się, czyli bez zmartwień
		Form form = new Form(0, currentPhotoPath, desc, recipient, location.toString(), signature, phoneNumber);

		Log.d("Wygenerowany formularz", form.toString());

		db.insertForm(form);

		//wysyłanie maila
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pothole form");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Description: " + desc + "\n"
				+ "Localization: " + location.toString() + "\n"
				+ "Contact: " + phoneNumber + "\n"
				+ "Signature: " + signature);

		File root = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		//File file = new File(root, currentPhotoPath);
		File file = new File(currentPhotoPath);
		if (!file.exists() || !file.canRead()) {
			Log.d("Wczytywanie zdjęcia", "brak istniejącego pliku");
			return;
		}
		Uri uri = Uri.fromFile(file);
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

		try {
			startActivity(Intent.createChooser(emailIntent, "sending mail"));
			Toast.makeText(getApplicationContext(), "Form was sent successfully.",
					Toast.LENGTH_LONG).show();
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getApplicationContext(), "There is no mail client installed.",
					Toast.LENGTH_LONG).show();
		}
		resetForm();
		finish();
	}

	public void resetForm() {
		Log.d("debug", "resetForm");
		currentPhotoPath = null;
		descriptEditor.setText("");
		signEditor.setText("");

		photoPreview.setImageResource(R.drawable.camera_icon);
		Spinner recipients = (Spinner) findViewById(R.id.recipient_list);
		recipients.setSelection(0);
		RadioButton btn = (RadioButton) findViewById(R.id.auto_radio_button);
		btn.setChecked(true);
		pictureAdded = false;
	}

	@Override
	public void onConnected(Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("debug", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
		if (pictureAdded) {
			outState.putBoolean(ADDED_KEY, true);
			outState.putString(PATH_KEY, currentPhotoPath);
		} else {
			outState.putBoolean(ADDED_KEY, false);
		}
	}
}
