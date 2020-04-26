package com.example.parrottalker.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

public class ActionButton extends AppCompatImageButton {
    public ActionButton(Context context) {
        super(context);
    }
    public ActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public ActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setListener(OnClickListener l) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onClick(v);
                setAlpha(getAlpha() == 0.5f ? 1f : 0.5f);
            }
        });
    }


}
