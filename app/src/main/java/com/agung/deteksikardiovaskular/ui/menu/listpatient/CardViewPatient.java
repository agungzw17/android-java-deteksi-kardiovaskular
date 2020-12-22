package com.agung.deteksikardiovaskular.ui.menu.listpatient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelPasien;
import com.agung.deteksikardiovaskular.ui.menu.listpatient.listCekpasien.CekPasienActivity;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CardViewPatient extends RecyclerView.Adapter<CardViewPatient.CardViewViewHolder> {
    private Context context;
    private ArrayList<ModelPasien> listPasien;
    private Bundle bundle;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private ArrayList<ModelPasien> list = new ArrayList<>();

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public CardViewPatient(Context context) {
        this.context = context;
    }

    public ArrayList<ModelPasien> getListPasien() {
        return listPasien;
    }

    public void setListPasien(ArrayList<ModelPasien> listPasien) {
        this.listPasien = listPasien;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient,parent,  false);
        return new CardViewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardViewPatient.CardViewViewHolder holder, final int position) {
        holder.tvName.setText(getListPasien().get(position).getName());
        holder.tvNik.setText(getListPasien().get(position).getNoKtp());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        holder.cvPatient.setCardBackgroundColor(color);
        holder.cvPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cekPasienActivity = new Intent(context, CekPasienActivity.class);
                cekPasienActivity.putExtra(CekPasienActivity.EXTRA_PASIEN, listPasien.get(position));
                context.startActivity(cekPasienActivity);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onItemClickCallback.onItemClicked(listPasien.get(holder.getAdapterPosition()), view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListPasien().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvNik;
        CardView cvPatient;
        ImageView overflow;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNik = itemView.findViewById(R.id.tv_nik);
            cvPatient = itemView.findViewById(R.id.cvPatient);
            overflow = itemView.findViewById(R.id.overflow);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ModelPasien data, View v);
    }
}
