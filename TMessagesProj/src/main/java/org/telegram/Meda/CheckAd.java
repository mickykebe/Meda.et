package org.telegram.Meda;

/**
 * Created by Micky on 12/31/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.ui.MedaAdActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Micky on 12/7/2015.
 */
public class CheckAd extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = CheckUpdate.class.getSimpleName();
    private Context mContext;

    public CheckAd(Context context){
        mContext = context;
    }

    private String getAdUrl(String adJsonStr) throws JSONException {
        String adUrl;
        boolean showAd = false;

        final String MEDA_KEY_AD_SHOW = "show";
        final String MEDA_KEY_AD_URL = "url";

        JSONObject adJson = new JSONObject(adJsonStr);
        showAd = adJson.getBoolean(MEDA_KEY_AD_SHOW);
        adUrl = adJson.getString(MEDA_KEY_AD_URL);

        if(showAd && adUrl != "")
            return adUrl;

        return null;
    }

    @Override
    protected String doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String adJsonStr;

        try {
            final String baseUrl = "http://meda.360ground.com/a";

            Uri uri = Uri.parse(baseUrl).buildUpon().build();

            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if(inputStream == null)
                return null;

            StringBuffer buffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0)
                return null;

            adJsonStr = buffer.toString();
            return getAdUrl(adJsonStr);
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

        return null;
    }

    @Override
    protected void onPostExecute(String adUrl) {
        super.onPostExecute(adUrl);
        if(adUrl != null) {
            Intent intent = new Intent(mContext, MedaAdActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, adUrl);
            mContext.startActivity(intent);
        }
    }
}
