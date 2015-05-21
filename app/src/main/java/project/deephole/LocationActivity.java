package project.deephole;

//TUTAJ MAMY FRAGMENTY Z MAPAMI, UŻYTKOWNIK WYBIERA SWOJĄ POZYCJĘ Z MAPKI

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LocationActivity extends Activity {
    static final int REQUEST_LOCATION = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_layout);
	}

	private void sendLocation(double lng, double lat) {
		Intent returnIntent = new Intent();
        returnIntent.putExtra("lng", lng);;
        returnIntent.putExtra("lat", lat);
		setResult(RESULT_OK, returnIntent);
		finish();
	}
}
