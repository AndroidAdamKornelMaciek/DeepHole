package project.deephole;

//AKTYWNOŚĆ FORMULARZA, TUTAJ MOŻLIWOŚĆ DODANIA OPISU, ZDJĘCIA, WYBÓR ADRESATA I OKREŚLENIE GEOLOKALIZACJI

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

	static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_LOCATION = 2;
	private String currentPhotoPath;
	private ImageView photoPreview;
	private EditText descriptEditor, signEditor;
	private Spinner recipientList;
	private boolean manual;
	private GoogleApiClient mGoogleApiClient; //do gejolokalizacji
	private Location mLastKnownLocation;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_layout);

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

        }
	}

	public void takePhoto(View view) {
		dispatchPhotoIntent();
	}

	private void dispatchPhotoIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (intent.resolveActivity(getPackageManager()) != null) {

			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) { Log.d("dispatchPhotoIntent", "Problem z utworzeniem pliku!"); }

			if (photoFile != null) {
				Uri currentPhotoUri = Uri.fromFile(photoFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						currentPhotoUri);
				startActivityForResult(intent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,
				".jpg",
				storageDir);

		currentPhotoPath = image.getAbsolutePath();//"file:" + image.getAbsolutePath();
		return image;
	}

	/*
	Metoda, która przeskalowuje zdjęcie w celu oszczędzania pamięci (płynności aplikacji)
	Z czasem zostanie przeniesiona w miejsce gdzie użytkownik będzie przeglądał listę zgłoszonych dziur
	 */
	private void setPic() {
		int targetW = photoPreview.getWidth();
		int targetH = photoPreview.getHeight();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
		photoPreview.setImageBitmap(bitmap);
	}

	/*Metoda zbiera dane z widoków, następnie tworzy z danych obiekt klasy Form, który zapisuje
	* do bazy danych, i wysyła maila z załącznikami
	* uwaga: jakie pola są wymagane, czy oznaczamy je np. gwiazdką?
	* Po ustaleniu dodamy warunki, które blokują wysłanie "pustego" zgłoszenia*/
	public void sendMail(View view) {
		String path = currentPhotoPath;
		String desc = descriptEditor.getText().toString();
		String recipient = recipientList.getSelectedItem().toString();
		String signature = signEditor.getText().toString();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();

		if(manual) {//uruchomienie map
			Intent intent = new Intent(this, LocationActivity.class);
			startActivityForResult(intent, REQUEST_LOCATION);
		} else { //pobranie lokalizacji
			if (mGoogleApiClient == null) {
				mGoogleApiClient = new GoogleApiClient.Builder(this)
						.addConnectionCallbacks(this)
						.addOnConnectionFailedListener(this)
						.addApi(LocationServices.API).build();
			}
			mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

			if (mLastKnownLocation != null) {
				//mamy lokalizacje
			} else {
				//nie mamy
			}

		}

		//tu mamy lokalizację z powyższego "if elsa"
		String localization = "no zgadnij cwaniaczku gdzie teraz jestem";

		//uwaga: id ustawiam na zero, bo jest kluczem autoinkrementującym się, czyli bez zmartwień
		Form form = new Form(0, path, desc, recipient, localization, signature, phoneNumber);
		//zapis do DB + wysyłanie maila
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
}
