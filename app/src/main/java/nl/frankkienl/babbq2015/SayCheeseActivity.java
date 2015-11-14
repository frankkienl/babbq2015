package nl.frankkienl.babbq2015;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.service.wallpaper.WallpaperService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by FrankkieNL on 11/14/2015.
 */
public class SayCheeseActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    File pictureFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saycheese);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
    }

    public void initUI() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    public void takePicture() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCamera != PackageManager.PERMISSION_GRANTED || permissionExternalStorage != PackageManager.PERMISSION_GRANTED) {
            //Show explanation?
            boolean shouldShowCamera = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
            if (shouldShowCamera) {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
            }
            boolean shouldShowExternal = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (shouldShowExternal) {
                Toast.makeText(this, "External Storage permission is required to read and write the image file.", Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            pictureFile = new File(pictureDir, "BABBQ2015_SAYCHEESE.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showPicture();
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(this.findViewById(android.R.id.content), "Picture canceled.", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(this.findViewById(android.R.id.content), "Couldn't take a picture.", Snackbar.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
                    takePicture();
                } else {
                    Toast.makeText(this, "Permissions NOT granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void showPicture() {
        ImageView img = (ImageView) findViewById(R.id.saycheese_img);
        Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getPath());
        img.setImageBitmap(bitmap);
        img.setVisibility(View.VISIBLE);
        Button button = (Button) findViewById(R.id.saycheese_btn);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAsWallpaper();
            }
        });
    }

    public void setAsWallpaper() {
        Bitmap bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.getApplicationContext());
        if (Build.VERSION.SDK_INT >= 23)
            if (!wallpaperManager.isWallpaperSupported()) {
                Snackbar.make(this.findViewById(android.R.id.content), "Setting the Wallpaper is not supported!", Snackbar.LENGTH_LONG).show();
                return;
            }
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException ex) {
            Snackbar.make(this.findViewById(android.R.id.content), "Setting the Wallpaper did not work!", Snackbar.LENGTH_LONG).show();
        }
        Snackbar.make(this.findViewById(android.R.id.content), "Wallpaper has been set!!", Snackbar.LENGTH_LONG).show();
        findViewById(R.id.saycheese_btn).setEnabled(false); //Don't allow to set again.
    }
}
