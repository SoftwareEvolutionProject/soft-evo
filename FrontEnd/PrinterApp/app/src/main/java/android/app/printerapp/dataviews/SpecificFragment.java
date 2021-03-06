package android.app.printerapp.dataviews;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.printerapp.DividerItemDecoration;
import android.app.printerapp.ListContent;
import android.app.printerapp.Log;
import android.app.printerapp.R;
import android.app.printerapp.SpecificActivity;
import android.app.printerapp.api.DatabaseHandler;
import android.app.printerapp.model.DataEntry;
import android.app.printerapp.ui.AnimationHelper;
import android.app.printerapp.ui.DataEntryRecyclerViewAdapter;
import android.app.printerapp.viewer.DataTextAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jcmma on 2017-11-21.
 */

abstract class SpecificFragment extends Fragment {
    //Variables for this view
    protected View mRootView;
    protected Context mContext;
    protected Bundle arguments;

    //Alert dialog builder
    AlertDialog.Builder alertDialogBuilder;

    //Api
    protected DatabaseHandler databaseHandler;

    //Views
    private TabHost traceTabHost;
    protected ListView dataListView;
    protected Map<String, RecyclerView> allTraceLists;

    //Constants
//---------------------------------------------------------------------------------------
//          OVERRIDES
//---------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Retain instance to keep the Fragment from destroying itself
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            //Get the rootview from its parent
            mRootView = inflater.inflate(getLayoutResourceId(), container, false);
            mContext = getActivity();
            databaseHandler = DatabaseHandler.getInstance();

            //Retrieve references to views
            dataListView = (ListView) mRootView.findViewById(R.id.data_list_view);
            dataListView.setAdapter(new DataTextAdapter(mContext));
            traceTabHost = (TabHost) mRootView.findViewById(R.id.trace_tab_host);

            //Retrieve all given arguments
            arguments = getArguments();
        }

        //Create an alert dialog which we will use for when data cannot be loaded from the server
        createAlertDialog();

        //Initialize the Tab Host for tracing
        initializeTraceTabHost();

        return mRootView;
    }

//---------------------------------------------------------------------------------------
//          ABSTRACT METHODS
//---------------------------------------------------------------------------------------

    public abstract int getLayoutResourceId();

    public abstract void loadData();

    public abstract void createTabs();

//---------------------------------------------------------------------------------------
//          SETUP METHODS
//---------------------------------------------------------------------------------------

    public static SpecificFragment newInstance(int id){
        Bundle b = new Bundle();
        b.putInt(SpecificActivity.ID, id);
        PrintsSpecificFragment psf = new PrintsSpecificFragment();
        psf.setArguments(b);
        return psf;
    }

    //Builds an alert dialog to be shown used when data cannot be retrieved
    private void createAlertDialog(){
        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage("Retrieving data from the database failed. Would you like to try again?");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadData();
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
    }

    //Method for initializing the tab host
    private void initializeTraceTabHost(){
        traceTabHost.setup();
        allTraceLists = new HashMap<>();
        createTabs();
        traceTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                View currentView = traceTabHost.getCurrentView();
                AnimationHelper.inFromRightAnimation(currentView);
                onTagSelected(traceTabHost.getCurrentTabTag());
            }
        });

    }

    abstract void onTagSelected(Object tag);


    protected void createTab(String tag, String title){
        TabHost.TabSpec spec = traceTabHost.newTabSpec(tag);
        spec.setIndicator(getTabIndicator(title));
        spec.setContent(new TraceTabFactory());
        traceTabHost.addTab(spec);
    }

    //Creation of tab indicator, used to customize tabs for the tabhost
    private View getTabIndicator(String title) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trace_tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.trace_tab_title_textview);
        tv.setText(title);
        return view;
    }

    private class TraceTabFactory implements TabHost.TabContentFactory {

        public View createTabContent(String tag) {

            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            RecyclerView recyclerView = new RecyclerView(mContext);

            recyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT));

            //Save the recyclerview in our map so we can access it
            allTraceLists.put(tag, recyclerView);
            linearLayout.addView(recyclerView);

            return linearLayout;
        }
    }

    protected void createAlertDialog(String message){
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Log.d("SpecificFragment", message);
    }

    //Helper methods
    protected boolean dataIsOk(List<?> list){
        return (list != null && !list.isEmpty());
    }
}
