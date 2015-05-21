package project.deephole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class OverviewArrayAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> values;

	public OverviewArrayAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.overview_line_layout, R.id.location, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.overview_line_layout, parent, false);

		TextView dscView = (TextView) rowView.findViewById(R.id.description);
		ImageView photoView = (ImageView) rowView.findViewById(R.id.photo);
		photoView.setImageResource(R.drawable.flag);

		return rowView;
	}
}
