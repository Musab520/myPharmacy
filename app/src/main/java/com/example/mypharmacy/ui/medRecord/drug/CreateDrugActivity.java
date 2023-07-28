package com.example.mypharmacy.ui.medRecord.drug;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mypharmacy.R;
import com.example.mypharmacy.data.local.entities.Drug;
import com.example.mypharmacy.data.local.repositories.DrugRepository;
import com.example.mypharmacy.data.local.repositories.impl.DrugRepositoryImpl;

import java.time.LocalDate;
import java.util.Calendar;

public class CreateDrugActivity extends AppCompatActivity {
    private EditText nameEditText, descriptionEditText, expiryDateEditText;
    private Spinner manufacturerSpinner, categorySpinner, typeSpinner;
    private TextView manufacturerTextView, categoryTextView, typeTextView;
    private Button createButton;
    private LocalDate EXPIRY_DATE;
    private DrugRepository drugRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_drug);
        drugRepository = new DrugRepositoryImpl(this);

        nameEditText = findViewById(R.id.edit_text_name);
        descriptionEditText = findViewById(R.id.edit_text_description);
        manufacturerSpinner = findViewById(R.id.manufacturer_spinner);
        categorySpinner = findViewById(R.id.category_spinner);
        typeSpinner = findViewById(R.id.types_spinner);
        createButton = findViewById(R.id.button_create);
        manufacturerTextView = findViewById(R.id.manufacturer);
        categoryTextView = findViewById(R.id.category);
        typeTextView = findViewById(R.id.type);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        expiryDateEditText.setOnClickListener(e -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            expiryDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            EXPIRY_DATE = LocalDate.of(year, monthOfYear, dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        createButton.setOnClickListener(view -> {
            if (isValidForm()) {
                String name = nameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String manufacturer = manufacturerSpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String type = typeSpinner.getSelectedItem().toString();


                Drug drug = new Drug();
                drug.setName(name);
                drug.setDescription(description);
                drug.setManufacturer(manufacturer);
                drug.setCategory(category);
                drug.setType(type);
                drug.setExpiryDate(EXPIRY_DATE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drugRepository.insertDrug(drug);
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }).start();
            }
        });
    }

    private boolean isValidForm() {
        boolean isValid = true;

        if (nameEditText.getText().toString().isEmpty()) {
            nameEditText.setError("Required");
            isValid = false;
        }

        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("Required");
            isValid = false;
        }

        if (manufacturerSpinner.getSelectedItem().toString().equals("Choose a Manufacturer")) {
            manufacturerTextView.setError("Required");
            isValid = false;
        }

        if (categorySpinner.getSelectedItem().toString().equals("Choose a Category")) {
            categoryTextView.setError("Required");
            isValid = false;
        }

        if (typeSpinner.getSelectedItem().toString().equals("Choose a Type")) {
            typeTextView.setError("Required");
            isValid = false;
        }

        if (EXPIRY_DATE == null) {
            Toast.makeText(this, "Please select an expiry date", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}

