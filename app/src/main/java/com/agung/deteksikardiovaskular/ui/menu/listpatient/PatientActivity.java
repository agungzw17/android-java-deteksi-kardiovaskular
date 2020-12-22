package com.agung.deteksikardiovaskular.ui.menu.listpatient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelPasien;
import com.agung.deteksikardiovaskular.ui.menu.EditPatientActivity;
import com.agung.deteksikardiovaskular.ui.menu.MenuActivity;
import com.agung.deteksikardiovaskular.ui.menu.registerpatient.RegisterPatientActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientActivity extends AppCompatActivity {
    private RecyclerView rvPatient;
    private ArrayList<ModelPasien> list = new ArrayList<>();

    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser user;
    private static final String STATE_RESULT = "state_result";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_patient);
        user = FirebaseAuth.getInstance().getCurrentUser();
        rvPatient = findViewById(R.id.rv_patient);
        rvPatient.setHasFixedSize(true);

        getDataPatient();

    }

    private void showRecyclerCardView(){
        rvPatient.setLayoutManager(new GridLayoutManager(this, 2));
        CardViewPatient cardViewPatient = new CardViewPatient(this);
        cardViewPatient.setListPasien(list);
        rvPatient.setAdapter(cardViewPatient);

        cardViewPatient.setOnItemClickCallback(new CardViewPatient.OnItemClickCallback() {

            @Override
            public void onItemClicked(ModelPasien data, View v) {
                // Membuat object popumemu
                PopupMenu popupMenu = new PopupMenu(PatientActivity.this, v);

                // Inflate menu ke dalam popupmenu
                popupMenu.inflate(R.menu.popup_menu);

                // Menampilkan menu
                popupMenu.show();

                // Onclick pada salah satu menu pada popupmenu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PatientActivity.this);
                                alertDialogBuilder.setMessage("Are you sure delete " + data.getName() + " ?");
                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        FirebaseApp.initializeApp(PatientActivity.this);
                                        mFirebaseInstance = FirebaseDatabase.getInstance();
                                        mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                                        if (mDatabaseReference != null) {
                                            final String hasil = data.getKey();
                                            System.out.println("hasilnya" + hasil);
                                            mDatabaseReference.child("users").child(user.getUid()).child("pasien").child(hasil)
                                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void mVoid) {
                                                    cardViewPatient.setListPasien(null);
                                                    mFirebaseInstance = FirebaseDatabase.getInstance();
                                                    mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                                                    mDatabaseReference.child("users").child(user.getUid()).child("pasien").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                                                                ModelPasien pasien = mDataSnapshot.getValue(ModelPasien.class);
                                                                pasien.setKey(mDataSnapshot.getKey());
                                                                list.add(pasien);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                            Toast.makeText(PatientActivity.this,
                                                                    databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                        }

                                                    });
                                                    cardViewPatient.setListPasien(list);
                                                    Toast.makeText(PatientActivity.this, "Data berhasil di hapus !" + hasil, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        Toast.makeText(PatientActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        Intent refresh = new Intent(PatientActivity.this, PatientActivity.class);
                                        PatientActivity.this.startActivity(refresh);
                                        PatientActivity.this.finish();
                                    }
                                });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                break;

                            case R.id.edit:
                                Intent editPatientActivity = new Intent((Context) PatientActivity.this, EditPatientActivity.class);
                                editPatientActivity.putExtra(EditPatientActivity.EXTRA_PASIEN, data);
                                PatientActivity.this.startActivity(editPatientActivity);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void getDataPatient(){
        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
        mDatabaseReference.child("users").child(user.getUid()).child("pasien").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    ModelPasien pasien = mDataSnapshot.getValue(ModelPasien.class);
                    pasien.setKey(mDataSnapshot.getKey());
                    list.add(pasien);
                }
                showRecyclerCardView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(PatientActivity.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}