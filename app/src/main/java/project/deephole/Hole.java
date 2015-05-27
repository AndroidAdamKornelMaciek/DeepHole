package project.deephole;

import android.util.Log;

public class Hole {

	private String photoPath;
	private String desc;
	private String location;
    private String readableLocation;

	public Hole(String p, String d, String l) {
		photoPath = p;
		desc = d;
        String tab[] = l.split("::");
		location = tab[1];
        readableLocation = tab[0];
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
		return readableLocation;
	}

	public String parser(String location) {
		Log.d("parser", location);
		final int offset = 4, scope = 10, start = 8;
		int counter = 0;
		char direction;

		StringBuilder readable = new StringBuilder();
		readable.append(location.substring(start, scope));
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

		Log.d("READEABLE_LOC", readable.toString());
		return readable.toString();
	}
}
