package com.example.revealhideview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private View contentView;                     // to reference the scrollview
    private View loadingView;                    // to reference the progressview
    private int shortAnimationDuration;
    private View floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentView  = findViewById(R.id.content);
        loadingView = findViewById(R.id.loading_spinner);
        floatingActionButton = findViewById(R.id.fab);

        // Initially hide the content view.
        contentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_longAnimTime);

        // call the crossFade method
        crossfade();

    }

    private void crossfade() {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);            // I think alpha value is 0f so we are directly make it visible so it further step we only need to change the alpha

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    public void startCardAnimationActivity(View view) {
        Intent intent = new Intent(this,CardFlipAnimationActivity.class);
        startActivity(intent);
    }

    // hiding the fab button
    public void hideFab(View view) {

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = floatingActionButton.getWidth() / 2;
            int cy = floatingActionButton.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(floatingActionButton, cx, cy, finalRadius, 0f);


            anim.addListener(new AnimatorListenerAdapter() {          // listener to invisible the view after animation completes
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();

        } else {
            // set the view to invisible without a circular reveal animation below Lollipop
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
    }

    // revealing the fab button
    public void revealFab(View view) {

        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = floatingActionButton.getWidth() / 2;
            int cy = floatingActionButton.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(floatingActionButton, cx, cy, 0f, finalRadius);

            // make the view visible and start the animation
            floatingActionButton.setVisibility(View.VISIBLE);
            anim.start();

        } else {
            // set the view to invisible without a circular reveal animation below Lollipop
            floatingActionButton.setVisibility(View.VISIBLE);
        }
    }

}
