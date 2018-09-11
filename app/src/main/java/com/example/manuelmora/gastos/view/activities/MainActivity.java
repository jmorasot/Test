package com.example.manuelmora.gastos.view.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.manuelmora.gastos.R;
import com.example.manuelmora.gastos.view.custom.TransactionDialog;

/*
 * @author Juan Mora
 * @since 10/09/2018 16:01
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean mAreElementsVisible;
    private FloatingActionButton mAddActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAreElementsVisible = false;
        mAddActionButton = findViewById(R.id.act_main_add);
        FloatingActionButton debtActionButton = findViewById(R.id.act_main_debt);
        FloatingActionButton paymentActionButton = findViewById(R.id.act_main_payment);

        mAddActionButton.setOnClickListener(v -> {
            Animation animator;
            if (mAreElementsVisible) {
                animator = AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_add_to_close);
                debtActionButton.setVisibility(View.GONE);
                paymentActionButton.setVisibility(View.GONE);
                mAddActionButton.setAnimation(animator);
            } else {
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


    @Override
    public void onClick(View view) {
        TransactionDialog transactionDialog = new TransactionDialog(this);
        switch (view.getId()) {
            case R.id.act_main_debt:
                transactionDialog.createDialog(false);
                break;
            case R.id.act_main_payment:
                transactionDialog.createDialog(true);
                break;
        }
        mAddActionButton.performClick();
    }
}
