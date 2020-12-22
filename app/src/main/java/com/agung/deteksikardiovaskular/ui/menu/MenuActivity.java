package com.agung.deteksikardiovaskular.ui.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.ui.login.LoginActivity;
import com.agung.deteksikardiovaskular.ui.menu.listpatient.PatientActivity;
import com.agung.deteksikardiovaskular.ui.menu.registerpatient.RegisterPatientActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvInstansi;
    private ImageView tvImage;
    private CardView cvDaftarPasien;
    private CardView cvListPasien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            this.startActivity(loginActivity);
            this.finish();
        }
        tvName = findViewById(R.id.tv_item_name);
        tvEmail = findViewById(R.id.tv_item_email);
        tvInstansi = findViewById(R.id.tv_item_instansi);
        tvImage = findViewById(R.id.tv_item_image);
        tvEmail.setText(user.getEmail());

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
        mDatabaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String instansi = dataSnapshot.child("instansi").getValue().toString();
                String photoProfil = dataSnapshot.child("photoProfil").getValue().toString();
                tvName.setText(name);
                tvInstansi.setText(instansi);
                Glide.with(MenuActivity.this).load(photoProfil).into(tvImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cvDaftarPasien = findViewById(R.id.cv_daftar_pasien);
        cvDaftarPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPatientActivity = new Intent((Context) MenuActivity.this, RegisterPatientActivity.class);
                MenuActivity.this.startActivity(registerPatientActivity);
            }
        });

        cvListPasien = findViewById(R.id.cv_list_pasien);
        cvListPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patientActivity = new Intent((Context) MenuActivity.this, PatientActivity.class);
                MenuActivity.this.startActivity(patientActivity);
            }
        });

        LinearLayout profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivity = new Intent((Context) MenuActivity.this, ProfileActivity.class);
                MenuActivity.this.startActivity(profileActivity);
            }
        });

    }
}