package baktiyar.com.poputkakg.ui.user_short_dialog;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import baktiyar.com.poputkakg.util.DIrectionsJsonParser;

class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    GoogleMap googleMap ;
    public ParserTask(GoogleMap map) {
        googleMap = map;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

        JSONObject jsonObject;
        List<List<HashMap<String, String>>> routes = null;
        try{
            jsonObject = new JSONObject(strings[0]);
            DIrectionsJsonParser parser = new DIrectionsJsonParser();

            routes = parser.parse(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        super.onPostExecute(result);
        ArrayList points  = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        for (int i = 0; i <result.size() ; i++) {
            points = new ArrayList();
            lineOptions =  new PolylineOptions();
            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j <path.size() ; j++) {
                HashMap point = path.get(j);

                double lat = Double.parseDouble((String) point.get("lat"));
                double lng = Double.parseDouble((String)point.get("lng"));
                LatLng position = new LatLng(lat,lng);
                points.add(position);
            }
            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.BLUE);
            lineOptions.geodesic(true);
            
        }
        //here add lines to map
        googleMap.addPolyline(lineOptions);

    }
}
