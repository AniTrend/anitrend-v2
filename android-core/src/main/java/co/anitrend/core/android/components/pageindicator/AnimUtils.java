/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright (C) 2019  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.core.android.components.pageindicator;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Build;
import android.util.ArrayMap;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Property;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;

public class AnimUtils {

    private static Interpolator fastOutSlowIn;
    private static Interpolator fastOutLinearIn;
    private static Interpolator linearOutSlowIn;
    private static Interpolator linear;

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in);
        }
        return fastOutSlowIn;
    }

    public static Interpolator getFastOutLinearInInterpolator(Context context) {
        if (fastOutLinearIn == null) {
            fastOutLinearIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_linear_in);
        }
        return fastOutLinearIn;
    }

    public static Interpolator getLinearOutSlowInInterpolator(Context context) {
        if (linearOutSlowIn == null) {
            linearOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.linear_out_slow_in);
        }
        return linearOutSlowIn;
    }

    public static Interpolator getLinearInterpolator() {
        if (linear == null) {
            linear = new LinearInterpolator();
        }
        return linear;
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * A delegate for creating a {@link Property} of <code>int</code> type.
     */
    public static abstract class IntProp<T> {

        public final String name;

        public IntProp(String name) {
            this.name = name;
        }

        public abstract void set(T object, int value);
        public abstract int get(T object);
    }

    /**
     * The animation framework has an optimization for <code>Properties</code> of type
     * <code>int</code> but it was only made public in API24, so wrap the impl in our own type
     * and conditionally create the appropriate type, delegating the implementation.
     */
    public static <T> Property<T, Integer> createIntProperty(final IntProp<T> impl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new IntProperty<T>(impl.name) {
                @Override
                public Integer get(T object) {
                    return impl.get(object);
                }

                @Override
                public void setValue(T object, int value) {
                    impl.set(object, value);
                }
            };
        } else {
            return new Property<T, Integer>(Integer.class, impl.name) {
                @Override
                public Integer get(T object) {
                    return impl.get(object);
                }

                @Override
                public void set(T object, Integer value) {
                    impl.set(object, value);
                }
            };
        }
    }

    /**
     * A delegate for creating a {@link Property} of <code>float</code> type.
     */
    public static abstract class FloatProp<T> {

        public final String name;

        protected FloatProp(String name) {
            this.name = name;
        }

        public abstract void set(T object, float value);
        public abstract float get(T object);
    }

    /**
     * The animation framework has an optimization for <code>Properties</code> of type
     * <code>float</code> but it was only made public in API24, so wrap the impl in our own type
     * and conditionally create the appropriate type, delegating the implementation.
     */
    public static <T> Property<T, Float> createFloatProperty(final FloatProp<T> impl) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new FloatProperty<T>(impl.name) {
                @Override
                public Float get(T object) {
                    return impl.get(object);
                }

                @Override
                public void setValue(T object, float value) {
                    impl.set(object, value);
                }
            };
        } else {
            return new Property<T, Float>(Float.class, impl.name) {
                @Override
                public Float get(T object) {
                    return impl.get(object);
                }

                @Override
                public void set(T object, Float value) {
                    impl.set(object, value);
                }
            };
        }
    }

    /**
     * https://halfthought.wordpress.com/2014/11/07/reveal-transition/
     * <p/>
     * Interrupting Activity transitions can yield an OperationNotSupportedException when the
     * transition tries to pause the animator. Yikes! We can fix this by wrapping the Animator:
     */
    public static class NoPauseAnimator extends Animator {
        private final Animator mAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListener> mListeners = new ArrayMap<>();

        public NoPauseAnimator(Animator animator) {
            mAnimator = animator;
        }

        @Override
        public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!mListeners.containsKey(listener)) {
                mListeners.put(listener, wrapper);
                mAnimator.addListener(wrapper);
            }
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }

        @Override
        public void end() {
            mAnimator.end();
        }

        @Override
        public long getDuration() {
            return mAnimator.getDuration();
        }

        @Override
        public TimeInterpolator getInterpolator() {
            return mAnimator.getInterpolator();
        }

        @Override
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            mAnimator.setInterpolator(timeInterpolator);
        }

        @Override
        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<>(mListeners.keySet());
        }

        @Override
        public long getStartDelay() {
            return mAnimator.getStartDelay();
        }

        @Override
        public void setStartDelay(long delayMS) {
            mAnimator.setStartDelay(delayMS);
        }

        @Override
        public boolean isPaused() {
            return mAnimator.isPaused();
        }

        @Override
        public boolean isRunning() {
            return mAnimator.isRunning();
        }

        @Override
        public boolean isStarted() {
            return mAnimator.isStarted();
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();

        public void resume();

        public void addPauseListener(AnimatorPauseListener listener);

        public void removePauseListener(AnimatorPauseListener listener);
        */

        @Override
        public void removeAllListeners() {
            mListeners.clear();
            mAnimator.removeAllListeners();
        }

        @Override
        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = mListeners.get(listener);
            if (wrapper != null) {
                mListeners.remove(listener);
                mAnimator.removeListener(wrapper);
            }
        }

        @Override
        public Animator setDuration(long durationMS) {
            mAnimator.setDuration(durationMS);
            return this;
        }

        @Override
        public void setTarget(Object target) {
            mAnimator.setTarget(target);
        }

        @Override
        public void setupEndValues() {
            mAnimator.setupEndValues();
        }

        @Override
        public void setupStartValues() {
            mAnimator.setupStartValues();
        }

        @Override
        public void start() {
            mAnimator.start();
        }
    }

    private static class AnimatorListenerWrapper implements Animator.AnimatorListener {
        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }

}
