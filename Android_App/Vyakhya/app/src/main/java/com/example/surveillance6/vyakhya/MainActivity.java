package com.example.surveillance6.vyakhya;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button slect_img , send ;
    Integer REQUEST_CAMERA=1,REQUEST_FILE=0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1212;
    ImageView pasteventimageview ;
    Bitmap pastimage;
    public static String TAG = "Popup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slect_img = (Button)findViewById(R.id.slect_img);

        send = (Button)findViewById(R.id.send);

        pasteventimageview = (ImageView)findViewById(R.id.pasteventimageview);

        slect_img.setOnClickListener(this);
        send.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.send)
        {

//            Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();


            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);


            final RequestData requestData = new RequestData();




            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pastimage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            String base_64_img =  Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);



            requestData.setImage(base_64_img);

            Call<ResultData> result = api.getresult(requestData);



            result.enqueue(new Callback<ResultData>() {
                @Override
                public void onResponse(Call<ResultData> call, Response<ResultData> response) {


//                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
//                    Api api_1 = retrofit.create(Api.class);
//                    Call<ResultData> res = api_1.greetUser();
////                    String msg = res.execute(Response<ResultData>);
////                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show()
//                    res.enqueue(new Callback<ResultData>(){
//                        @Override
//                        public void onResponse(Call<ResultData> call_1, Response<ResultData> response_1) {
//                            if (response_1.isSuccessful()) {
//                                String msg = response_1.body().toString();
//                                Log.d(TAG, "onResponse");
////                                print("---TTTT :: GET msg from server :: " + msg);
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                            }
//                            else{
//                                Log.d(TAG,"Hello");
//                            }
////                            String msg = response_1.body().toString();
////                            Log.d(TAG, "onResponse");
////                            print("---TTTT :: GET msg from server :: " + msg)
////                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//
//                        }
//
//
//                        @Override
//                        public void onFailure(Call<ResultData> call, Throwable t) {
//                            Toast.makeText(getApplicationContext(), "Network Error 1", Toast.LENGTH_LONG).show();
//                        }
//
//                    });

                    ResultData res = response.body();

                    if(res == null)
                        return;
                    String data = res.getData();

//                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<ResultData> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                }



            });





        }else {
            takeimagetask();

        }

    }



    public void takeimagetask()
    {
        final String[] menuforalert = {"Camera", "From Device"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setCancelable(true);
        builder.setItems(menuforalert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {


                    Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraintent.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(cameraintent, REQUEST_CAMERA);


                } else if (which == 1) {


                    if(hasPermissions()) {

                        getfilefromexternalstorage();

                    }else{


                        requestPerms();
                    }

                }
            }

        });

        builder.show();

    }

    private void requestPerms(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }


    public void getfilefromexternalstorage(){
        Intent fileintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fileintent.setType("image/*");
        startActivityForResult(Intent.createChooser(fileintent, "Select File"), REQUEST_FILE);
    }


    private boolean hasPermissions(){

        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        return true;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:

                for (int res : grantResults){
                    // if user granted all permissions.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.


            getfilefromexternalstorage();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(getBaseContext(), "Storage Permissions denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK)
        {



            if(requestCode==REQUEST_FILE)
            {


                Uri selectedimageuri = data.getData();
                Bitmap image;
                try {
                    image = decodeUri(getBaseContext(),selectedimageuri,10000);

                } catch (FileNotFoundException e) {


                    return;
                }


                if (REQUEST_FILE==0)
                {



                    pasteventimageview.setImageBitmap(image);


                    // converting the images to base64 string for easy transfer
                    if (pasteventimageview.getDrawable() != null) {


                        pastimage = image;



                    }




                }



            }
            else if (requestCode==REQUEST_CAMERA)
            {
                Bundle bundle = data.getExtras();
                final Bitmap image=(Bitmap) bundle.get("data");
                if (REQUEST_CAMERA==1)
                {
                    pasteventimageview.setImageBitmap(image);

                    // converting the images to base64 string for easy transfer
                    if(pasteventimageview.getDrawable() != null)
                    {
                        pastimage=image;



                    }




                }




            }

        }
    }




}