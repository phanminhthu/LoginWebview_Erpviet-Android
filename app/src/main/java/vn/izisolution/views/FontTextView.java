package vn.izisolution.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import vn.izisolution.R;


/**
 * Created by Developer on 9/5/2016.
 */
public class FontTextView extends AppCompatTextView {

    private String hint = "";

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setFontColor(int color) {
        setTextColor(color);
    }

    public void setFontSize(int sp) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    public void setFont(String font, String textStyle) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
        if (textStyle != null || !textStyle.equals("")) {
            if (textStyle.equals("bold"))
                setTypeface(tf, Typeface.BOLD);
            else if (textStyle.equals("italic"))
                setTypeface(tf, Typeface.ITALIC);
        } else
            setTypeface(tf);
    }

    public void setFont(Typeface tf, String textStyle) {
        if (textStyle != null || !textStyle.equals("")) {
            if (textStyle.equals("bold"))
                setTypeface(tf, Typeface.BOLD);
            else if (textStyle.equals("italic"))
                setTypeface(tf, Typeface.ITALIC);
            else
                setTypeface(tf, Typeface.NORMAL);
        } else
            setTypeface(tf, Typeface.NORMAL);
    }

    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        super.setHint(enable ? "" : hint);
        if (enable == false)
            setError(null);
    }

    public void setEnabled(boolean enable, boolean showHint) {
        super.setEnabled(enable);
        if (showHint)
            super.setHint(hint);
        else
            super.setHint("");
        if (enable == false)
            setError(null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);

            if (getHint() != null && !getHint().equals(""))
                hint = getHint().toString();

        }

    }

    public boolean invalidateInput(String regex, String error) {
        String value = getText().toString().trim();
        String required = "Bạn chưa nhập trường này";

        setError(null);
        if (value.length() <= 0) {
            if (error != null && !error.equals(""))
                setError(error);
            else
                setError(required);

            requestFocus();

            return false;
        }
        return true;
    }

}
