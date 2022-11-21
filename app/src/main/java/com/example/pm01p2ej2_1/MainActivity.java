package com.example.pm01p2ej2_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int TOMA_VIDEO = 1;
    private VideoView vv;

    Button btnsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vv = findViewById(R.id.videoView);
        btnsave = findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureVideo(vv);
            }
        });
    }

    public void CaptureVideo(View v) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, TOMA_VIDEO);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOMA_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            vv.setVideoURI(videoUri);
            vv.start();

            try {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
                FileOutputStream archivo = openFileOutput(CreateNameMP4(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    archivo.write(buf, 0, len);
                }

                Toast.makeText(this, "Guardado Exitosamente", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(this, "Error al grabar video", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private String CreateNameMP4() {
        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";
        return nombre;
    }

}