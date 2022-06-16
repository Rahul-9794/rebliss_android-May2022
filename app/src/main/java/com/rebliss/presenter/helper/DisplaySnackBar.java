package com.rebliss.presenter.helper;

import android.content.Context;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;

public class DisplaySnackBar {
    Context context;
    String message;
    int type = 0;
    public DisplaySnackBar(Context context) {
        this.context = context;
    }

    public void DisplaySnackBar(String message, int type) {
        this.type = type;
        this.message = message;
        showSnackBar();
    }

    private void showSnackBar() {
        Type snacktype = null;
        if (type == 0) {
            snacktype = Type.SUCCESS;
        } else if (type == 1) {
            snacktype = Type.ERROR;
        } else if (type == 2) {
            snacktype = Type.UPDATE;
        } else if (type == 3) {
            snacktype = Type.WARNING;
        }
        Snackbar.with(context, null)
                .type(snacktype)
                .message(message)
                .duration(Duration.SHORT)
                .fillParent(true)
                .textAlign(Align.CENTER)
                .show();
    }
}
