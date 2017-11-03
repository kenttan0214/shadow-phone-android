package pomelo.kenttan.shadowphone;

import android.app.ProgressDialog;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pomelo.kenttan.shadowphone.adapter.SmsListAdapter;
import pomelo.kenttan.shadowphone.model.SmsModel;
//https://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/
public class SmsListActivity extends AppCompatActivity {
    private ListView listView;

    private SmsListAdapter smsListAdapter;
    private List<SmsModel> smsList = new ArrayList<SmsModel>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        SmsModel data1 = new SmsModel("0123456789", "Hello I am 1st message");
        SmsModel data2 = new SmsModel("0166477789", "Hello I am 2nd message");
        SmsModel data3 = new SmsModel("0193456789", "Hello I am 3rd message");

        smsList.add(data1);
        smsList.add(data2);
        smsList.add(data3);

        listView = (ListView) findViewById(R.id.smsList);
        smsListAdapter = new SmsListAdapter(this, smsList);
        listView.setAdapter(smsListAdapter);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.dismiss();
    }
}
