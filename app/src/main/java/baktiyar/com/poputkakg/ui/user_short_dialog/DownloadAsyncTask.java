package baktiyar.com.poputkakg.ui.user_short_dialog;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadAsyncTask extends AsyncTask {
    GoogleMap map ;
    public DownloadAsyncTask(@Nullable GoogleMap mMap) {
        map = mMap;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String data ="";
        try{
            data = downloadUrl(objects[0]);

        }
        catch (Exception e){
            Log.d("Background Task :",e.getMessage());
        }
        return data;
    }

    private String downloadUrl(Object urlString) {
        String data = "";
        InputStream iStream  = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL((String)urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        ParserTask parserTask = new ParserTask(map);
        parserTask.execute((String) o);
    }
}
