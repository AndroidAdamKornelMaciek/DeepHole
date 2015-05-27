package project.deephole;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HoleRadarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hole_radar);

		Bundle extras = getIntent().getExtras();
		String location = extras.getString(OverviewActivity.LOCAL_KEY);
		int coma = location.indexOf(",");
		Double lat, lng;
		lat = Double.parseDouble(location.substring(10, coma));
		lng = Double.parseDouble(location.substring(coma + 1, location.length() - 2));
		LatLng coordinates = new LatLng(lat, lng);
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.addMarker(new MarkerOptions()
						.position(coordinates)
						.title("Tu jest dziura.")
		);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
}
