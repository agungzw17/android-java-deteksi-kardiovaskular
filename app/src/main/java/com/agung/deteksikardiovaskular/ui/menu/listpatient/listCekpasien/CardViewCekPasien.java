package com.agung.deteksikardiovaskular.ui.menu.listpatient.listCekpasien;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelCekPasien;
import com.agung.deteksikardiovaskular.model.ModelPasien;
import com.agung.deteksikardiovaskular.ui.menu.EditPatientActivity;
import com.agung.deteksikardiovaskular.ui.menu.listpatient.CardViewPatient;
import com.agung.deteksikardiovaskular.ui.menu.listpatient.PatientActivity;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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

public class CardViewCekPasien extends RecyclerView.Adapter<CardViewCekPasien.CardViewViewHolder> {
    private Context context;
    private String keyPasien;
    private ArrayList<ModelCekPasien> listCekPasien;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser userFirebase;
    private ArrayList<ModelCekPasien> list = new ArrayList<>();
    String gender;
    String tglLahir;

    private CardViewCekPasien.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(CardViewCekPasien.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public CardViewCekPasien(Context context, String keyPasien, String gender, String tglLahir) {
        this.context = context;
        this.keyPasien = keyPasien;
        this.gender = gender;
        this.tglLahir = tglLahir;
    }

    public ArrayList<ModelCekPasien> getListCekPasien() {
        return listCekPasien;
    }

    public void setListCekPasien(ArrayList<ModelCekPasien> listCekPasien) {
        this.listCekPasien = listCekPasien;
    }

    @NonNull
    @Override
    public CardViewCekPasien.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cek_patient,parent,  false);
        return new CardViewCekPasien.CardViewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardViewCekPasien.CardViewViewHolder holder, final int position) {
        holder.tvTglPeriksa.setText(getListCekPasien().get(position).getDateIssue());
//        holder.tvNik.setText(getListPasien().get(position).getNoKtp());
//
        holder.result.setText(getListCekPasien().get(position).getResult());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        holder.cvPatient.setCardBackgroundColor(color);
        holder.cvPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailCekPasienActivity = new Intent(context, DetailCekPatientActivity.class);
                detailCekPasienActivity.putExtra(DetailCekPatientActivity.EXTRA_CEK_PASIEN, listCekPasien.get(position));
                detailCekPasienActivity.putExtra("umurPasien", gender);
                detailCekPasienActivity.putExtra("genderPasien", tglLahir);
                context.startActivity(detailCekPasienActivity);
            }
        });
//
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);

                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.getMenu().findItem(R.id.edit).setEnabled(false).setVisible(true).setTitle("");
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:

                                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
                                alertDialogBuilder.setMessage("Apakah kamu ingin menghapus cek pasien pada tanggal  " + listCekPasien.get(position).getDateIssue() + " ?");
                                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        FirebaseApp.initializeApp(context);
                                        mFirebaseInstance = FirebaseDatabase.getInstance();
                                        userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                                        mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                                        if (mDatabaseReference != null) {
                                            final String hasil = listCekPasien.get(position).getKey();
                                            System.out.println("hasilnya" + hasil);
                                            mDatabaseReference.child("users").child(userFirebase.getUid()).child("pasien").child(keyPasien).child("cekPasien").child(hasil)
                                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void mVoid) {
                                                    setListCekPasien(null);
                                                    mFirebaseInstance = FirebaseDatabase.getInstance();
                                                    mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                                                    mDatabaseReference.child("users").child(userFirebase.getUid()).child("pasien").child(keyPasien).child("cekPasien").child(hasil).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                                                                ModelCekPasien pasien = mDataSnapshot.getValue(ModelCekPasien.class);
                                                                pasien.setKey(mDataSnapshot.getKey());
                                                                list.add(pasien);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                            Toast.makeText(context,
                                                                    databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                                        }

                                                    });
                                                    setListCekPasien(list);
                                                    Toast.makeText(context, "Data berhasil dihapus !" + hasil, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                        Toast.makeText(context, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        Intent refresh = new Intent(context, CekPasienActivity.class);
                                        context.startActivity(refresh);
                                        ((Activity)context).finish();
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

//                                // Membuat object bundle
//                                bundle = new Bundle();
//
//                                // Mengisi bundle dengan data
//                                bundle.putInt(Constant.KEY_ID_KELAS, kelasModel.getId_kelas());
//                                bundle.putString(Constant.KEY_NAMA_KELAS, kelasModel.getNama_kelas());
//                                bundle.putString(Constant.KEY_NAMA_WALI, kelasModel.getNama_wali());
//
//                                // Berpindah halaman dengan membawa data
//                                context.startActivity(new Intent(context, UpdateKelasActivity.class).putExtras(bundle));
                                break;
                        }
                        return true;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListCekPasien().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView tvTglPeriksa;
        CardView cvPatient;
        ImageView overflow;
        TextView result;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTglPeriksa = itemView.findViewById(R.id.tv_tanggal_pemeriksaan);
            cvPatient = itemView.findViewById(R.id.cvPatient);
            overflow = itemView.findViewById(R.id.overflow);
            result = itemView.findViewById(R.id.tv_result);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ModelPasien data, View v);
    }
}
