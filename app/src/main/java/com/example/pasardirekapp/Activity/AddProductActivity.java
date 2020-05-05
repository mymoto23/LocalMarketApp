package com.example.pasardirekapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.example.pasardirekapp.Classes.Product;
import com.example.pasardirekapp.FirebaseDatabaseHelper;
import com.example.pasardirekapp.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddProductActivity extends AppCompatActivity implements OnItemSelectedListener{

    FirebaseDatabaseHelper dbHelper;
    EditText etName, etDescription, etBuyPrice, etSellPrice, etQuantity;
    TextView tvDate;
    Button btnAddProduct;
    Spinner spinner;

    String selectedCategory;
    long selectedDate;

//    OnItemSelectedListener itemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etName = findViewById(R.id.tvName);
        etBuyPrice = findViewById(R.id.tvPrice);
        etSellPrice = findViewById(R.id.tvSellPrice);
        tvDate = findViewById(R.id.tvDate);
        etDescription = findViewById(R.id.tvDescription);
        etQuantity = findViewById(R.id.tvQuantity);
        spinner = findViewById(R.id.spnCategory);

        btnAddProduct = findViewById(R.id.btnAddProduct);

        dbHelper = new FirebaseDatabaseHelper();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddProductActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tvDate.setText("" + (month + 1) + "/" + day + "/" + year);
                                Calendar newDate = new GregorianCalendar(year, month, day);
                                selectedDate = newDate.getTimeInMillis();
                            }
                        },
                        year, month, day
                );
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Product p = new Product(
                            etName.getText().toString(),
                            selectedDate,
                            etDescription.getText().toString(),
                            Integer.parseInt(etBuyPrice.getText().toString()),
                            Integer.parseInt(etSellPrice.getText().toString()),
                            Integer.parseInt(etQuantity.getText().toString()),
                            selectedCategory);
                    dbHelper.retrieveReference("Products").push().setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddProductActivity.this, "Product successfully added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                else {
                    Toast.makeText(AddProductActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkEmpty(Editable e) {
        return !e.toString().isEmpty();
    }

    public boolean validate() {
        return checkEmpty(etName.getText())
                && checkEmpty(etBuyPrice.getText())
                && checkEmpty(etSellPrice.getText())
                && checkEmpty(etDescription.getText())
                && checkEmpty(etQuantity.getText())
                && !selectedCategory.isEmpty() && selectedDate != 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        selectedCategory = adapterView.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
