package project.deephole;

//TUTAJ MAMY FRAGMENTY Z MAPAMI, UŻYTKOWNIK WYBIERA SWOJĄ POZYCJĘ Z MAPKI

import android.app.Activity;
import android.os.Bundle;

public class LocationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.location_layout);
    }
}
