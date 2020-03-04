package com.example.newentryclear;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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

import static com.example.newentryclear.UtilityClass.timeDisplay;
import static java.lang.String.valueOf;

public class webdb extends AppCompatActivity {

    private static String ip = "192.168.10.131";
    //private static String ip = "10.0.2.2";

    public void reaad_entry( String typeWarning,String idDevice,String tabletName){
        servicioWeb("read",idDevice,tabletName,typeWarning,false);
    }

    public void delete_entry( String typeWarning,String idDevice,String tabletName){
        servicioWeb("delete",idDevice,tabletName,typeWarning,false);
    }

    public void create_entry(String typeWarning,String idDevice,String tabletName){
        servicioWeb("create",idDevice,tabletName,typeWarning,false);
    }

    public void update_entry(String typeWarning,String idDevice,String tabletName){
        servicioWeb("update",idDevice,tabletName,typeWarning,true);
    }

    private void servicioWeb(final String name_serv, String id_device, String name_device, String warning,Boolean attended) {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://" + ip + "/miray/"+name_serv+".php").newBuilder();

            if (name_serv.equals("create")) {
                urlBuilder.addQueryParameter("ID", id_device);
                urlBuilder.addQueryParameter("Name", name_device);
                urlBuilder.addQueryParameter("Warning", warning);
                urlBuilder.addQueryParameter("Attended", valueOf(attended));
                urlBuilder.addQueryParameter("TimeLog", timeDisplay());
            }else if (name_serv.equals("delete")){
                urlBuilder.addQueryParameter("ID", id_device);
            }else if(name_serv.equals("update")){
                urlBuilder.addQueryParameter("ID", id_device);
                urlBuilder.addQueryParameter("Name", name_device);
                urlBuilder.addQueryParameter("Warning", warning);
                urlBuilder.addQueryParameter("Attended", valueOf(attended));
                urlBuilder.addQueryParameter("TimeLog", timeDisplay());
                Log.i("response",timeDisplay());

            }else if(name_serv.equals("read")){
                urlBuilder.addQueryParameter("ID", id_device);
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
                                    Log.i("response","legamos al response");
                                    Log.i("response",response.body().string());
                                }else{
                                    Log.i("response","legamos al response read");
                                    try {
                                        String data = response.body().string();

                                        JSONArray jsonArray = new JSONArray(data);
                                        JSONObject jsonObject;

                                        jsonObject = jsonArray.getJSONObject(0);
                                        String res1,res2,res3,res4;
                                        res1 = jsonObject.getString("id");
                                        res2 = jsonObject.getString("diviceName");
                                        res3 = jsonObject.getString("typeWarning");
                                        res4 = jsonObject.getString("attended");

                                        //Toast.makeText(getBaseContext(), res1+", "+res2+", "+res3, Toast.LENGTH_SHORT).show();
                                        Log.i("response",res1+", "+res2+", "+res3+", "+res4);
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
