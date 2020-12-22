package com.agung.deteksikardiovaskular.ui.menu.listpatient.listCekpasien;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelCekPasien;
import com.agung.deteksikardiovaskular.ui.datapicker.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailCekPatientActivity extends AppCompatActivity {
    public static final String EXTRA_CEK_PASIEN = "extra_cek_pasien";
    private TextView inputDateIssue, tvResult, tglPeriksa;
    private EditText inputChestPT;
    private EditText inputRestingBP;
    private EditText inputFastingBS;
    private EditText inputCholesterol;
    private EditText inputRestingEM;
    private EditText inputMaxHeartRate;
        private EditText inputExerciseIA;
    private EditText inputStDepression;
    private EditText inputStSlope;
    private EditText inputMajorVessels;
    private EditText inputThalassemia;
    String getGenderFromPasien;
    String getUmurFromPasien;

    ModelCekPasien cekPasien;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cek_patient);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Rekap cek pasien");

        tvResult = findViewById(R.id.tv_result);
        tglPeriksa = findViewById(R.id.tv_tanggal_pemeriksaan);

        inputDateIssue = findViewById(R.id.input_cek_date);
        inputChestPT = findViewById(R.id.input_chestPT);
        inputRestingBP = findViewById(R.id.input_restingBP);
        inputFastingBS = findViewById(R.id.input_fastingBS);
        inputCholesterol = findViewById(R.id.input_cholesterol);
        inputRestingEM = findViewById(R.id.input_resting_electrocardiographic);
        inputMaxHeartRate = findViewById(R.id.input_maximum_heart_rate);
        inputExerciseIA = findViewById(R.id.input_exercise_induced_angina1);
        inputStDepression = findViewById(R.id.input_st_depression);
        inputStSlope = findViewById(R.id.input_st_slope);
        inputMajorVessels = findViewById(R.id.input_major_vessels);
        inputThalassemia = findViewById(R.id.input_thalassemia);


        cekPasien = getIntent().getParcelableExtra(EXTRA_CEK_PASIEN);
        getUmurFromPasien = getIntent().getStringExtra("umurPasien");
        getGenderFromPasien = getIntent().getStringExtra("genderPasien");

        tvResult.setText(String.valueOf(cekPasien.getResult()));
        tglPeriksa.setText(String.valueOf(cekPasien.getDateIssue()));

        inputChestPT.setText(String.valueOf(cekPasien.getChestPT()));
        inputRestingEM.setText(String.valueOf(cekPasien.getRestingEM()));
        inputStSlope.setText(String.valueOf(cekPasien.getsTSlope()));
        inputThalassemia.setText(String.valueOf(cekPasien.getThalassemia()));
        inputExerciseIA.setText(String.valueOf(cekPasien.getExerciseIA()));
        inputDateIssue.setText(String.valueOf(cekPasien.getDateIssue()));
        inputRestingBP.setText(String.valueOf(cekPasien.getRestingBP()));
        inputFastingBS.setText(String.valueOf(cekPasien.getFastingBS()));
        inputCholesterol.setText(String.valueOf(cekPasien.getCholesterol()));
        inputMaxHeartRate.setText(String.valueOf(cekPasien.getMaxHeartRate()));
        inputStDepression.setText(String.valueOf(cekPasien.getsTDepression()));
        inputMajorVessels.setText(String.valueOf(cekPasien.getMajorVessels()));

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


}