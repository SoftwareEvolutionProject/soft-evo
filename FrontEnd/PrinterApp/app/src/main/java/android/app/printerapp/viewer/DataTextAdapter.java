package android.app.printerapp.viewer;


import android.app.printerapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataTextAdapter extends BaseAdapter {

    private Context context;
    private String[] titles = null;
    private String[] values = null;

    public DataTextAdapter(Context context) {
        this.context = context;
    }

    public DataTextAdapter(String[] titles, String[] values, Context context){
        this(context);
        this.titles = titles;
        this.values = values;
    }

    public int getCount() {

        if(titles == null) {
            return 0;
        }
        return titles.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View dataTextView;

        TextView title;
        TextView value;

        if (convertView == null) {
            dataTextView = LayoutInflater.from(context).inflate(R.layout.data_text_view, null);
        } else {
            dataTextView = convertView;
        }

        title = (TextView) dataTextView.findViewById(R.id.data_title_textview);
        value = (TextView) dataTextView.findViewById(R.id.data_value_textview);

//      If titles are not null, use given titles
        if(titles != null) {
            title.setText(titles[position]);
        }
//      Same with values
        if(values != null) {
            value.setText(values[position]);
        }

        return dataTextView;
    }
}