package com.example.fwmda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDonationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText foodItem, description, pickUpTime, pickUpdate, address;
    private ImageView foodImage;
    private CheckBox donateTerms;
    private Button donateButton;

    private Uri resultUri;

    private ProgressDialog loader;

    private DatabaseReference donationReference;

    List<String> donationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food Donation Details"); //setting action bar title name
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //to show back button

        foodItem = findViewById(R.id.foodItem);
        description = findViewById(R.id.description);
        pickUpTime = findViewById(R.id.pickUpTime);
        pickUpdate = findViewById(R.id.pickUpDate);
        address = findViewById(R.id.address);
        foodImage = findViewById(R.id.foodImage);
        donateTerms = findViewById(R.id.donationTerms);
        donateButton = findViewById(R.id.donateButton);

        loader = new ProgressDialog(this);

        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,1); //picking image
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize notification manager
                NotificationManager manager = (NotificationManager)
                        getApplicationContext().getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

                //initialize intent for yes button
                Intent intent1 = new Intent(AddDonationActivity.this
                        , NotificationsActivity.class);
                //put extra
                intent1.putExtra("Yes", true);
                //add flags
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                        |Intent.FLAG_ACTIVITY_NEW_TASK);
                //initialize pending intent
                PendingIntent pendingIntent1 = PendingIntent.getActivity(
                        AddDonationActivity.this, 0,intent1
                        ,PendingIntent.FLAG_ONE_SHOT
                );

                //initialize intent for no button
                Intent intent2 = new Intent(AddDonationActivity.this
                        , NotificationsActivity.class);
                //put extra
                intent2.putExtra("No", false);
                //add flags
                PendingIntent pendingIntent2 = PendingIntent.getActivity(
                        AddDonationActivity.this, 1,intent2
                        ,PendingIntent.FLAG_ONE_SHOT
                );

                //Get default ringtone uri
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //initialize notification builder
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        AddDonationActivity.this,getString(R.string.app_name)
                );
                //SET notification tittle
                builder.setContentTitle("Donation Request");
                //SET content text
                builder.setContentText("Add your donation request ?");
                //set Notification icon
                builder.setSmallIcon(R.drawable.ic_notifications);
                //set sound
                builder.setSound(uri);
                //set auto cancel
                builder.setAutoCancel(true);
                //set priority
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                //Perform action yes button
                builder.addAction(R.drawable.ic_launcher_foreground, "Yes",pendingIntent1);
                //perform action for no
                builder.addAction(R.drawable.ic_launcher_foreground, "No",pendingIntent2);
                //Notify Manager
                manager.notify(1,builder.build());


                final String food = foodItem.getText().toString().trim();
                final String foodDescription = description.getText().toString().trim();
                final String time = pickUpTime.getText().toString().trim();
                final String date = pickUpdate.getText().toString().trim();
                final String location = address.getText().toString().trim();


                if(TextUtils.isEmpty(food)){
                    foodItem.setError("This field is required");
                    return;
                }
                if(TextUtils.isEmpty(foodDescription)){
                    description.setError("This field is required");
                }
                if(TextUtils.isEmpty(time)){
                    pickUpTime.setError("Pick up time is required");
                }
                if(TextUtils.isEmpty(date)){
                    pickUpdate.setError("Pick up date is required");
                }
                if(TextUtils.isEmpty(location)){
                    address.setError("Pick up date is required");
                }
                else {

                    loader.setMessage("Sending your donation request....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                }

                donationReference = FirebaseDatabase.getInstance().getReference()
                        .child("Donations").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                HashMap donateInfo = new HashMap();
                donateInfo.put("foodItemName", food);
                donateInfo.put("description", foodDescription);
                donateInfo.put("pickUpTime", time);
                donateInfo.put("pickUpDate", date);
                donateInfo.put("pickUpLocation", location);

                donationReference.updateChildren(donateInfo).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddDonationActivity.this, "Donation added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddDonationActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });

                if (resultUri !=null){
                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                            .child("food images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                    byte[] data = byteArrayOutputStream.toByteArray();
                    UploadTask uploadTask = filePath.putBytes(data);
                    // gets image, compresses it and uploads it to firebase

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddDonationActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() !=null){
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        Map newImageMap = new HashMap();
                                        newImageMap.put("foodurl", imageUrl);

                                        donationReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(AddDonationActivity.this, "Food image url successfully", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(AddDonationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                                        finish();
                                    }
                                });
                            }
                        }
                    });

                    Intent intent = new Intent(AddDonationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    loader.dismiss();

                }
            }

        });




    }
    

    public void itemClicked(View v) {
        if(donateTerms.isChecked()){
            Toast.makeText(AddDonationActivity.this, "Terms and Conditions Agreed",Toast.LENGTH_SHORT).show();
            return;
            //code to check if this checkbox is checked!
        }
        else {
            donateTerms.setError("You Agree to Terms and Conditions");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();// move user from AddDonationActivity to MainActivity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data !=null){
            resultUri = data.getData();
            foodImage.setImageURI(resultUri); //display image picked from gallery
        }
    }
}