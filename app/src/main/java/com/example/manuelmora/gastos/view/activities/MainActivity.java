package com.example.manuelmora.gastos.view.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuelmora.gastos.R;
import com.example.manuelmora.gastos.model.Transaction;
import com.example.manuelmora.gastos.view.adapters.TransactionsAdapter;
import com.example.manuelmora.gastos.view.custom.TransactionDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * @author Juan Mora
 * @since 10/09/2018 16:01
 *
 * Main activity of the application. Show the transactions list,
 * allows save new transactions and display the total debt.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CARD_SCAN_CODE = 0x100;
    private boolean mAreElementsVisible;
    private TextView mTotalTextView;
    private FloatingActionButton mAddActionButton;
    private TransactionsAdapter mTransactionsAdapter;
    private List<Transaction> mTransactions;
    private EditText mTestEditText;
    private TransactionDialog transactionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adapter creation for recycler view
        mTransactionsAdapter = new TransactionsAdapter();
        // Flag to show or hide floating buttons
        mAreElementsVisible = false;
        mTestEditText = findViewById(R.id.act_main_testing);
        mAddActionButton = findViewById(R.id.act_main_add);
        mTotalTextView = findViewById(R.id.act_main_total_debt);

        RecyclerView transactionsRecyclerView = findViewById(R.id.act_main_detail);
        FloatingActionButton debtActionButton = findViewById(R.id.act_main_debt);
        FloatingActionButton paymentActionButton = findViewById(R.id.act_main_payment);
        TextView date = findViewById(R.id.act_main_date);


        transactionsRecyclerView.setAdapter(mTransactionsAdapter);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Listener for add floating button
        mAddActionButton.setOnClickListener(v -> {
            // Animator to change from plus icon to a cross icon
            Animation animator;
            // Evaluates if show or hide the add transactions buttons
            if (mAreElementsVisible) {
                // Animate and hide the floating buttons
                animator = AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_add_to_close);
                debtActionButton.setVisibility(View.GONE);
                paymentActionButton.setVisibility(View.GONE);
                mAddActionButton.setAnimation(animator);
            } else {
                // Animate and show the floating buttons
                animator = AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_close_to_add);
                debtActionButton.setVisibility(View.VISIBLE);
                paymentActionButton.setVisibility(View.VISIBLE);
                mAddActionButton.setAnimation(animator);
            }
            mAreElementsVisible = !mAreElementsVisible;
        });
        mTestEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                Toast.makeText(MainActivity.this, mTestEditText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mTestEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(MainActivity.this, mTestEditText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        debtActionButton.setOnClickListener(this);
        paymentActionButton.setOnClickListener(this);
        date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    String selectedDate = String.format(Locale.getDefault(), "%d/%d/%d", day, month + 1, year);
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    try {
                        selectedDate = timeFormatter.format(timeFormatter.parse(selectedDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } finally {
                        date.setText(selectedDate);
                    }
                }
            }, 1970, 1, 1);
            datePickerDialog.show();
        });
    }

    /**
     * Implementation of click listener for floating buttons
     *
     * */
    @Override
    public void onClick(View view) {
        // Creates a dialog instance to input data
        transactionDialog = new TransactionDialog(this);
        switch (view.getId()) {
            case R.id.act_main_debt:
                // Show dialog and create a callback to update the transactions list and calculate the total debt
                transactionDialog.createDialog(false, () -> {
                    mTransactions = mTransactionsAdapter.update();
                    calculateAndPrintTotal();
                });
                break;
            case R.id.act_main_payment:
                transactionDialog.createDialog(true, () -> {
                    mTransactions = mTransactionsAdapter.update();
                    calculateAndPrintTotal();
                });
                break;
        }
        // Calls onClick event of mAddActionButton
        mAddActionButton.performClick();

    }

    /**
     * Calculate the total debt with the transactions list
     *
     */

    private void calculateAndPrintTotal() {
        double total = 0.0;
        for (Transaction tx : mTransactions) {
            if (tx.type.equals(Transaction.DEBT_TYPE))
                total += tx.amount;
            else
                total -= tx.amount;
        }
        mTotalTextView.setText(String.format(Locale.getDefault(), "$%1$,.2f", total));
    }

    public void callScanActivity() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, CARD_SCAN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CARD_SCAN_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                if (transactionDialog != null) {
                    transactionDialog.printScannedCard(scanResult.getRedactedCardNumber());
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
        }
    }
}
