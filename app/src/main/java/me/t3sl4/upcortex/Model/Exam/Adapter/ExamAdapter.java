package me.t3sl4.upcortex.Model.Exam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.t3sl4.upcortex.Model.Exam.Exam;
import me.t3sl4.upcortex.R;
import me.t3sl4.upcortex.UI.Screens.Exam.ExamProcess;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private Context context;
    private List<Exam> examList;

    public ExamAdapter(Context context, List<Exam> examsList) {
        this.context = context;
        this.examList = examsList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exams_list_recycler_item, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam examItem = examList.get(position);

        int defaultColor;
        Drawable defaultIcon;
        String isNecessaryString;

        if(examItem.isDefault()) {
            //Zorunlu Sınav
            defaultColor = ContextCompat.getColor(context, R.color.ratingColor);
            defaultIcon = ContextCompat.getDrawable(context, R.drawable.ikon_star);
            isNecessaryString = ContextCompat.getString(context, R.string.exam_must_be);
        } else {
            //İsteğe Bağlı
            defaultColor = ContextCompat.getColor(context, R.color.darkBaseColor);
            defaultIcon = ContextCompat.getDrawable(context, R.drawable.ikon_carrot);
            isNecessaryString = ContextCompat.getString(context, R.string.exam_optional);
        }

        if (defaultIcon != null) {
            defaultIcon.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);
        }

        holder.examIcon.setImageDrawable(defaultIcon);
        holder.isExamNecessary.setText(isNecessaryString);
        holder.isExamNecessary.setTextColor(defaultColor);
        holder.examTitle.setText(examItem.getExamName());
        holder.examDescription.setText(examItem.getExamDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExamProcess.class);
            intent.putExtra("exam", examItem);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {

        ImageView examIcon;
        TextView isExamNecessary, examTitle, examDescription;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examIcon = itemView.findViewById(R.id.examIcon);
            isExamNecessary = itemView.findViewById(R.id.isExamNecessary);
            examTitle = itemView.findViewById(R.id.examTitle);
            examDescription = itemView.findViewById(R.id.examDescription);
        }
    }
}