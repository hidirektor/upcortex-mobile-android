package me.t3sl4.upcortex.UI.Components.DatePicker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import me.t3sl4.upcortex.R;

public class DatePickerBottomSheet extends BottomSheetDialog {

    private DatePicker datePicker;
    private OnDateSelectedListener listener;

    public DatePickerBottomSheet(@NonNull Context context, OnDateSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_date_picker, null);
        setContentView(view);

        datePicker = view.findViewById(R.id.date_picker);
        Button btnDone = view.findViewById(R.id.btn_done);

        btnDone.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            listener.onDateSelected(day, month, year);
            dismiss();
        });
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int day, int month, int year);
    }
}