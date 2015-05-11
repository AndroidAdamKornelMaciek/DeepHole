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

//TO JEST GŁÓWNA AKTYWNOŚĆ PANOWIE, OTWIERA SIĘ PO URUCHOMIENIU

public class DeepHoleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deep_hole_layout);
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
}
