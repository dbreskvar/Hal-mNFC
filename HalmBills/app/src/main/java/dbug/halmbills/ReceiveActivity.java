package dbug.halmbills;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import dbug.halmbills.models.Sale;
import dbug.halmbills.models.SaleModel;
import dbug.halmbills.network.APIs;
import dbug.halmbills.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

        }
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    String amount;
    String currency;
    String purpose;
    String paymentReference;
    String orderId;
    String channelId;

    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String string = new String(msg.getRecords()[0].getPayload());

        for (int i = 0; i < msg.getRecords().length; i++) {
            try {
                String payload = new String(msg.getRecords()[i].getPayload());
                String name = payload.split(":")[0];
                String info = payload.split(":")[1];
                if (name != null) {
                    switch (name) {
                        case "amount":
                            amount = info;
                            break;
                        case "currency":
                            currency = info;
                            break;
                        case "purpose":
                            purpose = info;
                            break;
                        case "paymentreference":
                            paymentReference = info;
                            break;
                        case "orderid":
                            orderId = info;
                            break;
                        case "channelid":
                            channelId = info;
                            break;
                    }
                }
            } catch (IndexOutOfBoundsException e) {

            }
        }
        APIs restclient = RestClient.setupRestClient(APIs.class, "https://hack.halcom.com/MBillsWS/API/v1/transaction/sale");
        Sale sale = new Sale();
        sale.setAmount(1000);
        sale.setCurrency("EUR");
        sale.setPurpose("Online payment");
        sale.setPaymentreference("SI0011072015");
        sale.setOrderid("3515435135");
        sale.setChannelid("mNFCpay");

        restclient.createSale(sale).enqueue(new Callback<SaleModel>() {
            @Override
            public void onResponse(Call<SaleModel> call, Response<SaleModel> response) {
                Log.e("onResponse", "Response: " + response.message() + " CODE: " + response.headers());
                Log.e("Call URL", "URL: " + response.body() + call.request().url());
            }

            @Override
            public void onFailure(Call<SaleModel> call, Throwable t) {
                Log.e("OnFailure", "Failed: " + t.getLocalizedMessage());
            }
        });
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        Log.e("Got sth", "NFC: " + string);
    }
}
