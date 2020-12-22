package com.agung.deteksikardiovaskular.ui.login;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agung.deteksikardiovaskular.R;
import com.agung.deteksikardiovaskular.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText inputName, inputEmail, inputPassword, inputInstansi;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private AVLoadingIndicatorView avi;
    private TextView backgroundWhite;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;
    private StorageReference mStorageRef;

    private Button mButtonChooseImage;
    private TextView txtImage;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    UploadTask upload;
    private Uri downloadUrl;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        inputName = v.findViewById(R.id.input_name);
        inputEmail = v.findViewById(R.id.input_email);
        inputPassword = v.findViewById(R.id.input_password);
        inputInstansi = v.findViewById(R.id.input_instansi);

        mButtonChooseImage = v.findViewById(R.id.choose_file);
        txtImage = v.findViewById(R.id.txt_choose_file);
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , PICK_IMAGE_REQUEST );
            }
        });

        avi= (AVLoadingIndicatorView) v.findViewById(R.id.avi);
        backgroundWhite = v.findViewById(R.id.background_white);

        avi.setIndicator("BallPulseIndicator");
        showLoading(false);

        btnRegister = v.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading(true);
                String name = inputName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String instansi = inputInstansi.getText().toString().trim();
                String foto = txtImage.getText().toString();

                boolean isEmptyFields = false;
                if(TextUtils.isEmpty(name)) {
                    showLoading(false);
                    isEmptyFields = true;
                    inputName.setError("Field ini tidak boleh kosong");
                }
                if(TextUtils.isEmpty(email)) {
                    showLoading(false);
                    isEmptyFields = true;
                    inputEmail.setError("Field ini tidak boleh kosong");
                }

                if (password.length() < 8) {
                    showLoading(false);
                    isEmptyFields = true;
                    inputPassword.setError("Password harus lebih dari 7");
                } else {
                    if(TextUtils.isEmpty(password)) {
                        showLoading(false);
                        isEmptyFields = true;
                        inputPassword.setError("Field ini tidak boleh kosong");
                    }
                }

                if(TextUtils.isEmpty(instansi)) {
                    showLoading(false);
                    isEmptyFields = true;
                    inputInstansi.setError("Field ini tidak boleh kosong");
                }

                if(TextUtils.isEmpty(foto)) {
                    showLoading(false);
                    isEmptyFields = true;
                    txtImage.setError("Field ini tidak boleh kosong");
                }
                if(!TextUtils.isEmpty(name) && password.length() >= 8 && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(instansi) && !TextUtils.isEmpty(foto)) {
                    showLoading(true);
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser userUID = FirebaseAuth.getInstance().getCurrentUser();
                                    if (task.isSuccessful()) {
                                        FirebaseApp.initializeApp(getActivity());

                                        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
                                        mStorageRef = mStorageRef.child(System.currentTimeMillis()
                                                + "." + getFileExtension(mImageUri));
                                        mUploadTask = mStorageRef.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        mFirebaseInstance = FirebaseDatabase.getInstance();
                                                        mDatabaseReference = mFirebaseInstance.getReference("KardiovaskularDetektor");
                                                        mDatabaseReference.child("users").child(userUID.getUid()).setValue(new ModelUser(name, email, instansi, task.getResult().toString()));
                                                        showLoading(false);

                                                        inputName.setText(null);
                                                        inputEmail.setText(null);
                                                        inputPassword.setText(null);
                                                        inputInstansi.setText(null);
                                                        mImageUri = null;
                                                        txtImage.setText(null);
                                                        Log.d(TAG, "createUserWithEmail:success");
                                                        Toast.makeText(getActivity(), "Berhasil mendaftar",
                                                                Toast.LENGTH_SHORT).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                    }
                                                });
                                            }
                                        });

                                    } else {
                                        showLoading(false);
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Email sudah terdaftar",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void submitUser(ModelUser user) {

    }

    public void showLoading(boolean state) {
        if(state) {
            backgroundWhite.setVisibility(View.VISIBLE);
            avi.show();
        } else {
            backgroundWhite.setVisibility(View.GONE);
            avi.hide();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            String picturePath = getPath( getActivity( ).getApplicationContext( ), mImageUri );
            txtImage.setText(picturePath);
            txtImage.setError(null);
        }
    }
}