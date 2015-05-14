package project.deephole;

//AKTYWNOŚĆ FORMULARZA, TUTAJ MOŻLIWOŚĆ DODANIA OPISU, ZDJĘCIA, WYBÓR ADRESATA I OKREŚLENIE GEOLOKALIZACJI

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormActivity extends Activity {

	static final int REQUEST_TAKE_PHOTO = 1;
	/*ściezka do ostanio wykonanego zdjęcia, ustawiana będzie w instanji formularza
	w polu photoPath przed zapisaniem do bazy danych*/
	private String currentPhotoPath;
	/*pytanie co wygodniejsze?*/
	private Uri currentPhotoUri;
    private ImageView photoPreview;
    private Spinner recipientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);

        photoPreview = (ImageView) findViewById(R.id.hole_photo);
        photoPreview.setImageResource(R.drawable.camera_icon);

        recipientList = (Spinner) findViewById(R.id.recipient_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.recipient_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipientList.setAdapter(adapter);
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
				currentPhotoUri = Uri.fromFile(photoFile);
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
				storageDir
		);

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
}
