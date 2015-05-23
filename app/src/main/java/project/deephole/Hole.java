package project.deephole;

import android.util.Log;

public class Hole {

	private String photoPath;
	private String desc;
	private String location;

	public Hole(String p, String d, String l) {
		photoPath = p;
		desc = d;
		location = l;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public String getDesc() {
		return desc;
	}

	public String getLocation(){
		return location;
	}

	public String getReadableLocation() {
		return parser(location);
	}

	public String parser(String location) {
		Log.d("parser", location);
		final int offset = 4, scope = 10;
		int counter = 0;
		char direction;

		StringBuilder readable = new StringBuilder();
		readable.append(location.substring(0, scope));
		if(location.charAt(scope) == '-') {
			counter++;
			direction = 'S';
		}
		else
			direction = 'N';

		readable.append(location.substring(scope + counter, scope + counter + offset)
				+ " " + direction);

		counter = 0;
		int coma = location.indexOf(",");
		if(location.charAt(coma + 1) == '-') {
			counter++;
			direction = 'W';
		}
		else
			direction = 'E';

		counter += 1;
		readable.append(", " + location.substring(coma + counter, coma + counter + offset)
				+ " " + direction + ")");

		return readable.toString();
	}
}
