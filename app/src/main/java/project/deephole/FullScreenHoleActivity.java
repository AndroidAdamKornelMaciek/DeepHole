package project.deephole;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FullScreenHoleActivity extends Activity {

	private String location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_screen_hole);

		final ImageView photoView = (ImageView) findViewById(R.id.photo_view);
		TextView descView = (TextView) findViewById(R.id.desc_view);
		TextView locView = (TextView) findViewById(R.id.loc_view);
		ImageButton mapBtn = (ImageButton) findViewById(R.id.map_btn);

		Bundle extras = getIntent().getExtras();
		final String photoPath = extras.getString(OverviewActivity.PATH_KEY);
		String desc = extras.getString(OverviewActivity.DESC_KEY);
		String readableLoc = extras.getString(OverviewActivity.READ_LOCAL_KEY);
		location = extras.getString(OverviewActivity.LOCAL_KEY);

		photoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				photoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				setPic(photoPath, photoView);
			}
		});

		descView.setText(desc);
		descView.setMovementMethod(new ScrollingMovementMethod());
		locView.setText(getResources().getString(R.string.localizationPrompt) + "\n" + readableLoc);
		mapBtn.setBackgroundResource(R.drawable.map_icon);
	}

	private void setPic(String path, ImageView view) {
		if(path == null) {
			return;
		}

		int targetW = view.getWidth();
		int targetH = view.getHeight();

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
		view.setImageBitmap(bitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_full_screen_hole, menu);
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

	public void navigateToMap(View view) {
		Intent holeRadar = new Intent(this, HoleRadarActivity.class);
		holeRadar.putExtra(OverviewActivity.LOCAL_KEY, location);
		startActivity(holeRadar);
	}
}
