package project.deephole;

//TUTAJ MAMY FRAGMENTY Z MAPAMI, UŻYTKOWNIK WYBIERA SWOJĄ POZYCJĘ Z MAPKI

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends Activity {
    static final int REQUEST_LOCATION = 2;
	static final String LOCATION_KEY = "location";
	static final String COORDINATES_KEY = "coordinates";

	private GoogleMap map;
	private Marker marker;
	private boolean picked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_layout);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {
				if(picked) return;

				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(500);

				marker = map.addMarker(new MarkerOptions()
								.position(latLng)
								.title("Here is a deep hole...")
										//.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
								.draggable(true)
				);
				picked = true;
			}
		});
	}

	public void confirmLocation(View view) {
		if(!picked) {
			Toast.makeText(getApplicationContext(), "Please pick pothole localization.",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent data = new Intent();
		Bundle coordinates = new Bundle();
		coordinates.putParcelable(LOCATION_KEY, marker.getPosition());
		data.putExtra(COORDINATES_KEY, coordinates);

		if (getParent() == null) {
			setResult(Activity.RESULT_OK, data);
		} else {
			getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
	}

	private void sendLocation(double lng, double lat) {
		Intent returnIntent = new Intent();
        returnIntent.putExtra("lng", lng);
        returnIntent.putExtra("lat", lat);
		setResult(RESULT_OK, returnIntent);
		finish();
	}
}
