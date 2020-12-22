package com.agung.deteksikardiovaskular.ui.menu.listpatient.listCekpasien;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelCekPasien;
import com.agung.deteksikardiovaskular.model.ModelPasien;
import com.agung.deteksikardiovaskular.ui.menu.listpatient.CardViewPatient;
import com.agung.deteksikardiovaskular.ui.menu.listpatient.PatientActivity;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CekPasienActivity extends AppCompatActivity {
    public static final String EXTRA_PASIEN = "extra_pasien";

    private RecyclerView rvCekPatient;
    private ArrayList<ModelCekPasien> list = new ArrayList<>();

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser user;

    ModelPasien pasien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cek_pasien);

        user = FirebaseAuth.getInstance().getCurrentUser();

        TextView inputNama = findViewById(R.id.input_nama);
        TextView inputNIK = findViewById(R.id.input_nik);
        TextView inputTTL = findViewById(R.id.input_tanggal_lahir);
        TextView inputAlamat = findViewById(R.id.input_alamat);

        pasien = getIntent().getParcelableExtra(EXTRA_PASIEN);
        inputNama.setText(pasien.getName());
        inputNIK.setText(pasien.getNoKtp());
        inputTTL.setText(pasien.getDateBirth());
        inputAlamat.setText(pasien.getAddress());

        String gender = Integer.toString(pasien.getGender());
        FloatingActionButton btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPatientActivity =  new Intent(CekPasienActivity.this, AddPatientActivity.class);
                addPatientActivity.putExtra("keyPasien", pasien.getKey());
                addPatientActivity.putExtra("umurPasien", pasien.getDateBirth());
                addPatientActivity.putExtra("genderPasien", Integer.toString(pasien.getGender()));
                CekPasienActivity.this.startActivity(addPatientActivity);
            }
        });

        CardView cvDetailPasien = findViewById(R.id.cv_detail_pasien);
//        ColorGenerator generator = ColorGenerator.MATERIAL;
//        int color = generator.getRandomColor();
//        cvDetailPasien.setCardBackgroundColor(color);
//        cvDetailPasien.setRadius(30);

        rvCekPatient = findViewById(R.id.rv_cek_patient);
        rvCekPatient.setHasFixedSize(true);

        getDataPatient(pasien.getKey());


//        NestedScrollView scrollView = findViewById(R.id.nested_scroll);
//        FrameLayout frameLayout1 = findViewById(R.id.frame_layout1);
//        frameLayout1.setVisibility(View.VISIBLE);
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                if (scrollView != null) {
//                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
//                        frameLayout1.setVisibility(View.VISIBLE);
//                    }
//                    else {
//                        frameLayout1.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
    }

    private void showRecyclerCardView(String key, String gender, String tglLahir){
        rvCekPatient.setLayoutManager(new GridLayoutManager(this, 2));
        CardViewCekPasien cardViewPatient = new CardViewCekPasien(this, key, gender, tglLahir);
        cardViewPatient.setListCekPasien(list);
        rvCekPatient.setAdapter(cardViewPatient);
    }

    private void getDataPatient(String key){
        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
        mDatabaseReference.child("users").child(user.getUid()).child("pasien").child(key).child("cekPasien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    ModelCekPasien pasien = mDataSnapshot.getValue(ModelCekPasien.class);
                    pasien.setKey(mDataSnapshot.getKey());
                    list.add(pasien);
                }
                showRecyclerCardView(key, pasien.getKey(), Integer.toString(pasien.getGender()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(CekPasienActivity.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}