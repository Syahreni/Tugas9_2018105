package com.example.pertemuan4_actionbar;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.pertemuan4_actionbar.databinding.ActivityRestApiBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class RestAPI extends AppCompatActivity implements
        View.OnClickListener{
    //declaration variable
    private ActivityRestApiBinding binding;
    String index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityRestApiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
    }
    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://dekontaminasi.com/api/id/covid19/hospitals")
                .buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String>{
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get data json
        public void parseJson(String data) throws JSONException{
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(data);
            }catch (JSONException e){
                e.printStackTrace();
            }

            for (int i =0; i <jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String Sobj = obj.get("name").toString();
                if (Sobj.equals(index.trim())){
                    String address = obj.get("address").toString();
                    binding.resultAddress .setText(address);
                    String region = obj.get("region").toString();
                    binding.resultRegion.setText(region);
                    String name = obj.get("name").toString();
                    binding.resultName.setText(name);
                    break;

                }
                else{
//                    binding.resultName.setText("Not Found");
                    Log.d("TAG", "parseJson: not found");
                }
            }
        }
    }
}