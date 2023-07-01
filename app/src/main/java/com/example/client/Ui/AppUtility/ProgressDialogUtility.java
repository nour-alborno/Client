package com.example.client.Ui.AppUtility;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtility {

  public static ProgressDialog showProgressBar(Context context,String Message){
       //Setting the progress
       ProgressDialog progressDialog= new ProgressDialog(context);
       progressDialog.setTitle(Message);
       progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       progressDialog.show();

       return progressDialog;
   }


}
