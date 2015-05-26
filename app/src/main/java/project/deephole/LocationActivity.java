package project.deephole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends Activity {
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
								.title(getResources().getString(R.string.deepHoleWarning))
								.draggable(true)
				);
				picked = true;
			}
		});

		TypedValue outValue = new TypedValue();
		getResources().getValue(R.dimen.Wroclaw_lat, outValue, true);
		float lat = outValue.getFloat();
		getResources().getValue(R.dimen.Wroclaw_lng, outValue, true);
		float lng = outValue.getFloat();

		LatLng coordinates = new LatLng(lat, lng);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
	}

	public void confirmLocation(View view) {
		if(!picked) {
			Toast.makeText(getApplicationContext(), "Okre?l lokalizacj? dziury",
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
}
