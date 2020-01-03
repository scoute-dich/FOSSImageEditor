package de.aosd.fossimageeditor.utils;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class MsgUtil {

    public static void show(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static SpannableString textSpannable(String text) {
        SpannableString s;
        s = new SpannableString(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        return s;
    }

    public static void setBottomSheetBehavior (final BottomSheetDialog dialog, final View view, int beh) {
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(beh);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dialog.cancel();
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
}
