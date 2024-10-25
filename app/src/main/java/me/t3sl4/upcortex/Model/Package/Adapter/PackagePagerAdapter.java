package me.t3sl4.upcortex.Model.Package.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.t3sl4.upcortex.Model.Package.PackageItem;
import me.t3sl4.upcortex.R;

public class PackagePagerAdapter extends RecyclerView.Adapter<PackagePagerAdapter.PackageViewHolder> {
    private List<PackageItem> packageList;

    public PackagePagerAdapter(List<PackageItem> packageList) {
        this.packageList = packageList;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_package_item, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        PackageItem currentPackage = packageList.get(position);
        holder.packageName.setText(currentPackage.getName());
        holder.packageDescription.setText(currentPackage.getDescription());
        holder.packageImage.setImageResource(currentPackage.getImageResource());
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {
        public ImageView packageImage;
        public TextView packageName;
        public TextView packageDescription;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            packageImage = itemView.findViewById(R.id.packageImage);
            packageName = itemView.findViewById(R.id.packageName);
            packageDescription = itemView.findViewById(R.id.packageDescription);
        }
    }
}