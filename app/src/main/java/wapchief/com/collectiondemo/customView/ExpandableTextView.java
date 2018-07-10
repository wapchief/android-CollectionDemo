package wapchief.com.collectiondemo.customView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wapchief.com.collectiondemo.R;


/**
 * @author wapchief
 * @date 2018/5/29
 */

public class ExpandableTextView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = ExpandableTextView.class.getSimpleName();
    private static final int MAX_COLLAPSED_LINES = 8;
    private static final int DEFAULT_ANIM_DURATION = 300;
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7F;
    protected TextView mTv;
    protected ImageButton mButton;
    private boolean mRelayout;
    private boolean mCollapsed;
    private int mCollapsedHeight;
    private int mTextHeightWithMaxLines;
    private int mMaxCollapsedLines;
    private int mMarginBetweenTxtAndBottom;
    private Drawable mExpandDrawable;
    private Drawable mCollapseDrawable;
    private int mAnimationDuration;
    private float mAnimAlphaStart;
    private boolean mAnimating;
    private ExpandableTextView.OnExpandStateChangeListener mListener;
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    public ExpandableTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCollapsed = true;
        this.init(attrs);
    }

    @TargetApi(11)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCollapsed = true;
        this.init(attrs);
    }

    public void setOrientation(int orientation) {
        if (0 == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        } else {
//            super.setOrientation(orientation);
        }
    }

    @Override
    public void onClick(View view) {
        if (this.mButton.getVisibility() == VISIBLE) {
            this.mCollapsed = !this.mCollapsed;
            this.mButton.setImageDrawable(this.mCollapsed ? this.mExpandDrawable : this.mCollapseDrawable);
            if (this.mCollapsedStatus != null) {
                this.mCollapsedStatus.put(this.mPosition, this.mCollapsed);
            }

            this.mAnimating = true;
            ExpandableTextView.ExpandCollapseAnimation animation;
            if (this.mCollapsed) {
                animation = new ExpandableTextView.ExpandCollapseAnimation(this, this.getHeight(), this.mCollapsedHeight);
            } else {
                animation = new ExpandableTextView.ExpandCollapseAnimation(this, this.getHeight(), this.getHeight() + this.mTextHeightWithMaxLines - this.mTv.getHeight());
            }

            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ExpandableTextView.applyAlphaAnimation(ExpandableTextView.this.mTv, ExpandableTextView.this.mAnimAlphaStart);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ExpandableTextView.this.clearAnimation();
                    ExpandableTextView.this.mAnimating = false;
                    if (ExpandableTextView.this.mListener != null) {
                        ExpandableTextView.this.mListener.onExpandStateChanged(ExpandableTextView.this.mTv, !ExpandableTextView.this.mCollapsed);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            this.clearAnimation();
            this.startAnimation(animation);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mAnimating;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        this.findViews();
    }

    @Override
    @SuppressLint("WrongConstant")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mRelayout && this.getVisibility() != GONE) {
            this.mRelayout = false;
            this.mButton.setVisibility(GONE);
            this.mTv.setMaxLines(Integer.MAX_VALUE);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (this.mTv.getLineCount() > this.mMaxCollapsedLines) {
                this.mTextHeightWithMaxLines = getRealTextViewHeight(this.mTv);
                if (this.mCollapsed) {
                    this.mTv.setMaxLines(this.mMaxCollapsedLines);
                }

                this.mButton.setVisibility(VISIBLE);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                if (this.mCollapsed) {
                    this.mTv.post(new Runnable() {
                        @Override
                        public void run() {
                            ExpandableTextView.this.mMarginBetweenTxtAndBottom = ExpandableTextView.this.getHeight() - ExpandableTextView.this.mTv.getHeight();
                        }
                    });
                    this.mCollapsedHeight = this.getMeasuredHeight();
                }

            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setOnExpandStateChangeListener(@Nullable ExpandableTextView.OnExpandStateChangeListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("WrongConstant")
    public void setText(@Nullable CharSequence text) {
        this.mRelayout = true;
        this.mTv.setText(text);
        this.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }

    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        this.mCollapsedStatus = collapsedStatus;
        this.mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        this.clearAnimation();
        this.mCollapsed = isCollapsed;
        this.mButton.setImageDrawable(this.mCollapsed ? this.mExpandDrawable : this.mCollapseDrawable);
        this.setText(text);
        this.getLayoutParams().height = -2;
        this.requestLayout();
    }

    @Nullable
    public CharSequence getText() {
        return (CharSequence) (this.mTv == null ? "" : this.mTv.getText());
    }

    @SuppressLint("WrongConstant")
    private void init(AttributeSet attrs) {
        TypedArray typedArray = this.getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, 8);
        this.mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, 300);
        this.mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, 0.7F);
        this.mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expandDrawable);
        this.mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapseDrawable);
        if (this.mExpandDrawable == null) {
            this.mExpandDrawable = getDrawable(this.getContext(), R.drawable.icon_live_home_arrow);
        }

        if (this.mCollapseDrawable == null) {
            this.mCollapseDrawable = getDrawable(this.getContext(), R.drawable.icon_live_home_below);
        }

        typedArray.recycle();
        this.setVisibility(GONE);
    }

    private void findViews() {
        this.mTv = (TextView) this.findViewById(R.id.expandable_text);
        this.mTv.setOnClickListener(this);
        this.mButton = (ImageButton) this.findViewById(R.id.expand_collapse);
        this.mButton.setImageDrawable(this.mCollapsed ? this.mExpandDrawable : this.mCollapseDrawable);
        this.mButton.setOnClickListener(this);
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    @TargetApi(11)
    private static void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            alphaAnimation.setDuration(0L);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }

    }

    @TargetApi(21)
    private static Drawable getDrawable(@NonNull Context context, int resId) {
        Resources resources = context.getResources();
        return isPostLolipop() ? resources.getDrawable(resId, context.getTheme()) : resources.getDrawable(resId);
    }

    private static int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    public interface OnExpandStateChangeListener {
        void onExpandStateChanged(TextView var1, boolean var2);
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            this.mTargetView = view;
            this.mStartHeight = startHeight;
            this.mEndHeight = endHeight;
            this.setDuration((long) ExpandableTextView.this.mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) ((float) (this.mEndHeight - this.mStartHeight) * interpolatedTime + (float) this.mStartHeight);
            ExpandableTextView.this.mTv.setMaxHeight(newHeight - ExpandableTextView.this.mMarginBetweenTxtAndBottom);
            if (Float.compare(ExpandableTextView.this.mAnimAlphaStart, 1.0F) != 0) {
                ExpandableTextView.applyAlphaAnimation(ExpandableTextView.this.mTv, ExpandableTextView.this.mAnimAlphaStart + interpolatedTime * (1.0F - ExpandableTextView.this.mAnimAlphaStart));
            }

            this.mTargetView.getLayoutParams().height = newHeight;
            this.mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
