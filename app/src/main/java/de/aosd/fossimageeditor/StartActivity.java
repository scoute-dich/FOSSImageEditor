package de.aosd.fossimageeditor;


import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.aosd.fossimageeditor.utils.MsgUtil;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.dialog_about);
        Button button_launcherIcon = findViewById(R.id.button_launcherIcon);
        button_launcherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager p = getPackageManager();
                ComponentName componentName = new ComponentName(StartActivity.this, StartActivity.class);
                if (p.getComponentEnabledSetting(componentName) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                    p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                } else {
                    p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                }
            }
        });

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
