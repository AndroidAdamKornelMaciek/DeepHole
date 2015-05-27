package project.deephole;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Kornel on 2015-05-27.
 */
public class AsyncRouteGetter extends AsyncTask<Double, String, String> {
	protected String doInBackground(Double... coords) {
		String xml = "";
		String url = "http://maps.google.com/maps/api/geocode/xml?address=" + coords[0] + "," + coords[1] + "&sensor=false";
		//String url = "http://maps.google.com/maps/api/geocode/json?address=51.0031761,17.0499418&sensor=false";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity);
		} catch (Exception e) { e.printStackTrace(); }
		//Log.d("LOK", xml);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            doc = builder.parse(is);
        } catch (Exception e) { Log.d("DOC", "BLAD2"); e.printStackTrace(); return null; }
        String result = doc.getElementsByTagName("formatted_address").item(0).getTextContent();
        Log.d("LOKALIZACJA", result);

        return result;
	}
}
