package me.t3sl4.upcortex.UI.Components.DatePicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import me.t3sl4.upcortex.R;

public class ExpiryDatePicker extends BottomSheetDialogFragment {

    private DatePicker datePicker;
    private Button confirmButton;
    private ExpiryDateSelectedListener listener;

    public interface ExpiryDateSelectedListener {
        void onExpiryDateSelected(int month, int year);
    }

    public ExpiryDatePicker(ExpiryDateSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_credit_card_expiry_date_picker, container, false);

        datePicker = view.findViewById(R.id.date_picker);
        confirmButton = view.findViewById(R.id.btn_done);

        // Sadece ay ve yıl göstermek için DatePicker'ı yapılandırma
        hideDaySpinner(datePicker);

        confirmButton.setOnClickListener(v -> {
            int selectedMonth = datePicker.getMonth() + 1; // Month starts from 0
            int selectedYear = datePicker.getYear();
            listener.onExpiryDateSelected(selectedMonth, selectedYear);
            dismiss();
        });

        return view;
    }

    private void hideDaySpinner(DatePicker datePicker) {
        try {
            int daySpinnerId = getResources().getIdentifier("day", "id", "android");
            if (daySpinnerId != 0) {
                View daySpinner = datePicker.findViewById(daySpinnerId);
                if (daySpinner != null) {
                    daySpinner.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}