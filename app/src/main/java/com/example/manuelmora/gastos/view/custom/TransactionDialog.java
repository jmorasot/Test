package com.example.manuelmora.gastos.view.custom;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manuelmora.gastos.R;
import com.example.manuelmora.gastos.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * @author Juan Mora
 * @since 11/09/2018 13:18
 *
 * Class that creates a custom dialog to input transactions data.
 */

public class TransactionDialog {
    private AppCompatActivity mActivity;
    private Realm mRealm;

    public TransactionDialog(AppCompatActivity activity){
        mActivity = activity;
        mRealm = Realm.getDefaultInstance();
    }

    /** Create a new dialog instance to input the transaction's data
     *
     * @param isPayment Flag to show concepts list or EditText
     * @param callback  Callback to know the dialog's result
     */

    public void createDialog(boolean isPayment, Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        AlertDialog dialog;

        // Create a view that contains the dialog layout
        View dialogView = mActivity.getLayoutInflater().inflate(R.layout.dialog_transaction, null);

        EditText conceptEditText = dialogView.findViewById(R.id.dialog_transaction_concept);
        EditText amountEditText = dialogView.findViewById(R.id.dialog_transaction_amount);

        // Evaluates if the transaction's type is a payment to show the correct views and concepts list
        if (isPayment) {
            List<String> concepts = new ArrayList<>();
            // Recover the concepts from the transactions list
            for (Transaction tx : mRealm.copyFromRealm(mRealm.where(Transaction.class).findAll())) {
                concepts.add(tx.concept);
            }

            // Create a instance of Spinner to show the concepts and pick one in payment transaction
            Spinner conceptList = dialogView.findViewById(R.id.dialog_transaction_concept_list);
            conceptEditText.setVisibility(View.GONE);
            conceptList.setVisibility(View.VISIBLE);
            // Set list's adapter to show items
            conceptList.setAdapter(new ArrayAdapter<>(mActivity.getApplicationContext(), R.layout.spinner_item, concepts));
            conceptList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // When item is selected, the text is saved to the concept EditText
                    conceptEditText.setText("");
                    conceptEditText.setText(conceptList.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        builder.setView(dialogView);
        builder.setTitle(isPayment ? "Abonar a deuda" : "Agregar un gasto");
        // Set the click listener to null because the dialog is closing and ignoring the validations
        builder.setPositiveButton(R.string.dialog_accept, null);
        builder.setNegativeButton(R.string.dialog_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setCancelable(false);

        // Create the dialog and show it
        dialog = builder.create();
        dialog.show();
        // Set the click listener to positive button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener( view -> {
            // Gets the EditText data
            String concept = conceptEditText.getText().toString();
            String amount = amountEditText.getText().toString();

            // Validates if concept and amount are filled
            if (!concept.trim().isEmpty() && !amount.trim().isEmpty()) {
                // Create the transaction instance
                Transaction transaction = new Transaction(isPayment ? Transaction.PAYMENT_TYPE : Transaction.DEBT_TYPE,
                        concept, Double.parseDouble(amount));
                // Save the transaction in Realm database
                mRealm.executeTransaction(tx -> mRealm.copyToRealmOrUpdate(transaction));
                callback.onTransactionAdded();
                mRealm.close();
                dialog.dismiss();
            } else {
                Toast.makeText(mActivity, "Datos incompletos", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface Callback {
        void onTransactionAdded();
    }
}
