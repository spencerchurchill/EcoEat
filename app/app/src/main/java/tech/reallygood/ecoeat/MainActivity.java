package tech.reallygood.ecoeat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1033;
    Uri image_uri;
    Dialog dialog = null;
    private ImageView graphImage;
    private TextView predText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mStorageRef = FirebaseStorage.getInstance().getReference();


        predText = findViewById(R.id.predText);

        RelativeLayout camera_btn = findViewById(R.id.camera_button);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else{
                        // permission already granted
                        openCamera();
                    }
                }
                else {
                    // system os < marshallmallow
                    openCamera();
                }
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ){

        switch(requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    System.out.println("Permission Error");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        Log.v("LOGS:", "PICTURE TAKEN SUCCESSFULLY");
        if (resultCode == RESULT_OK){
            // Create a reference
            final StorageReference imageRef = mStorageRef.child("images/" + System.currentTimeMillis() + ".jpg");

//            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] pic_data = baos.toByteArray();


//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            FileInputStream fis;
//            try {
//                fis = new FileInputStream(new File(image_uri.getPath()));
//                byte[] buf = new byte[1024];
//                int n;
//                while (-1 != (n = fis.read(buf)))
//                    baos.write(buf, 0, n);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            byte[] bbytes = baos.toByteArray();

            UploadTask uploadTask = imageRef.putFile(image_uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.v("LOGS:", "PICTURE UPLOAD FAIL");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.v("LOGS:", "PICTURE SUCCESSFULLY UPLOADED");
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            sendData("image", "url", uri.toString());

                        }
                    });
                }
            });
        }
    }
    private void sendData(String dir, String query, String args) {
        try {
            JSONObject data = ClientInstance.getData(dir, query, args);
            dialog.dismiss();
            Log.v("LOGS:", "GOT DATA FROM SERVER");
            Log.v("LOGS:", String.valueOf(data));
            showData(data);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.v("LOGS:", "FAIL TO GET DATA FROM SERVER");
        }
    }

    private void showData(JSONObject data) throws JSONException {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        JSONObject info = data.getJSONObject("data").getJSONObject("information");
        LinearLayout body = dialog.findViewById(R.id.body);
        body.setVisibility(View.VISIBLE);
        TextView temissions = dialog.findViewById(R.id.temissions);
        TextView thealth = dialog.findViewById(R.id.thealth);
        TextView trec = dialog.findViewById(R.id.trec);
        TextView twater = dialog.findViewById(R.id.twater);
        TextView ttrees = dialog.findViewById(R.id.ttrees);
        Button btn = dialog.findViewById(R.id.okbtn);

        String emission = new String(Character.toChars(0x1F4A8)) + " Greenhouse Emissions: " + String.format("%.2f", info.getDouble("emission")) + "kg";
        temissions.setText(emission);

        String water = new String (Character.toChars(0x1F6B0)) + " Water Used: " + String.format("%.2f", info.getDouble("water")) + "kg";
        twater.setText(water);

        String trees = new String (Character.toChars(0x1F333)) + " Tree Used: " + String.format("%.2f", info.getDouble("tree") * 100) + '%';
        ttrees.setText(trees);

        String health = new String(Character.toChars(0x1F49A)) + " Meal health score: " + info.getString("health");
        thealth.setText(health);

        String[] rec_list = info.getString("rec").toLowerCase().split(",");
        String rec = rec_list.length > 1 ? "We recommend " + rec_list[1] + " over " + rec_list[0] + '.' : " ";

        trec.setText(rec);

        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveData(String.format("%.2f", info.getDouble("emission")), String.format("%.2f", info.getDouble("water")));
    }
    private void saveData(String emission, String water) {
        DataStorage ds = new DataStorage(this);
        Set<String> eset = ds.getStoredData("emission");
        Set<String> wset = ds.getStoredData("water");
        eset.add(emission);
        wset.add(water);
        Log.v("LOGS:", eset + ", " + wset);
        ds.saveData("emission", eset);
        ds.saveData("water", wset);
        setGraph();
        setPred();
    }

    @Override
    protected void onStart() {
        setGraph();
        setPred();
        super.onStart();

    }
    private void setGraph() {
        DataStorage ds = new DataStorage(this);
        Set<String> eset = ds.getStoredData("emission");
        Set<String> wset = ds.getStoredData("water");
        if (!eset.isEmpty() && !wset.isEmpty() && eset.size() > 1 && wset.size() > 1) {
            CardView card = findViewById(R.id.graphcard);
            graphImage = findViewById(R.id.graphImage);
            card.setVisibility(View.VISIBLE);
            String co2 = TextUtils.join("-", eset);
            String water = TextUtils.join("-", wset);

            try {
                String graph = ClientInstance.getGraph(co2, water);

                byte[] decodedString = Base64.decode(graph, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                graphImage.setImageBitmap(decodedByte);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void setPred() {
        DataStorage ds = new DataStorage(this);
        Set<String> eset = ds.getStoredData("emission");

        if (!eset.isEmpty() && eset.size() > 1) {
            String co2 = TextUtils.join("-", eset);
            try {
                JSONObject pred = ClientInstance.getData("stats", "list", co2);
                String prestr = pred.getString("prediction");
                String pred_text = "You are projected to save " + prestr + " trees in 2019!";

                predText.setText(pred_text);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
