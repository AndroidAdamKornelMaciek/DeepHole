package project.deephole;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class HoleRadarActivity extends ActionBarActivity {

	private GoogleMap map;

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
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.addMarker(new MarkerOptions()
						.position(coordinates)
						.title("Here is a deep hole...")
		);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_hole_radar, menu);
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
}
