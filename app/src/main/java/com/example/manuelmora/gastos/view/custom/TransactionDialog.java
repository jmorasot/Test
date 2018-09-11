package com.example.manuelmora.gastos.view.custom;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manuelmora.gastos.R;
import com.example.manuelmora.gastos.model.Transaction;

import io.realm.Realm;

/*
 * @author Juan Mora
 * @since 11/09/2018 13:18
 */

public class TransactionDialog {
    private AppCompatActivity mActivity;
    private Realm mRealm;

    public TransactionDialog(AppCompatActivity activity){
        mActivity = activity;
        mRealm = Realm.getDefaultInstance();
    }

    public void createDialog(boolean isPayment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        AlertDialog dialog;

        View dialogView = mActivity.getLayoutInflater().inflate(R.layout.dialog_transaction, null);

        EditText conceptEditText = dialogView.findViewById(R.id.dialog_transaction_concept);
        EditText amountEditText = dialogView.findViewById(R.id.dialog_transaction_amount);

        builder.setView(dialogView);
        builder.setTitle(isPayment ? "Agregar un pago" : "Agregar un gasto");
        builder.setPositiveButton(R.string.dialog_accept, null);
        builder.setNegativeButton(R.string.dialog_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( view -> {
            String concept = conceptEditText.getText().toString();
            String amount = amountEditText.getText().toString();
            if (!concept.trim().isEmpty() && !amount.trim().isEmpty()) {
                Transaction transaction = new Transaction(isPayment ? Transaction.PAYMENT_TYPE : Transaction.DEBT_TYPE,
                        concept, Double.parseDouble(amount));
                mRealm.copyToRealmOrUpdate(transaction);
                dialog.dismiss();
            } else {
                Toast.makeText(mActivity, "Datos incompletos", Toast.LENGTH_LONG).show();
            }
        });
    }
}
