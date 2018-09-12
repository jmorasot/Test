package com.example.manuelmora.gastos.view.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.manuelmora.gastos.R;
import com.example.manuelmora.gastos.model.Transaction;
import com.example.manuelmora.gastos.view.adapters.TransactionsAdapter;
import com.example.manuelmora.gastos.view.custom.TransactionDialog;

import java.util.List;
import java.util.Locale;

/**
 * @author Juan Mora
 * @since 10/09/2018 16:01
 *
 * Main activity of the application. Show the transactions list,
 * allows save new transactions and display the total debt.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean mAreElementsVisible;
    private TextView mTotalTextView;
    private FloatingActionButton mAddActionButton;
    private TransactionsAdapter mTransactionsAdapter;
    private List<Transaction> mTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adapter creation for recycler view
        mTransactionsAdapter = new TransactionsAdapter();
        // Flag to show or hide floating buttons
        mAreElementsVisible = false;
        mAddActionButton = findViewById(R.id.act_main_add);
        mTotalTextView = findViewById(R.id.act_main_total_debt);

        RecyclerView transactionsRecyclerView = findViewById(R.id.act_main_detail);
        FloatingActionButton debtActionButton = findViewById(R.id.act_main_debt);
        FloatingActionButton paymentActionButton = findViewById(R.id.act_main_payment);

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

        debtActionButton.setOnClickListener(this);
        paymentActionButton.setOnClickListener(this);
    }

    /**
     * Implementation of click listener for floating buttons
     *
     * */
    @Override
    public void onClick(View view) {
        // Creates a dialog instance to input data
        TransactionDialog transactionDialog = new TransactionDialog(this);
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
}
