package project.deephole;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

//TO JEST GŁÓWNA AKTYWNOŚĆ PANOWIE, OTWIERA SIĘ PO URUCHOMIENIU

public class DeepHoleActivity extends Activity {

	private SQLiteDeepHoleHelper db;
	private List<Form> forms = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deep_hole_layout);

		db = new SQLiteDeepHoleHelper(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deep_hole, menu);
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

    /**
     * Metoda prywatna. Wywoływana przez send email. Metoda sendEmail, gdy użytkownik wybierze wybór manualny lokalizacji odpali google maps i pozwoli mu wybrać lokalizację.
     * @param manually true gdy trzeba otworzyć google maps, false otherwise
     * @return współrzędne geograficzne
     * Zrobie to do konca poźniej :) agurgul
     */
    private String getLocalisation(boolean manually){
        if (manually){
            //TODO otwarcie aktywności do wyboru położenia. startactivityforresult?
        }else{
            //TODO pobranie 'last known location'
        }
        return null;
    }
    /**
     * Metoda publiczna do wysyłania maila.
     * TODO ustawianie adresu email wybranego przez użytkownika
     * TODO wybieranie odpowiedniego załącznika
     * TODO tworzenie odpowiedniego tekstu z geolokalizacją
     * autor agurgul (at)author nie można dodać do metody
     * @param view current View
     */
    public void sendEmail(View view){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "adres@mailowy.domena");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Zgłoszenie dziury drogowej");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "String zbudowany z odpowiednich danych. Geolokalizacja, jakieś sratatata");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile="temp/attachement.xml";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }
    public void formulate(View view) {
        Intent intent = new Intent(this, FormActivity.class);
//FOR RESULT, JEŚLI CHCEMY ZAPISYWAĆ FORMULARZ ZGŁOSZENIOWY W BAZIE DANYCH I DODAWAĆ GO DO LISTY WYSŁANYCH FORMULARZY PRZEZ UŻYTKOWNIKA
        startActivity(intent);
    }

//FUNCKJA DO TESTOWANIA OPERACJI INSERT W BAZIE DANYCH
	public void insertTest(View v) {
		Form form = new Form();
		form.setTelephone(123456789);
		form.setDescription("opis dziury");
		form.setPhotoPath("path");
		db.insertForm(form);
	}

//FUNCKJA DO TESTOWANIA POBIERANIA LISTY WSZYSTKICH FORMULARZY ZAWARTYCH W BAZIE DANYCH
	public void getAllTest(View v) {
		forms = db.getAllForms();
	}

//FUNCKJA DO TESTOWANIA OPERACJI DELETE W BAZIE DANYCH
	public void deleteTest(View v) {
		if(!forms.isEmpty())
			db.deleteForm(forms.get(0));
	}
}
