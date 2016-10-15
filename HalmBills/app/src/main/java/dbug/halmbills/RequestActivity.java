package dbug.halmbills;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.nio.charset.Charset;

public class RequestActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        NdefRecord mimeRecord = NdefRecord.createMime("application/dbug.halmbills",
                "Test NFC application".getBytes(Charset.defaultCharset()));

        NdefRecord aar = NdefRecord.createApplicationRecord("dbug.halmbills");

        NdefRecord amount = NdefRecord.createMime("application/dbug.halmbills",
                "amount:1000".getBytes(Charset.defaultCharset()));

        NdefRecord purpose = NdefRecord.createMime("application/dbug.halmbills",
                "purpose:Test Call".getBytes(Charset.defaultCharset()));

        NdefRecord channelId = NdefRecord.createMime("application/dbug.halmbills",
                "channelid:Hal mNFC".getBytes(Charset.defaultCharset()));

        NdefMessage message = new NdefMessage(new NdefRecord[]{mimeRecord, aar, amount, purpose, channelId});

        nfcAdapter.setNdefPushMessage(message, this);

        //nfcAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        return null;
    }
}
