package me.t3sl4.upcortex.UI.Components.PaymentOptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zpj.widget.checkbox.ZCheckBox;

import me.t3sl4.upcortex.R;

public class PaymentOptions extends BottomSheetDialogFragment {

    private ZCheckBox oneMonthCheckBox;
    private ZCheckBox twoMonthCheckBox;
    private ZCheckBox fourMonthCheckBox;
    private ZCheckBox eightMonthCheckBox;
    private ZCheckBox annualCheckBox;
    private Button confirmButton;
    private PaymentOptionSelectedListener paymentListener;

    public interface PaymentOptionSelectedListener {
        void onPaymentOptionSelected(String option);
    }

    public PaymentOptions(PaymentOptionSelectedListener paymentListener) {
        this.paymentListener = paymentListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_payment_options, container, false);

        oneMonthCheckBox = view.findViewById(R.id.oneMonthCheckBox);
        twoMonthCheckBox = view.findViewById(R.id.twoMonthCheckBox);
        fourMonthCheckBox = view.findViewById(R.id.fourMonthCheckBox);
        eightMonthCheckBox = view.findViewById(R.id.eightMonthCheckBox);
        annualCheckBox = view.findViewById(R.id.annualCheckBox);
        confirmButton = view.findViewById(R.id.btn_done);

        oneMonthCheckBox.setChecked(true);

        oneMonthCheckBox.setOnClickListener(v -> uncheckAllExcept(oneMonthCheckBox));
        twoMonthCheckBox.setOnClickListener(v -> uncheckAllExcept(twoMonthCheckBox));
        fourMonthCheckBox.setOnClickListener(v -> uncheckAllExcept(fourMonthCheckBox));
        eightMonthCheckBox.setOnClickListener(v -> uncheckAllExcept(eightMonthCheckBox));
        annualCheckBox.setOnClickListener(v -> uncheckAllExcept(annualCheckBox));

        confirmButton.setOnClickListener(v -> {
            // Taksit Seçimi
            dismiss();
        });

        return view;
    }

    private void uncheckAllExcept(ZCheckBox checkBox) {
        oneMonthCheckBox.setChecked(checkBox == oneMonthCheckBox);
        twoMonthCheckBox.setChecked(checkBox == twoMonthCheckBox);
        fourMonthCheckBox.setChecked(checkBox == fourMonthCheckBox);
        eightMonthCheckBox.setChecked(checkBox == eightMonthCheckBox);
        annualCheckBox.setChecked(checkBox == annualCheckBox);
    }

    private String getSelectedOption() {
        if (oneMonthCheckBox.isChecked()) return "1 Month";
        if (twoMonthCheckBox.isChecked()) return "2 Months";
        if (fourMonthCheckBox.isChecked()) return "4 Months";
        if (eightMonthCheckBox.isChecked()) return "8 Months";
        if (annualCheckBox.isChecked()) return "Annual";
        return "";
    }
}