package me.t3sl4.upcortex.Model.ExamResult;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Components.RadialPercent.RadialPercent;

public class Adapter extends RecyclerView.Adapter<Adapter.ExamViewHolder> {

    private final List<String> categoryList;
    private final List<String> subNameList;
    private final List<String> subDescList;
    private final List<Integer> percentList;

    public Adapter(List<String> categoryList, List<String> subNameList, List<String> subDescList, List<Integer> percentList) {
        this.categoryList = categoryList;
        this.subNameList = subNameList;
        this.subDescList = subDescList;
        this.percentList = percentList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_result_list_item, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        holder.examCategory.setText(categoryList.get(position));
        holder.categoryResultMain.setText(subNameList.get(position));
        holder.categoryResultDesc.setText(subDescList.get(position));
        holder.categoryPercent.setCurrentPercent(percentList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size(); // Kategori sayısına göre item sayısı belirlenir
    }

    static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView examCategory;
        TextView categoryResultMain;
        TextView categoryResultDesc;
        RadialPercent categoryPercent;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examCategory = itemView.findViewById(R.id.examCategory);
            categoryResultMain = itemView.findViewById(R.id.categoryResultMain);
            categoryResultDesc = itemView.findViewById(R.id.categoryResultDesc);
            categoryPercent = itemView.findViewById(R.id.radialPercent);
        }
    }
}