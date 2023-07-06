package com.example.client.Ui.base_classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BasePresenter {


    public static FirebaseFirestore firestore ;
   public FirebaseStorage firebaseStorage;

   public FirebaseDatabase db;
   public FirebaseAuth firebaseAuth;



    public BasePresenter() {
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db=FirebaseDatabase.getInstance();


    }

}
