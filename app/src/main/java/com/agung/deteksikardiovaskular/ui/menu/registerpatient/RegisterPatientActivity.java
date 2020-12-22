package com.agung.deteksikardiovaskular.ui.menu.registerpatient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelPasien;
import com.agung.deteksikardiovaskular.ui.datapicker.DatePickerFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterPatientActivity extends AppCompatActivity implements DatePickerFragment.DialogDateListener {
    final String DATE_PICKER_TAG = "DatePicker";

    private BottomAppBar btnRegister;
    private EditText inputName;
    private TextView inputDateBirth;
    private EditText inputAddress;
    private EditText inputNoKtp;

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;

    private int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Pasien");

        inputName = findViewById(R.id.input_name);
        inputDateBirth = findViewById(R.id.input_date_birth);
        inputAddress = findViewById(R.id.input_address);
        inputNoKtp = findViewById(R.id.input_ktp);

        inputDateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                return;
            }
        });

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String dateBirth = inputDateBirth.getText().toString();
                String address = inputAddress.getText().toString().trim();
                String noKtp = inputNoKtp.getText().toString().trim();

                boolean isEmptyFields = false;
                if(TextUtils.isEmpty(name)) {
                    isEmptyFields = true;
                    inputName.setError("Field ini tidak boleh kosong");
                }
                if(TextUtils.isEmpty(dateBirth)) {
                    isEmptyFields = true;
                    inputDateBirth.setError("Field ini tidak boleh kosong");
                }
                if(TextUtils.isEmpty(address)) {
                    isEmptyFields = true;
                    inputAddress.setError("Field ini tidak boleh kosong");
                }
                if(TextUtils.isEmpty(noKtp)) {
                    isEmptyFields = true;
                    inputNoKtp.setError("Field ini tidak boleh kosong");
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(!TextUtils.isEmpty(noKtp) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(dateBirth) && !TextUtils.isEmpty(name)) {
                    FirebaseApp.initializeApp(getApplication());
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                    mDatabaseReference.child("users").child(user.getUid()).child("pasien").push().setValue(new ModelPasien(name, dateBirth, address, noKtp, gender)).addOnSuccessListener(RegisterPatientActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterPatientActivity.this, "Data pasien berhasil di simpan !", Toast.LENGTH_LONG).show();
                            inputName.setText(null);
                            inputDateBirth.setText(null);
                            inputAddress.setText(null);
                            inputNoKtp.setText(null);
                        }
                    });
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onDialogDateSet(String tag, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        inputDateBirth.setText(dateFormat.format(calendar.getTime()));
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.rb_f:
                if(checked)
                    gender = 0;
                break;

            case R.id.rb_m:
                if(checked)
                    gender = 1;
                break;

            default:
                break;
        }
    }
}