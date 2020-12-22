package com.agung.deteksikardiovaskular.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.ui.menu.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        inputEmail = v.findViewById(R.id.input_email);
        inputPassword = v.findViewById(R.id.input_password);
        btnLogin = v.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmptyFields = false;

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    isEmptyFields = true;
                    inputEmail.setError("Field ini tidak boleh kosong");
                }

                if(TextUtils.isEmpty(password)) {
                    isEmptyFields = true;
                    inputPassword.setError("Field ini tidak boleh kosong");
                }

                if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getActivity(), "Login berhasil",
                                                Toast.LENGTH_SHORT).show();
                                        Intent loginActivity = new Intent(getActivity(), MenuActivity.class);
                                        getActivity().startActivity(loginActivity);
                                        getActivity().finish();
                                    } else {
                                        Toast.makeText(getActivity(), "Email atau password salah",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}