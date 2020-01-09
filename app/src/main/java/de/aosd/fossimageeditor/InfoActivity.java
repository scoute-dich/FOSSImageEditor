package de.aosd.fossimageeditor;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.aosd.fossimageeditor.utils.MsgUtil;


public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_info);
        TextView about_help_dialog = findViewById(R.id.about_help_dialog);
        about_help_dialog.setText(MsgUtil.textSpannable(getString(R.string.about_help_dialog)));
        about_help_dialog.setMovementMethod(LinkMovementMethod.getInstance());
        TextView about_cont_dialog = findViewById(R.id.about_cont_dialog);
        about_cont_dialog.setText(MsgUtil.textSpannable(getString(R.string.about_cont_dialog)));
        about_cont_dialog.setMovementMethod(LinkMovementMethod.getInstance());
        TextView about_license_dialog = findViewById(R.id.about_license_dialog);
        about_license_dialog.setText(MsgUtil.textSpannable(getString(R.string.about_license_dialog)));
        about_license_dialog.setMovementMethod(LinkMovementMethod.getInstance());

        super.onCreate(savedInstanceState);
    }
}
