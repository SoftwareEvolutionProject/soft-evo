package android.app.printerapp.dataviews;

import android.app.Fragment;
import android.app.printerapp.DividerItemDecoration;
import android.app.printerapp.R;
import android.app.printerapp.SearchView;
import android.app.printerapp.api.ApiService;
import android.app.printerapp.api.DatabaseHandler;
import android.app.printerapp.model.BuildList;
import android.app.printerapp.model.DataEntry;
import android.app.printerapp.model.DetailList;
import android.app.printerapp.ui.DataEntryRecyclerViewAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

/**
 * Created by jcmma on 2017-11-23.
 */

public class DetailsFragment extends Fragment implements PropertyChangeListener {
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private Context mContext;
    private View mRootView;
    private RelativeLayout searchHolder;
    private SearchView searchView;

    public DetailsFragment() {
        databaseHandler = DatabaseHandler.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(savedInstanceState == null) {
            mRootView = inflater.inflate(R.layout.fragment_prints, container, false);
            mContext = getActivity();
            recyclerView = (RecyclerView) mRootView.findViewById(R.id.prints_recycler_view);
            searchHolder = (RelativeLayout) mRootView.findViewById(R.id.search_holder);
            searchView = new SearchView(mContext);
            searchView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT));
            searchView.addPropertyChangeListener(this);
            searchHolder.addView(searchView);
        }


        new LoadDataTask().execute();

        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});
        searchView.createSearchOption("Company", new String[]{"Ericsson", "Höganäs", "Chalmers"});

        return mRootView;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals(SearchView.GO_BUTTON_CLICKED)){
            if(event.getNewValue() == null){
                return;
            }

            recyclerView.setAdapter(new DataEntryRecyclerViewAdapter((List<DataEntry>) event.getNewValue()));

        }
    }

    //Async task to retrieve data from database, and set the adapter
    //of the recycler view upon data retrieved
    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        private DetailList result = null;

        @Override
        protected Void doInBackground(Void... vs) {
            ApiService apiService = databaseHandler.getApiService();

            try {
                result =  apiService.fetchAllDetails().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if(result == null){
                return;
            }

            recyclerView.setAdapter(new DataEntryRecyclerViewAdapter<>(result.getDetails()));
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext));
            searchView.updateData(result.getDetails());
        }
    }
}