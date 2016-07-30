package me.shiyan.mvptest.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import me.shiyan.mvptest.R;

/**
 * Created by shiyan on 2016/7/30.
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Context context, int theme) {
        super(context, theme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
    }

    public void setLayoutContent(int layout){
        setContentView(layout);
    }
}
