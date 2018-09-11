package com.example.manuelmora.gastos;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    private boolean mAreElementsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAreElementsVisible = false;
        FloatingActionButton addActionButton = findViewById(R.id.act_main_add);
        FloatingActionButton debtActionButton = findViewById(R.id.act_main_debt);
        FloatingActionButton paymentActionButton = findViewById(R.id.act_main_payment);

        addActionButton.setOnClickListener(v -> {
            Animation animator;
            if (mAreElementsVisible) {
                animator = AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_add_to_close);
                debtActionButton.setVisibility(View.GONE);
                paymentActionButton.setVisibility(View.GONE);
                addActionButton.setAnimation(animator);
            } else {
                animator = AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_close_to_add);
                debtActionButton.setVisibility(View.VISIBLE);
                paymentActionButton.setVisibility(View.VISIBLE);
                addActionButton.setAnimation(animator);
            }
            mAreElementsVisible = !mAreElementsVisible;
        });

        debtActionButton.setOnClickListener(null);
        paymentActionButton.setOnClickListener(null);
    }
}
