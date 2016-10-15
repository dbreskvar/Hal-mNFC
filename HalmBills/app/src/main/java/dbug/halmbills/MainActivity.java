package dbug.halmbills;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import dbug.halmbills.models.TestObject;
import dbug.halmbills.network.APIs;
import dbug.halmbills.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button request = (Button) findViewById(R.id.request_nfc);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RequestActivity.class);
                startActivity(intent);
            }
        });

        Button receive = (Button) findViewById(R.id.receive_nfc);
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
                startActivity(intent);
            }
        });

        Button test = (Button) findViewById(R.id.test_endpoint);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://hack.halcom.com/MBillsWS/API/v1/system/test";
                APIs restclient = RestClient.setupRestClient(APIs.class, url);
                restclient.testEndpoint().enqueue(new Callback<TestObject>() {
                    @Override
                    public void onResponse(Call<TestObject> call, Response<TestObject> response) {
                        Log.e("onResponse", "Response: " + response.message() + " CODE: " + response.code());
                        Log.e("Call URL", "URL: " + response.body() + call.request().url());
                    }

                    @Override
                    public void onFailure(Call<TestObject> call, Throwable t) {
                        Log.e("OnFailure", "Failed: " + t.getLocalizedMessage());
                    }
                });
            }
        });
    }
}
