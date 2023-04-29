package com.SmartTech.teasyNew.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.SmartTech.teasyNew.R;

/**
 * Created by muddvayne on 03/04/2017.
 */

public class PinView extends LinearLayout {

    private AnimatedView[] dots;
    private AnimatorPlayer animator;

    public PinView(Context context) {
        super(context);
        init();
    }

    public PinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        removeAllViews();

        dots = new AnimatedView[4];
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.pin_dot_size);
        int progressWidth = getContext().getResources().getDimensionPixelSize(R.dimen.pin_layout_width);
        for (int i = 0; i < dots.length; i++) {
            AnimatedView v = new AnimatedView(getContext());
            switch (i) {
                case 0:
                case 3:
                    v.setBackgroundDrawable( getResources().getDrawable(R.drawable.pin_dot_blue) );
                    break;
                case 1:
                case 4:
                    v.setBackgroundDrawable( getResources().getDrawable(R.drawable.pin_dot_red) );
                    break;
                case 2:
                case 5:
                    v.setBackgroundDrawable( getResources().getDrawable(R.drawable.pin_dot_green) );
                    break;
            }
            v.setTarget(progressWidth);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
            lp.setMargins(5, 0, 5, 0);
            v.setLayoutParams(lp);

            addView(v, lp);
            dots[i] = v;
        }
    }

    public void startAnim() {
        for(AnimatedView view : dots) {
            view.setXFactor(-1f);
        }
        animator = new AnimatorPlayer(createAnimations());
        animator.play();
    }

    public void stopAnim() {
        for(AnimatedView view : dots) {
            view.setXFactor(1f);
        }
        animator.stop();
    }

    public void setVisibleDots(int count) {
        for(int i = 0; i < dots.length; ++i) {
            if(i < count) {
                dots[i].setVisibility(VISIBLE);
            }
            else {
                dots[i].setVisibility(GONE);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator != null) {
            animator.stop();
        }
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[4];
        for (int i = 0; i < dots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(dots[i], "xFactor", 0, 1);
            move.setDuration(1500);
            move.setInterpolator(new HesitateInterpolator());
            move.setStartDelay(150 * i);
            animators[i] = move;
        }
        return animators;
    }

    private class AnimatedView extends androidx.appcompat.widget.AppCompatImageView {
        private int target;

        public AnimatedView(Context context) {
            super(context);
        }

        public float getXFactor() {
            return getX() / target;
        }

        public void setXFactor(float xFactor) {
            setX(target * xFactor);
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public int getTarget() {
            return target;
        }
    }

    private class AnimatorPlayer extends AnimatorListenerAdapter {

        private boolean interrupted = false;
        private Animator[] animators;

        public AnimatorPlayer(Animator[] animators) {
            this.animators = animators;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!interrupted) {
                animate();
            }
            else {
                init();
            }
        }

        public void play() {
            animate();
        }

        public void stop() {
            interrupted = true;
        }

        private void animate() {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animators);
            set.addListener(this);
            set.start();
        }
    }

    private class HesitateInterpolator implements Interpolator {

        private double POW = 1.0/2.0;

        @Override
        public float getInterpolation(float input) {
            return input < 0.5
                    ? (float) Math.pow(input * 2, POW) * 0.5f
                    : (float) Math.pow((1 - input) * 2, POW) * -0.5f + 1;
        }
    }
}
