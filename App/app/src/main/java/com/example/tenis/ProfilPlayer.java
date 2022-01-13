package com.example.tenis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URL;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfilPlayer extends Fragment implements View.OnClickListener {
    DatabaseReference reff;
    DocumentReference docRef;
    FirebaseFirestore db;
    private TextView emailP, lastNameP, firstNameP, phoneP, birthP, levelP;
    private TextView emailEdit, lastNameEdit, firstNameEdit, phoneEdit, birthEdit, levelEdit, levelET, birthET, image;
    private RelativeLayout nameRL, lastNameRL, emailRL, phoneRL, birthRL, levelRL;
    private TextView nameOK, lastNameOK, emailOK, phoneOK, birthOK, levelOK, levelChoose;
    private TextView nameNO, lastNameNO, emailNO, phoneNO, birthNO, levelNO;
    private EditText nameET, lastNameET, emailET, phoneET;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    String firstName, lastName, email, phone, birth, level, mail;
    ProgressDialog pd;
    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;
    ImageView photo;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profil_player, container, false);

        emailP = (TextView) rootView.findViewById(R.id.emailEditPP);
        lastNameP = (TextView) rootView.findViewById(R.id.lastNameEditPP);
        firstNameP = (TextView) rootView.findViewById(R.id.nameEditPP);
        phoneP = (TextView) rootView.findViewById(R.id.phoneEditPP);
        birthP = (TextView) rootView.findViewById(R.id.birthEditPP);
        levelP = (TextView) rootView.findViewById(R.id.levelEditPP);

        firstNameEdit = (TextView) rootView.findViewById(R.id.nameChangePP);
        lastNameEdit = (TextView) rootView.findViewById(R.id.lastNameChangePP);
        emailEdit = (TextView) rootView.findViewById(R.id.emailChangePP);
        phoneEdit = (TextView) rootView.findViewById(R.id.phoneChangePP);
        birthEdit = (TextView) rootView.findViewById(R.id.birthChangePP);
        levelEdit = (TextView) rootView.findViewById(R.id.levelChangePP);

        nameRL = (RelativeLayout) rootView.findViewById(R.id.rlName);
        lastNameRL = (RelativeLayout) rootView.findViewById(R.id.rlLastName);
        emailRL = (RelativeLayout) rootView.findViewById(R.id.rlEmail);
        phoneRL = (RelativeLayout) rootView.findViewById(R.id.rlPhone);
        birthRL = (RelativeLayout) rootView.findViewById(R.id.rlBirth);
        levelRL = (RelativeLayout) rootView.findViewById(R.id.rlLevel);

        nameOK = (TextView) rootView.findViewById(R.id.nameYes);
        lastNameOK = (TextView) rootView.findViewById(R.id.lastNameYes);
        emailOK = (TextView) rootView.findViewById(R.id.emailYes);
        phoneOK = (TextView) rootView.findViewById(R.id.phoneYes);
        birthOK = (TextView) rootView.findViewById(R.id.birthYes);
        levelOK = (TextView) rootView.findViewById(R.id.levelYes);
        levelChoose = (TextView) rootView.findViewById(R.id.levelChoose);

        nameNO = (TextView) rootView.findViewById(R.id.nameNo);
        lastNameNO = (TextView) rootView.findViewById(R.id.lastNameNo);
        emailNO = (TextView) rootView.findViewById(R.id.emailNo);
        phoneNO = (TextView) rootView.findViewById(R.id.phoneNo);
        birthNO = (TextView) rootView.findViewById(R.id.birthNo);
        levelNO = (TextView) rootView.findViewById(R.id.levelNo);

        nameET = (EditText) rootView.findViewById(R.id.nameEdit);
        lastNameET = (EditText) rootView.findViewById(R.id.lastNameEdit);
        emailET = (EditText) rootView.findViewById(R.id.emailEdit);
        phoneET = (EditText) rootView.findViewById(R.id.phoneEdit);
        birthET = (TextView) rootView.findViewById(R.id.birthEdit);
        levelET = (TextView) rootView.findViewById(R.id.levelEdit);
        image = (TextView) rootView.findViewById(R.id.image); 

        firstNameEdit.setOnClickListener(this);
        lastNameEdit.setOnClickListener(this);
        emailEdit.setOnClickListener(this);
        phoneEdit.setOnClickListener(this);
        birthEdit.setOnClickListener(this);
        levelEdit.setOnClickListener(this);
        nameOK.setOnClickListener(this);
        lastNameOK.setOnClickListener(this);
        emailOK.setOnClickListener(this);
        phoneOK.setOnClickListener(this);
        birthOK.setOnClickListener(this);
        levelOK.setOnClickListener(this);
        nameNO.setOnClickListener(this);
        lastNameNO.setOnClickListener(this);
        emailNO.setOnClickListener(this);
        phoneNO.setOnClickListener(this);
        birthNO.setOnClickListener(this);
        levelNO.setOnClickListener(this);
        birthET.setOnClickListener(this);
        levelET.setOnClickListener(this);
        image.setOnClickListener(this);

        photo = (ImageView) rootView.findViewById(R.id.photo2);

        photo.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        completeThePanel();

        registerForContextMenu(levelChoose);

        pd = new ProgressDialog(getContext());

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // edytuj
            case R.id.nameChangePP:
                nameRL.setVisibility(View.VISIBLE);
                firstNameEdit.setVisibility(View.INVISIBLE);
                break;
            case R.id.lastNameChangePP:
                lastNameRL.setVisibility(View.VISIBLE);
                break;
            case R.id.emailChangePP:
                emailRL.setVisibility(View.VISIBLE);
                break;
            case R.id.phoneChangePP:
                phoneRL.setVisibility(View.VISIBLE);
                break;
            case R.id.birthChangePP:
                birthRL.setVisibility(View.VISIBLE);
                break;
            case R.id.levelChangePP:
                levelRL.setVisibility(View.VISIBLE);
                break;
                //zatwierdź edycje
            case R.id.nameYes:
                changeData(nameET,firstNameP,nameRL,"first_name", "imię", v);
                firstNameEdit.setVisibility(View.VISIBLE);
                break;
            case R.id.lastNameYes:
                changeData(lastNameET,lastNameP,lastNameRL,"last_name", "nazwisko", v);
                break;
            case R.id.emailYes:
                changeData(emailET,emailP,emailRL,"email", "email", v);
                break;
            case R.id.phoneYes:
                changeData(phoneET,phoneP,phoneRL,"phone", "numer telefonu", v);
                break;
            case R.id.birthYes:
                changeDataText(birthET,birthP,birthRL,"birth", "datę urodzenia", v);
                break;
            case R.id.levelYes:
                changeDataText(levelET,levelP,levelRL,"level", "poziom", v);
                break;
            //anuluj edycje
            case R.id.nameNo:
                nameRL.setVisibility(View.INVISIBLE);
                firstNameEdit.setVisibility(View.VISIBLE);
                break;
            case R.id.lastNameNo:
                lastNameRL.setVisibility(View.INVISIBLE);
                break;
            case R.id.emailNo:
                emailRL.setVisibility(View.INVISIBLE);
                break;
            case R.id.phoneNo:
                phoneRL.setVisibility(View.INVISIBLE);
                break;
            case R.id.birthNo:
                birthRL.setVisibility(View.INVISIBLE);
                break;
            case R.id.levelNo:
                levelRL.setVisibility(View.INVISIBLE);
                break;
            //kalendarz
            case R.id.birthEdit:
                dateOfBirth();
                break;
                //dodaj zdjęcie
            case R.id.image:
                openImage();
                break;
            case R.id.photo2:
                Image.downloadImage(mail, photo);
                break;
        }

    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            uploadImage();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        pd.setMessage("Dodawanie...");
        pd.show();
        if(imageUri !=null){
            //StorageReference ref = FirebaseStorage.getInstance().getReference().child(mail).child(System.currentTimeMillis()+ "."+ getFileExtension(imageUri));
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(mail).child(mail+ "."+ getFileExtension(imageUri));
            ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Log.d("Dodano", url);
                            pd.dismiss();
                            Toast.makeText(getContext(), "Dodano zdjęcie", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void changeData(EditText editText, TextView textView, RelativeLayout rl, String field, String k, View v){
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        String value = editText.getText().toString();
        textView.setText(value);
        rl.setVisibility(View.INVISIBLE);
        docRef = db.collection("players").document(mail);
        docRef.update(field, value);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        Toast.makeText(getContext(), "Zmieniono "+k+" na " + value, Toast.LENGTH_SHORT).show();
    }
    private void changeDataText(TextView editText, TextView textView, RelativeLayout rl, String field, String k, View v){
        InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        String value = editText.getText().toString();
        textView.setText(value);
        rl.setVisibility(View.INVISIBLE);
        docRef = db.collection("players").document(mail);
        docRef.update(field, value);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        Toast.makeText(getContext(), "Zmieniono "+k+" na " + value, Toast.LENGTH_SHORT).show();
    }

    private void completeThePanel(){
        reff = FirebaseDatabase.getInstance().getReference().child("Email");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mail4 = snapshot.getValue().toString();
                mail = mail4;
                docRef = db.collection("players").document(mail4);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                firstName = document.get("first_name").toString();
                                lastName = document.get("last_name").toString();
                                email = document.get("email").toString();
                                phone = document.get("phone").toString();
                                birth = document.get("birth").toString();
                                level = document.get("level").toString();
                                firstNameP.setText(firstName);
                                lastNameP.setText(lastName);
                                emailP.setText(email);
                                phoneP.setText(phone);
                                birthP.setText(birth);
                                levelP.setText(level);
                                Image.downloadImage(mail, photo);
                            }
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    public void dateOfBirth(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date;
                if(day<10 && month<10) {
                    date = year + "-0" + month + "-0" + day;
                    birthET.setText(date);
                }
                else if(day<10 && month>=10)  {
                    date = year + "-" + month + "-0" + day;
                    birthET.setText(date);
                }
                else if(day>=10 && month<10){
                    date = year + "-0" + month + "-" + day;
                    birthET.setText(date);
                }
                else {
                    date = year + "-" + month + "-" + day;
                    birthET.setText(date);
                }
            }
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 0, 0, "1.0");
        menu.add(0,1,0,"2.0");
        menu.add(0,2,0,"3.0");
        menu.add(0,3,0,"4.0");
        menu.add(0,4,0,"5.0");
        menu.add(0,5,0,"6.0");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                levelET.setText("1.0");
                break;
            case 1:
                levelET.setText("2.0");
                break;
            case 2:
                levelET.setText("3.0");
                break;
            case 3:
                levelET.setText("4.0");
                break;
            case 4:
                levelET.setText("5.0");
                break;
            case 5:
                levelET.setText("6.0");
                break;
        }
        return super.onContextItemSelected(item);
    }

}