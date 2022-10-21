package com.example.fwmda.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fwmda.Model.Donations;
import com.example.fwmda.R;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {
    private Context context;
    private List<Donations> donationList;
    public DonationAdapter(Context context, List<Donations> donationList) {
        this.context = context;
        this.donationList = donationList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.new_donations_displayed,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Donations donations = donationList.get(position);

        holder.foodItemName.setText(donations.getFoodItemName());
        holder.foodItemDescription.setText(donations.getDescription());
        holder.foodItemTime.setText(donations.getPickUpTime());
        holder.foodItemDate.setText(donations.getPickUpDate());
        holder.foodItemNameLocation.setText(donations.getPickUpLocation());

        Glide.with(context).load(donations.getFoodurl()).into(holder.donationFoodImage);

        holder.approveDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("APPROVE DONATION")
                        .setMessage("Approve current donation?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


            }
        });



    }

    @Override
    public int getItemCount() {

        return donationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

       public TextView foodItemName, foodItemDescription, foodItemTime, foodItemDate, foodItemNameLocation;
       public ImageView donationFoodImage;
       public Button approveDonation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItemName = itemView.findViewById(R.id.foodItemName);
            foodItemDescription = itemView.findViewById(R.id.foodItemDescription);
            foodItemTime = itemView.findViewById(R.id.foodItemTime);
            foodItemDate = itemView.findViewById(R.id.foodItemNameDate);
            foodItemNameLocation = itemView.findViewById(R.id.foodItemNameLocation);
            donationFoodImage = itemView.findViewById(R.id.donationFoodImage);
            approveDonation = itemView.findViewById(R.id.approveDonation);

        }
    }
}
