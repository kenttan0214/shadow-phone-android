package pomelo.kenttan.shadowphone.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pomelo.kenttan.shadowphone.R;
import pomelo.kenttan.shadowphone.model.SmsModel;

public class SmsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SmsModel> smsItems;

    public SmsListAdapter(Activity activity, List<SmsModel> smsItems) {
        this.activity = activity;
        this.smsItems = smsItems;
    }

    @Override
    public int getCount() {
        return smsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return smsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_sms_list_row, null);
        }

        TextView senderText = (TextView) convertView.findViewById(R.id.sender);
        TextView messageText = (TextView) convertView.findViewById(R.id.message);

        SmsModel sms = smsItems.get(position);

        senderText.setText(sms.getSender());
        messageText.setText(sms.getText());

        return convertView;
    }
}
