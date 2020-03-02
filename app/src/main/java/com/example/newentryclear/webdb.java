package com.example.newentryclear;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newentryclear.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class webdb extends AppCompatActivity {

    //private static String ip;
    private static String ip = "192.168.10.131";

    public void reaad_db(String name_serv, String id_device){

        servicioWeb(name_serv,id_device,"null","null");

    }
    private void servicioWeb(final String name_serv, String id_device, String name_device, String warning) {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://" + ip + "/miray/"+name_serv+".php").newBuilder();

            if (name_serv.equals("create")) {
                urlBuilder.addQueryParameter("PID", id_device);
                urlBuilder.addQueryParameter("PName", name_device);
                urlBuilder.addQueryParameter("Price", warning);
            }else if (name_serv.equals("delete")){
                urlBuilder.addQueryParameter("PID", id_device);
            }else if(name_serv.equals("update")){
                urlBuilder.addQueryParameter("PID", id_device);
                urlBuilder.addQueryParameter("PName", name_device);
                urlBuilder.addQueryParameter("Price", warning);
            }else if(name_serv.equals("read")){
                urlBuilder.addQueryParameter("PID", id_device);
            }

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if(!name_serv.equals("read")){
                                    //txtInfo.setText(response.body().string());
                                    Log.i("response",response.body().string());
                                }else{
                                    try {
                                        String data = response.body().string();

                                        JSONArray jsonArray = new JSONArray(data);
                                        JSONObject jsonObject;

                                        jsonObject = jsonArray.getJSONObject(0);
                                        String res1,res2,res3;
                                        res1 = jsonObject.getString("id");
                                        res2 = jsonObject.getString("diviceName");
                                        res3 = jsonObject.getString("typeWarning");

                                        //Toast.makeText(getBaseContext(), res1+", "+res2+", "+res3, Toast.LENGTH_SHORT).show();
                                        Log.i("response",res1+", "+res2+", "+res3);
                                    } catch (JSONException e) {
                                        Log.e("Json ERROR",e.getMessage());
                                    }
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
