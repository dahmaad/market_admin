package com.example.aaaaaaaaa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Document;

public class adminpage extends AppCompatActivity {
    ImageView qt, pic;
    Button add;
    EditText name, cat, price, des;
    TextView img;
    Uri uri;
    StorageReference storageReference;
    static final int PICK_IMAGE_REQUEST = 1;
    Uri uriPic;



    FirebaseFirestore baseStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);
        qt = findViewById(R.id.quit);
        pic = findViewById(R.id.pic);
        name = findViewById(R.id.name);
        cat = findViewById(R.id.cat);
        price = findViewById(R.id.prx);
        des = findViewById(R.id.dx);
        img = findViewById(R.id.img);
        add = findViewById(R.id.btnAdd);


        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGal();
            }
        });
        qt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(adminpage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsData();
            }
        });
    }

    private void openGal() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriPic = data.getData();
            pic.setImageURI(uriPic);
        }
    }

    private void productsData(){
        String nm = name.getText().toString();
        String category = cat.getText().toString();
        String prx = price.getText().toString();
        String description = des.getText().toString();


        storageReference = FirebaseStorage.getInstance().getReference("products");
        baseStore = FirebaseFirestore.getInstance();

        //Products empty = new Products();
        //baseStore.collection("Products").document("bioMarketStore").collection("products").add(empty);
       // String id = baseStore.collection("Products").document("bioMarketStore").collection("products").getId().toString();

        String imageUrl = setImage(nm);


        Products products = new Products(nm, prx, category, description, imageUrl);


        baseStore.collection("Products").document("bioMarketStore").collection("products").add(products)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(adminpage.this, "data has been sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(adminpage.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private String setImage(String nm) {
        if (uriPic != null){
            StorageReference imgRef = storageReference.child(nm + ".jpg");
            imgRef.putFile(uriPic);

            StorageReference ref = FirebaseStorage.getInstance().getReference("products/"+nm + ".jpg");

            return ref.getDownloadUrl().toString();
        }
        return null;
    }

}
