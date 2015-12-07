package org.telegram.Meda;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.UpdateWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Micky on 12/7/2015.
 */
public class CheckUpdate extends AsyncTask<Void, Void, Boolean> {
    private static final String LOG_TAG = CheckUpdate.class.getSimpleName();
    private Context mContext;

    public CheckUpdate(Context context){
        mContext = context;
    }

    private boolean updateExists(String updateJsonStr) throws JSONException{
        String latestVersion;
        String installedVersion = Utility.getInstalledAppVersion();

        final String MEDA_LATEST_VERSION = "version";

        JSONObject updateJson = new JSONObject(updateJsonStr);
        latestVersion = updateJson.getString(MEDA_LATEST_VERSION);

        if(!installedVersion.equals(latestVersion))
            return true;

        return false;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String updateJsonStr;

        try {
            final String baseUrl = "http://meda.360ground.com/";

            Uri uri = Uri.parse(baseUrl);

            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if(inputStream == null)
                return false;

            StringBuffer buffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0)
                return false;

            updateJsonStr = buffer.toString();
            return updateExists(updateJsonStr);
        }
        catch(IOException e){
            Log.e(LOG_TAG, "Error retrieving update info", e);
        }
        catch (JSONException e){
            Log.e(LOG_TAG, "Unable to parse update JSON", e);
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }
                catch (final IOException e){
                    Log.e(LOG_TAG, "Error Closing Stream", e);
                }
            }
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean newUpdate) {
        super.onPostExecute(newUpdate);
        if(newUpdate) {
            Intent intent = new Intent(mContext, UpdateWindow.class);
            mContext.startActivity(intent);
        }
    }
}
