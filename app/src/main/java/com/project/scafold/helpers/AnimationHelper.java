package com.project.scafold.helpers;

import android.app.Activity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androidplayer.actions.property.PropertyAction;

/**
 * Created by jayan on 11/5/2016.
 */

public class AnimationHelper {

    public static PropertyAction fadeIn(View view, int duration) {
        PropertyAction anim = PropertyAction.newPropertyAction(view)
                .alpha(0)
                .duration(duration)
                .interpolator(new AccelerateDecelerateInterpolator())
                .build();
        return anim;
    }

    public static PropertyAction slideUp(Activity activity, View view, int duration){
        PropertyAction anim = PropertyAction.newPropertyAction(view)
                .translationY(ScreenHelper.getScreenHeight(activity))
                .duration(duration)
                .interpolator(new AccelerateDecelerateInterpolator())
                .build();
        return  anim;
    }

    public static PropertyAction slideDown(Activity activity, View view, int duration){
        PropertyAction anim = PropertyAction.newPropertyAction(view)
                .translationY(-ScreenHelper.getScreenHeight(activity))
                .duration(duration)
                .interpolator(new AccelerateDecelerateInterpolator())
                .build();
        return  anim;
    }

    public static PropertyAction slideLeft(Activity activity, View view, int duration){
        PropertyAction anim = PropertyAction.newPropertyAction(view)
                .translationX(-ScreenHelper.getScreenWidth(activity))
                .duration(duration)
                .interpolator(new AccelerateDecelerateInterpolator())
                .build();
        return  anim;
    }

    public static PropertyAction slideRight(Activity activity, View view, int duration){
        PropertyAction anim = PropertyAction.newPropertyAction(view)
                .translationX(ScreenHelper.getScreenWidth(activity))
                .duration(duration)
                .interpolator(new AccelerateDecelerateInterpolator())
                .build();
        return  anim;
    }

}
