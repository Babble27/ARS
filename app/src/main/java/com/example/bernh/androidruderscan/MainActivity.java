package com.example.bernh.androidruderscan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private Button button;
    private List<Data> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    static final int CAM_REQUEST = 1;
    static final int CAMERA_RESULT = 2;
    Bitmap image;
    String bitmapPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.ButtonCamera);
        recyclerView = (RecyclerView) findViewById(R.id.list_view);

        adapter = new MyAdapter(dataList,MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camerbtnclicked();


            }
        });
    }

    private void camerbtnclicked() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAM_REQUEST);
        } else {
            onTakePhotoClicked();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_RESULT: {
                //bitmapPath = saveToInternalStorage((Bitmap) data.getExtras().get("data"));
                //Bundle extra = data.getExtras();
                //image = (Bitmap) extra.get("data");
                //imageView.setImageBitmap(bitmap) ;
                if (data != null)
                {
                    if (data.hasExtra("data"))
                    {
                        image = data.getParcelableExtra("data");
                        showString(tesseractOCR(image));
                        //showString("hello");
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAM_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onTakePhotoClicked();
                } else {
                    Toast.makeText(this, "unable to use camera without permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onTakePhotoClicked() {

        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_RESULT);
        //showString(tesseractOCR(image));


    }

    public String tesseractOCR(Bitmap image) {
        TessBaseAPI baseAPI = new TessBaseAPI();
        //image = loadImageFromStorage(bitmapPath);
        baseAPI.setImage(image);
        String result = baseAPI.getUTF8Text();
        return result;
    }

        public void showString (final String result){
            AlertDialog.Builder altdia = new AlertDialog.Builder(MainActivity.this);
            altdia.setMessage(result).setCancelable(false).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    camerbtnclicked();
                }
            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setTable(result);
                }
            });
            AlertDialog alert = altdia.create();
            alert.setTitle("Is that correct ?");
            alert.show();
        }

        public void setTable(String text){
            Data data = new Data(text,"blabla","");

            adapter.addItem(data);
            //myDataset.add(text);
            // next thing you have to do is check if your adapter has changed
            adapter.notifyDataSetChanged();

            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

            recyclerView.setAdapter(adapter);
                    //table.addTab(table.newTab().setText(text));
                    //mPagerAdapter.addTabPage(text);
        }
   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }*/

    /*private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"data.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private Bitmap loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }*/



        /*public void setTableSize(){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            table.setMinimumHeight(height/2);
            table.setMinimumWidth(width);
        }*/
}


