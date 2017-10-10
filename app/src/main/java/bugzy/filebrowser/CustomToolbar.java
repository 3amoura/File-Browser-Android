package bugzy.filebrowser;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Mostafa on 08/30/2017.
 */

public class CustomToolbar extends Toolbar {

    private TextView titleTextView;
    private TextView subtitleTextView;

    private boolean transparentBackground;

    public CustomToolbar(Context context) {
        super(context);
        setupToolBar(context);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(attrs);
        setupToolBar(context);
    }

    private void applyAttributes(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CustomToolbar,
                    0, 0);
            try {
                transparentBackground = a.getBoolean(R.styleable.CustomToolbar_transparent_background, false);
            } finally {
                a.recycle();
            }
        }
    }

    private void setupToolBar(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.6);
        LayoutParams params = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        titleTextView = makeTitleTextView();
        subtitleTextView = makeSubtitleTextView();
        linearLayout.addView(titleTextView);
        linearLayout.addView(subtitleTextView);
        subtitleTextView.setVisibility(GONE);
        addView(linearLayout);
        setBackgroundColor(transparentBackground);
    }

    public void setBackgroundColor(boolean transparentBackground) {
        this.transparentBackground = transparentBackground;
        if (transparentBackground) {
            super.setBackgroundColor(Color.TRANSPARENT);
        } else {
            super.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
    }

    private TextView makeTitleTextView() {
        AppCompatTextView textView = new AppCompatTextView(getContext());
        textView.setGravity(Gravity.CENTER);
//        textView.setTextAppearance(getContext(), android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Title);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        textView.setAllCaps(false);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    private TextView makeSubtitleTextView() {
        AppCompatTextView textView = new AppCompatTextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextAppearance(getContext(), android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Subtitle);
        textView.setTextColor(Color.DKGRAY);
        textView.setAllCaps(false);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    @Override
    public void setTitle(@StringRes int resId) {
        titleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }

    @Override
    public void setSubtitle(@StringRes int resId) {
        subtitleTextView.setText(resId);
        subtitleTextView.setVisibility(TextUtils.isEmpty(subtitleTextView.getText()) ? GONE : VISIBLE);
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        subtitleTextView.setText(subtitle);
        subtitleTextView.setVisibility(TextUtils.isEmpty(subtitleTextView.getText()) ? GONE : VISIBLE);
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        titleTextView.setTextColor(color);
    }

    @Override
    public void setSubtitleTextColor(@ColorInt int color) {
        subtitleTextView.setTextColor(color);
    }
}