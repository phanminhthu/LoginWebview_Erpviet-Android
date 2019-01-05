package vn.izisolution.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import vn.izisolution.R;

/**
 * Created by ToanNMDev on 4/16/2017.
 */

public class ValidateEditText extends AppCompatEditText {

    private static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String hint = "";


    public ValidateEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ValidateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ValidateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setCursorVisible(boolean visible) {
        super.setCursorVisible(visible);

        super.setFocusable(visible);
        super.setFocusableInTouchMode(visible);
    }

    @Override
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

    // interface
    public interface OnTextChangeListener {
        public void onTextChanged(CharSequence s, int start, int before, int count);

        public void beforeTextChanged(CharSequence s, int start, int count, int after);

        public void afterTextChanged(Editable s);
    }

    public void addTextChangedListener() {
        addTextChangedListener(null, ".", 3, true);
    }

    public void addTextChangedListener(final String charactersToAdd) {
        addTextChangedListener(null, charactersToAdd, 3, true);
    }

    public void addTextChangedListener(final String charactersToAdd, final int addCharPosition) {
        addTextChangedListener(null, charactersToAdd, addCharPosition, true);
    }

    /**
     * @param charactersToAdd
     * @param addCharPosition
     * @param checkZero
     */
    public void addTextChangedListener(final OnTextChangeListener onTextChangeListener, final String charactersToAdd, final int addCharPosition, final boolean checkZero) {
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (onTextChangeListener != null)
                    onTextChangeListener.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (onTextChangeListener != null)
                    onTextChangeListener.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onTextChangeListener != null)
                    onTextChangeListener.afterTextChanged(s);
                try {
                    removeTextChangedListener(this);
                    String value = getText().toString();

                    if (value != null && !value.equals("")) {
                        if (checkZero) {
                            if (value.startsWith(charactersToAdd)) {
                                setText("0" + charactersToAdd);
                            }
                            if (value.startsWith("0") && !value.startsWith("0" + charactersToAdd)) {
                                setText("0");
                            }
                        }
                        String str = getText().toString().replace(charactersToAdd, "");
                        if (!value.equals(""))
                            setText(getDecimalFormattedString(str, addCharPosition, charactersToAdd));
                        setSelection(getText().toString().length());
                    }
                    addTextChangedListener(this);

                    return;
                } catch (Exception ex) {
                    addTextChangedListener(this);
                    ex.printStackTrace();
                }
            }
        });
    }

    private String getDecimalFormattedString(String value, int addCharPosition, String charactersToAdd) {
        String str1 = value;
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        for (int k = j; ; k--) {
            if (k < 0) {
                return str3;
            }
            if (i == addCharPosition) {
                str3 = charactersToAdd + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }

    public String trimCommaOfString(String charactersToAdd) {
        // String returnString;
        if (getText().toString().contains(charactersToAdd)) {
            return getText().toString().replace(charactersToAdd, "");
        } else {
            return getText().toString();
        }

    }


    public boolean invalidateInput(String error) {
        String value = getText().toString().trim();
        String required = "Bạn chưa nhập trường này";
        if (!error.equals(""))
            required = error;

        setError(null);
        if (value.length() <= 0) {
            setError(required);

            requestFocus();

            return false;
        }
        return true;
    }

}
