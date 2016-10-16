package dbug.halmbills;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    NfcAdapter nfcAdapter;

    EditText amount_edit;
    EditText description_edit;
    Button requestRequest;
    Button requestDecline;
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        amount_edit = (EditText) findViewById(R.id.amount_edittext);
        description_edit = (EditText) findViewById(R.id.desc_edittext);
        requestRequest = (Button) findViewById(R.id.request_request);
        requestDecline = (Button) findViewById(R.id.request_decline);
        info = (TextView) findViewById(R.id.info_text);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        requestRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        requestDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nfcAdapter.setOnNdefPushCompleteCallback(new NfcAdapter.OnNdefPushCompleteCallback() {
            @Override
            public void onNdefPushComplete(NfcEvent nfcEvent) {
                Log.e("NFC Complete", "NFC has been delivered");
                RequestActivity.this.finish();
            }
        }, this);
        //nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        return null;
    }

    void sendRequest() {

        NdefRecord mimeRecord = NdefRecord.createMime("application/dbug.halmbills",
                "Test NFC application".getBytes(Charset.defaultCharset()));

        NdefRecord aar = NdefRecord.createApplicationRecord("dbug.halmbills");

        String amountString = "amount:" + amount_edit.getText().toString();

        NdefRecord amount = NdefRecord.createMime("application/dbug.halmbills",
                amountString.getBytes(Charset.defaultCharset()));

        String purposeString = "";
        if (description_edit.getText().length() > 0) {
            purposeString = "purpose:" + description_edit.getText().toString();
        }
        NdefRecord purpose = NdefRecord.createMime("application/dbug.halmbills",
                purposeString.getBytes(Charset.defaultCharset()));

        NdefRecord channelId = NdefRecord.createMime("application/dbug.halmbills",
                "channelid:Hal mNFC".getBytes(Charset.defaultCharset()));

        NdefMessage message = new NdefMessage(new NdefRecord[]{mimeRecord, aar, amount, purpose, channelId});

        nfcAdapter.setNdefPushMessage(message, this);

        requestRequest.setVisibility(View.INVISIBLE);
        requestDecline.setVisibility(View.INVISIBLE);
        info.setVisibility(View.VISIBLE);
    }
}
