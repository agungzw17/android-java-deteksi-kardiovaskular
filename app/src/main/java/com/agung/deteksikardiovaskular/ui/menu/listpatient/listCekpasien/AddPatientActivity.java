package com.agung.deteksikardiovaskular.ui.menu.listpatient.listCekpasien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelCekPasien;
import com.agung.deteksikardiovaskular.ui.datapicker.DatePickerFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity implements DatePickerFragment.DialogDateListener, AdapterView.OnItemSelectedListener{
    String getKeyPasien;
    final String DATE_PICKER_TAG = "DatePicker";

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private TextView inputDateIssue;
    private EditText inputChestPT;
    private EditText inputRestingBP;
    private EditText inputFastingBS;
    private EditText inputCholesterol;
    private EditText inputRestingEM;
    private EditText inputMaxHeartRate;
//    private EditText inputExerciseIA;
    private EditText inputStDepression;
    private EditText inputStSlope;
    private EditText inputMajorVessels;
    private EditText inputThalassemia;
    private EditText inputGetGender;
    private EditText inputGetUmur;

    int finalRestingBP;
    int finalFastingBS;
    int finalCholesterol;
    int finalMaxHR;
    int finalMajorVessels;
    float finalSTDepression;

    private FirebaseUser user;

    private int exerciseIA;

    String getSpinnerChesPT;
    String getSpinnerRestECG;
    String getSpinnerSTSLOPE;
    String getSpinnerThalassemia;

    String getGenderFromPasien;
    String getUmurFromPasien;

    Interpreter tflite;

    long diff;
    int finalUmur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Cek Pasien");

        user = FirebaseAuth.getInstance().getCurrentUser();

        inputDateIssue = findViewById(R.id.input_cek_date);
//        inputChestPT = findViewById(R.id.input_chestPT);
        inputRestingBP = findViewById(R.id.input_restingBP);
        inputFastingBS = findViewById(R.id.input_fastingBS);
        inputCholesterol = findViewById(R.id.input_cholesterol);
//        inputRestingEM = findViewById(R.id.input_resting_electrocardiographic);
        inputMaxHeartRate = findViewById(R.id.input_maximum_heart_rate);
//        inputExerciseIA = findViewById(R.id.input_exercise_induced_angina);
        inputStDepression = findViewById(R.id.input_st_depression);
//        inputStSlope = findViewById(R.id.input_st_slope);
        inputMajorVessels = findViewById(R.id.input_major_vessels);
//        inputThalassemia = findViewById(R.id.input_thalassemia);
        inputGetGender = findViewById(R.id.input_get_gender);
        inputGetUmur = findViewById(R.id.input_get_date);

        getUmurFromPasien = getIntent().getStringExtra("umurPasien");
        getGenderFromPasien = getIntent().getStringExtra("genderPasien");
        getKeyPasien = getIntent().getStringExtra("keyPasien");
        System.out.println("idnya" + getKeyPasien);

        inputGetGender.setText(getGenderFromPasien);
        inputGetUmur.setText(getUmurFromPasien);

        String date1 = inputGetUmur.getText().toString().trim();
        try {
            Date dateBirthInDate = new SimpleDateFormat("dd-MM-yyyy").parse(date1);
            Date currentTime = Calendar.getInstance().getTime();
            diff = currentTime.getTime() - dateBirthInDate.getTime();
            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            double umurTahunDouble = diffDays/365.25;
            finalUmur = (int) Math.round(umurTahunDouble);
            System.out.println("UMURNYA SEKARANG ADALAH" + finalUmur + " doublenya " + umurTahunDouble);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("UMURNYA SEKARANG ADALAH" + finalUmur);


        onSpinner();

        inputDateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
                return;
            }
        });

        //For Tensor Flow
                try {
                    tflite = new Interpreter(loadModelFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

        BottomAppBar btnCekPasien = findViewById(R.id.btn_cek);
        btnCekPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(getApplication());
                boolean isEmptyFields = false;

                String gender = inputGetGender.getText().toString();

                String dateIssue = inputDateIssue.getText().toString();
                if(TextUtils.isEmpty(dateIssue)) {
                    isEmptyFields = true;
                    inputDateIssue.setError("Field ini tidak boleh kosong");
                }

                String chestPT = getSpinnerChesPT;
//                String chestPT = inputChestPT.getText().toString().trim();
//                if(TextUtils.isEmpty(chestPT)) {
//                    isEmptyFields = true;
//                    inputChestPT.setError("Field ini tidak boleh kosong");
//                }

                String restingBP = inputRestingBP.getText().toString().trim();
                if(TextUtils.isEmpty(restingBP)) {
                    isEmptyFields = true;
                    restingBP = "0";
                    inputRestingBP.setError("Field ini tidak boleh kosong");
                }
                if (!TextUtils.isEmpty(restingBP)) {
                    finalRestingBP = Integer.parseInt(restingBP);
                }

                String fastingBS = inputFastingBS.getText().toString().trim();
                if(TextUtils.isEmpty(fastingBS)) {
                    isEmptyFields = true;
                    inputFastingBS.setError("Field ini tidak boleh kosong");
                }
                if (!TextUtils.isEmpty(fastingBS)) {
                    finalFastingBS = Integer.parseInt(fastingBS);
                }

                String cholesterol = inputCholesterol.getText().toString().trim();
                if(TextUtils.isEmpty(cholesterol)) {
                    isEmptyFields = true;
                    cholesterol = "0";
                    inputCholesterol.setError("Field ini tidak boleh kosong");
                }

                if(!TextUtils.isEmpty(cholesterol)) {
                    finalCholesterol = Integer.parseInt(cholesterol);
                }

                String restingEM = getSpinnerRestECG;
//                String restingEM = inputRestingEM.getText().toString().trim();
//                if(TextUtils.isEmpty(restingEM)) {
//                    isEmptyFields = true;
//                    inputRestingEM.setError("Field ini tidak boleh kosong");
//                }

                String maxHR = inputMaxHeartRate.getText().toString().trim();
                if(TextUtils.isEmpty(maxHR)) {
                    isEmptyFields = true;
                    maxHR = "0";
                    inputMaxHeartRate.setError("Field ini tidak boleh kosong");
                }
                if (!TextUtils.isEmpty(maxHR)) {
                    finalMaxHR = Integer.parseInt(maxHR);
                }

//                String exerciseIA = inputExerciseIA.getText().toString().trim();
                String exerciseIAToString = String.valueOf(exerciseIA);

                String stDepression = inputStDepression.getText().toString().trim();
                if(TextUtils.isEmpty(stDepression)) {
                    isEmptyFields = true;
                    stDepression = "0";
                    inputStDepression.setError("Field ini tidak boleh kosong");
                }
                if (!TextUtils.isEmpty(stDepression)) {
                    finalSTDepression = Float.parseFloat(stDepression);
                }

                String stSlope = getSpinnerSTSLOPE;
//                String stSlope = inputStSlope.getText().toString().trim();
//                if(TextUtils.isEmpty(stSlope)) {
//                    isEmptyFields = true;
//                    inputStSlope.setError("Field ini tidak boleh kosong");
//                }

                String majorVessels = inputMajorVessels.getText().toString().trim();
                if(TextUtils.isEmpty(majorVessels)) {
                    isEmptyFields = true;
                    majorVessels = "0";
                    inputMajorVessels.setError("Field ini tidak boleh kosong");
                }
                if (!TextUtils.isEmpty(majorVessels)) {
                    finalMajorVessels = Integer.parseInt(majorVessels);
                }

                String thalassemia = getSpinnerThalassemia;
//                String thalassemia = inputThalassemia.getText().toString().trim();
//                if(TextUtils.isEmpty(thalassemia)) {
//                    isEmptyFields = true;
//                    inputThalassemia.setError("Field ini tidak boleh kosong");
//                }

//                int finalRestingBP = Integer.parseInt(restingBP);
//                int finalFastingBS = Integer.parseInt(fastingBS);
//                int finalCholesterol = Integer.parseInt(cholesterol);
//                int finalMaxHR = Integer.parseInt(maxHR);
//                int finalMajorVessels = Integer.parseInt(majorVessels);
//                float finalSTDepression = Float.parseFloat(stDepression);

                //Convert user input into model input
                String chest_pain_type_atypical_angina = "0";
                String chest_pain_type_nonAnginal_pain = "0";
                String chest_pain_type_typical_angina = "0";
                String fasting_blood_sugar_lower_than_120mg_ml = "0";
                String rest_ecg_left_ventricular_hypertrophy = "0";
                String rest_ecg_normal = "0";
                String exercise_induced_angina_yes = "0";
                String st_slope_flat = "0";
                String st_slope_upsloping = "0";
                String thalassemia_fixed_defect = "0";
                String thalassemia_normal= "0";
                String thalassemia_reversable_defect = "0";

                String result = "Negative";



                if (getSpinnerChesPT.equals("atypical angina")){
                    chest_pain_type_atypical_angina = "1";
                } else if(getSpinnerChesPT.equals("typical angina")){
                    chest_pain_type_typical_angina = "1";
                }else if(getSpinnerChesPT.equals("non-anginal pain")) {
                    chest_pain_type_nonAnginal_pain = "1";
                }

                if (finalFastingBS < 120){
                    fasting_blood_sugar_lower_than_120mg_ml = "1";
                }

                if (getSpinnerRestECG.equals("normal")) {
                    rest_ecg_normal = "1";
                }else if(getSpinnerRestECG.equals("left ventricular hypertrophy")){
                    rest_ecg_left_ventricular_hypertrophy = "1";
                }

                if (exerciseIAToString.equals(1)){
                    exercise_induced_angina_yes = "1";
                }

                if (getSpinnerSTSLOPE.equals("upsloping")){
                    st_slope_upsloping = "1";
                } else if(getSpinnerSTSLOPE.equals("flat")){
                    st_slope_flat = "1";
                }

                if (getSpinnerThalassemia.equals("normal")){
                    thalassemia_normal = "1";
                } else if(getSpinnerThalassemia.equals("fixed defect")){
                    thalassemia_fixed_defect = "1";
                } else if(getSpinnerThalassemia.equals("reversable defect")){
                    thalassemia_reversable_defect = "1";
                }
//
//
//
                String finalUmurToString = String.valueOf(finalUmur);
                String[] inputForTF= {finalUmurToString, restingBP, cholesterol, maxHR, stDepression, majorVessels, gender, chest_pain_type_atypical_angina, chest_pain_type_nonAnginal_pain,
                        chest_pain_type_typical_angina, fasting_blood_sugar_lower_than_120mg_ml, rest_ecg_left_ventricular_hypertrophy, rest_ecg_normal, exercise_induced_angina_yes, st_slope_flat,
                        st_slope_upsloping, thalassemia_fixed_defect, thalassemia_normal, thalassemia_reversable_defect};

                // Mencari hasil kalkulasi
                if (doInference(inputForTF) > 0.5){
                    result = "Positive";
                }

                System.out.println("hasilnyaCUK " + doInference(inputForTF));



                if( !TextUtils.isEmpty(majorVessels)  && !TextUtils.isEmpty(stDepression) && !TextUtils.isEmpty(maxHR)
                         && !TextUtils.isEmpty(cholesterol) && !TextUtils.isEmpty(fastingBS) && !TextUtils.isEmpty(restingBP) && !TextUtils.isEmpty(dateIssue)) {
                    mFirebaseInstance = FirebaseDatabase.getInstance();
                    mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                    mDatabaseReference.child("users").child(user.getUid()).child("pasien").child(getKeyPasien).child("cekPasien").push().setValue(new ModelCekPasien(dateIssue, chestPT, restingEM, exerciseIAToString, stSlope, thalassemia, finalRestingBP, finalFastingBS, finalCholesterol, finalMaxHR, finalMajorVessels, finalSTDepression, result)).addOnSuccessListener(AddPatientActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            inputDateIssue.setText("0");
                            inputRestingBP.setText("0");
                            inputFastingBS.setText("0");
                            inputCholesterol.setText("0");
                            inputMaxHeartRate.setText("0");
                            inputStDepression.setText("0");
                            inputMajorVessels.setText("0");
                            inputGetGender.setText("0");
                            inputGetUmur.setText("0");
                            Toast.makeText(AddPatientActivity.this, "Data barang berhasil di simpan !", Toast.LENGTH_LONG).show();

                        }
                    });
                }


            }
        });


    }

    public void onSpinner(){
        Spinner spinnerChesPT = findViewById(R.id.spinner_chespt);
        if(spinnerChesPT != null);{
            spinnerChesPT.setOnItemSelectedListener(AddPatientActivity.this);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chestPainType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(spinnerChesPT != null){
            spinnerChesPT.setAdapter(adapter);
        }

        spinnerChesPT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerLabel = parent.getItemAtPosition(position).toString();
                getSpinnerChesPT = spinnerLabel;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //
        Spinner spinnerResECG = findViewById(R.id.spinner_resting_electrocardiographic);
        if(spinnerResECG != null);{
            spinnerResECG.setOnItemSelectedListener(AddPatientActivity.this);
        }

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.rest_ecg, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(spinnerResECG != null){
            spinnerResECG.setAdapter(adapter1);
        }
        spinnerResECG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerLabel = parent.getItemAtPosition(position).toString();
                getSpinnerRestECG = spinnerLabel;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //
        Spinner spinnerSlope = findViewById(R.id.spinner_stslope);
        if(spinnerSlope != null);{
            spinnerSlope.setOnItemSelectedListener(AddPatientActivity.this);
        }

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.st_slope, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(spinnerSlope != null){
            spinnerSlope.setAdapter(adapter2);
        }

        spinnerSlope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerLabel = parent.getItemAtPosition(position).toString();
                getSpinnerSTSLOPE = spinnerLabel;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //
        Spinner spinnerThalassemia = findViewById(R.id.spinner_thalassemia);
        if(spinnerThalassemia != null);{
            spinnerThalassemia.setOnItemSelectedListener(AddPatientActivity.this);
        }

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.thalassemia, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(spinnerThalassemia != null){
            spinnerThalassemia.setAdapter(adapter3);
        }

        spinnerThalassemia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerLabel = parent.getItemAtPosition(position).toString();
                getSpinnerThalassemia = spinnerLabel;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////        String spinnerLabel = parent.getItemAtPosition(position).toString();
//////        inputChestPT.setText(spinnerLabel);
//        switch(parent.getId()){
//            case R.id.spinner_chespt:
//                String spinnerLabel = parent.getItemAtPosition(position).toString();
//                inputRestingBP.setText(spinnerLabel);
//                break;
//            case R.id.spinner_resting_electrocardiographic:
//                String spinnerLabel1 = parent.getItemAtPosition(position).toString();
//                inputRestingBP.setText(spinnerLabel1);
//                break;
//            case R.id.spinner_stslope:
//                String spinnerLabel2 = parent.getItemAtPosition(position).toString();
//                inputRestingBP.setText(spinnerLabel2);
//                break;
//            case R.id.spinner_thalassemia:
//                String spinnerLabel3 = parent.getItemAtPosition(position).toString();
//                inputRestingBP.setText(spinnerLabel3);
//                break;
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.rb_yes:
                if(checked)
                    exerciseIA = 1;
                break;

            case R.id.rb_no:
                if(checked)
                    exerciseIA = 0;
                break;

            default:
                break;
        }
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
        inputDateIssue.setText(dateFormat.format(calendar.getTime()));
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float doInference(String[] value){
        float[] inputVal = new float[19];
        inputVal[0] = Float.valueOf(value[0]);
        inputVal[1] = Float.valueOf(value[1]);
        inputVal[2] = Float.valueOf(value[2]);
        inputVal[3] = Float.valueOf(value[3]);
        inputVal[4] = Float.valueOf(value[4]);
        inputVal[5] = Float.valueOf(value[5]);
        inputVal[6] = Float.valueOf(value[6]);
        inputVal[7] = Float.valueOf(value[7]);
        inputVal[8] = Float.valueOf(value[8]);
        inputVal[9] = Float.valueOf(value[9]);
        inputVal[10] = Float.valueOf(value[10]);
        inputVal[11] = Float.valueOf(value[11]);
        inputVal[12] = Float.valueOf(value[12]);
        inputVal[13] = Float.valueOf(value[13]);
        inputVal[14] = Float.valueOf(value[14]);
        inputVal[15] = Float.valueOf(value[15]);
        inputVal[16] = Float.valueOf(value[16]);
        inputVal[17] = Float.valueOf(value[17]);
        inputVal[18] = Float.valueOf(value[18]);

        float[][] outputval = new float[1][1];

        tflite.run(inputVal, outputval);

        float inferredValue = outputval[0][0];

        return inferredValue;

    }

    public float doInference1(){
        float[] inputVal = new float[19];
        inputVal[0] = Float.valueOf(58);
        inputVal[1] = Float.valueOf(112);
        inputVal[2] = Float.valueOf(230);
        inputVal[3] = Float.valueOf(165);
        inputVal[4] = Float.valueOf("2.5");
        inputVal[5] = Float.valueOf(1);
        inputVal[6] = Float.valueOf(1);
        inputVal[7] = Float.valueOf(1);
        inputVal[8] = Float.valueOf(0);
        inputVal[9] = Float.valueOf(0);
        inputVal[10] = Float.valueOf(1);
        inputVal[11] = Float.valueOf(0);
        inputVal[12] = Float.valueOf(1);
        inputVal[13] = Float.valueOf(0);
        inputVal[14] = Float.valueOf(0);
        inputVal[15] = Float.valueOf(1);
        inputVal[16] = Float.valueOf(0);
        inputVal[17] = Float.valueOf(0);
        inputVal[18] = Float.valueOf(1);

        float[][] outputval = new float[1][1];

        tflite.run(inputVal, outputval);

        float inferredValue = outputval[0][0];

        return inferredValue;



    }
}