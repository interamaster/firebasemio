package com.mio.jrdv.firebasemio;

/**
 * Created by joseramondelgado on 15/11/16.
 */

public class BounceInterpolator implements android.view.animation.Interpolator {
    double mAmplitude = 1;
    double mFrequency = 10;
    BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude; mFrequency = frequency;
    }
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) * Math.cos(mFrequency * time) + 1); }
}