package project.deephole;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class FetchPhotoTask extends AsyncTask<Object, Void, Bitmap> {

	private ImageView photoView;
	private String photoPath;
	private int id;

	public FetchPhotoTask(ImageView view, String path) {
		photoView = view;
		photoPath = path;
		id = view.getId();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Bitmap doInBackground(Object... params) {
		Bitmap bitmap = null;

		//File file = new File(photoPath);
		//if(file.exists())
		//	bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bitmap = BitmapFactory.decodeFile(photoPath, options);

		//Log.d("doInBackground", photoPath);
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (photoView.getId() != id)
			return;

		if(result != null && photoView != null){
			photoView.setVisibility(View.VISIBLE);
			photoView.setImageBitmap(result);
		} else {
			photoView.setVisibility(View.GONE);
		}
	}

}
