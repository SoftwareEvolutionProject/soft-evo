package android.app.printerapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.printerapp.api.DatabaseHandler;
import android.app.printerapp.model.Company;
import android.app.printerapp.model.CompanyList;
import android.app.printerapp.model.Detail;
import android.app.printerapp.ui.DetailSaveListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AddDetailsActivity extends Activity implements Callback<CompanyList> {

    private List<Company> companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        fillCompanyList();
        setup();
    }

    private void setup() {
        setupAddNewCompanyListener();
        setupSaveButtonListener();
        setupCancelButton();
    }

    private void setupCancelButton() {
        final Activity currentActivity = this;
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        if(cancelButton!= null)
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentActivity.finish();
                }
            });
    }

    private void setupAddNewCompanyListener() {
        View button = findViewById(R.id.addNewCompanyButton);
        View.OnClickListener listener = getAddNewCompanyListner(this);
        button.setOnClickListener(listener);
    }

    private void setupSaveButtonListener() {
        View button = findViewById(R.id.saveButton);
        View.OnClickListener listener = getAddDetailsListner(this);
        button.setOnClickListener(listener);
    }

    private View.OnClickListener getAddDetailsListner(AddDetailsActivity addDetailsActivity) {
        View.OnClickListener onClickListener = new DetailSaveListener(addDetailsActivity);
        return onClickListener;
    }

    private View.OnClickListener getAddNewCompanyListner(Activity activity) {
        return new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewCompany.class);
                startActivity(intent);
            }
        };
    }

    private void fillCompanyList() {
        companies = new LinkedList<>();
        DatabaseHandler.getInstance().getApiService().fetchAllCompanies().enqueue(this);
    }

    @Override
    public void onResponse(Call<CompanyList> call, Response<CompanyList> response) {
        if (response.isSuccessful()) {
            companies.addAll(response.body().getCompaniesApi());
            Spinner spinner = (Spinner) findViewById(R.id.companyListInput);
            SpinnerAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, companies);
            spinner.setAdapter(adapter);

        }

        android.util.Log.e(TAG, "onResponse: " + response.toString());
    }

    @Override
    public void onFailure(Call<CompanyList> call, Throwable t) {
        companies.add(new Company("No Company Found", 0));
    }

}
