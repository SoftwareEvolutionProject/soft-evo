package android.app.printerapp.search;

import android.app.printerapp.R;
import android.app.printerapp.api.ApiService;
import android.app.printerapp.api.DatabaseHandler;
import android.app.printerapp.model.DataEntry;
import android.app.printerapp.model.Date;
import android.app.printerapp.model.Detail;
import android.app.printerapp.model.NoDataSelected;
import android.app.printerapp.model.Operator;
import android.app.printerapp.model.Print;
import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SAMSUNG on 2017-11-23.
 * In order to make the outer view (most likely a fragment such as PrintsFragment) update itself
 * after search, it must listen to this, by implementing PropertyChangeListener.
 * Everytime the new view updates its data, it must also call the method updateData() on this
 * class. This updates the AutoCompleteTextView.
 */

public class SearchView extends ConstraintLayout {

//---------------------------------------------------------------------------------------
//          VARIABLES
//---------------------------------------------------------------------------------------

    //Class variables
    private Context mContext;
    private List<? extends DataEntry> data;
    private Map<String, List<? extends DataEntry>> optionLists = new HashMap<>();
    private Map<String, DataEntry> selectedOptionsMap = new HashMap<>();

    //Views
    private LinearLayout leftSearchOptionsLayout;
    private LinearLayout rightSearchOptionsLayout;
    private AutoCompleteTextView searchBar;
    private Button goButton;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private EditText dateInput;

    //Constants
    public static final String GO_BUTTON_CLICKED = "ONCLICK";
    public static final String DATE_OPTION = "date_option";

    //Counter
    private int optionCounter = 0;

//---------------------------------------------------------------------------------------
//          OVERRIDES
//---------------------------------------------------------------------------------------

    public SearchView(Context context) {
        super(context);
        initializeView();
    }

    public SearchView(Context context, AttributeSet attrs){
        super(context, attrs);
        initializeView();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initializeView();
    }

//---------------------------------------------------------------------------------------
//          SETUP METHODS
//---------------------------------------------------------------------------------------

    private void initializeView(){
        //Basic setup
        mContext = getContext();
        inflate(mContext, R.layout.search_layout, this);

        //Retrieve references to GUI elements
        leftSearchOptionsLayout = (LinearLayout) findViewById(R.id.left_search_options);
        rightSearchOptionsLayout = (LinearLayout) findViewById(R.id.right_search_options);
        searchBar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        goButton = (Button) findViewById(R.id.search_go_button);

        //Setup the onclick listener for the Go button
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date dateOption = new Date();
                dateOption.setDate(dateInput.getText().toString());
                selectedOptionsMap.put(DATE_OPTION, dateOption);

                pcs.firePropertyChange(GO_BUTTON_CLICKED, null, selectedOptionsMap);

            }
        });

        createSearchOptionTextInput("Creation date", "YYYY-MM");
    }

    public void createSearchOptionSelection(final String tag, String title, List<? extends DataEntry> givenOptions){
        if(givenOptions == null){
            return;
        }

        List<DataEntry> options = new ArrayList<>();
        options.add(0, new NoDataSelected());
        options.addAll(givenOptions);

        optionLists.put(title, options);

        //Inflate the search_option_selection_layout
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout searchOptionLayout = (RelativeLayout) inflater.inflate(R.layout.search_option_selection_layout, null);

        //Retrieve references to views
        TextView titleTextView = (TextView) searchOptionLayout.findViewById(R.id.search_option_text);
        titleTextView.setText(title);
        Spinner optionSpinner = (Spinner) searchOptionLayout.findViewById(R.id.search_option_spinner);

        //Set the adapter of the spinner
        SearchOptionArrayAdapter<? extends DataEntry> adapter = new SearchOptionArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, options);
        optionSpinner.setAdapter(adapter);
        optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedOptionsMap.put(tag, (DataEntry) adapterView.getAdapter().getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedOptionsMap.put(tag, null);
            }
        });

        addOptionToLayout(searchOptionLayout);

    }

    public void createSearchOptionTextInput(String title, String hint){
        //Inflate the search_option_selection_layout
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout searchOptionLayout = (RelativeLayout) inflater.inflate(R.layout.search_option_text_input_layout, null);

        //Retrieve references to views
        TextView titleTextView = (TextView) searchOptionLayout.findViewById(R.id.search_option_text);
        titleTextView.setText(title);
        EditText textInput= (EditText) searchOptionLayout.findViewById(R.id.search_option_text_input);
        textInput.setHint(hint);

        addOptionToLayout(searchOptionLayout);

        if(title.equals("Creation date")){
            dateInput = textInput;
        }
    }

    private void addOptionToLayout(RelativeLayout searchOptionLayout){
        //Places uneven views in the left layout and even views on the right
        if(optionCounter % 2 == 0){
            leftSearchOptionsLayout.addView(searchOptionLayout);
        }else{
            rightSearchOptionsLayout.addView(searchOptionLayout);
        }
        optionCounter++;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener){
        pcs.removePropertyChangeListener(listener);
    }


//---------------------------------------------------------------------------------------
//          METHODS TO CONTROL THE CLASS
//---------------------------------------------------------------------------------------

    public void updateData(List<? extends DataEntry> data){

        List<String> dataIds = new ArrayList<>();

        //Save all print ids in a list. Add prefix to define that it's a print
        for(DataEntry current : data){
            dataIds.add(current.getIdName());
        }

        //Update teh adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_dropdown_item_1line, dataIds);
        searchBar.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Update our data
        this.data = data;
    }

    public String getSearchText(){
        return searchBar.getText().toString();
    }
}
