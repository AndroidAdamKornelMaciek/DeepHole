package project.deephole;

//AKTYWNOŚĆ FORMULARZA, TUTAJ MOŻLIWOŚĆ DODANIA OPISU, ZDJĘCIA, WYBÓR ADRESATA I OKREŚLENIE GEOLOKALIZACJI

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class FormActivity extends Activity {

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
}
