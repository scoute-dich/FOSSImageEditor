package de.aosd.fossimageeditor;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.klinker.android.badged_imageview.BadgedImageView;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import de.aosd.fossimageeditor.utils.GPUImageFilterTools;
import de.aosd.fossimageeditor.utils.MsgUtil;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private String afterSaving;

    private Activity activity;
    private Uri uriSource;

    private ImageButton filterApply;
    private ImageButton filterCancel;
    private SeekBar seekBar;

    private GPUImageView mGPUImageView;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    private CropImageView cropImageView;
    private RelativeLayout cropBar;
    private RelativeLayout badged_iv_layout;
    private RelativeLayout textBar;
    private RelativeLayout appbar;

    private int applyFilterCount;
    private int textColor;
    private int badgedImageView_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .errorActivity(StartActivity.class) //default: null (default error activity)
                .apply();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        seekBar = findViewById(R.id.seekBar);
        assert seekBar != null;
        seekBar.setOnSeekBarChangeListener(MainActivity.this);
        seekBar.setEnabled(false);

        mGPUImageView = findViewById(R.id.gpuImage);
        mGPUImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);


        cropImageView = findViewById(R.id.cropImageView);
        cropBar = findViewById(R.id.cropBar);
        badged_iv_layout = findViewById(R.id.badged_iv_layout);
        textBar = findViewById(R.id.textBar);
        appbar = findViewById(R.id.appbar);

        filterApply = findViewById(R.id.filterApply);
        filterApply.setEnabled(false);
        filterApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });

        filterCancel = findViewById(R.id.filterCancel);
        filterCancel.setEnabled(false);
        filterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGPUImageView.setFilter(new GPUImageFilter());
                filterApply.setEnabled(false);
                filterCancel.setEnabled(false);
                seekBar.setEnabled(false);
            }
        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && type.startsWith("image/")) {
            uriSource = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            handleImage();
        } else if ("android.intent.action.EDIT".equals(action) && type != null && type.startsWith("image/")) {
            uriSource = intent.getData();
            handleImage();
        }

        final ImageButton button_edit = findViewById(R.id.button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPUImageFilterTools.showDialog(activity, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
                    @Override
                    public void onGpuImageFilterChosenListener(final GPUImageFilter filter) {
                        switchFilterTo(filter);
                        mGPUImageView.requestRender();
                    }
                });
            }
        });

        ImageButton button_crop = findViewById(R.id.button_crop);
        button_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appbar.setVisibility(View.INVISIBLE);
                cropBar.setVisibility(View.VISIBLE);
                cropImageView.setVisibility(View.VISIBLE);
                mGPUImageView.setVisibility(View.INVISIBLE);
                cropImageView.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                Button button_1_1 = findViewById(R.id.button_1_1);
                button_1_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.setAspectRatio(1,1);
                    }
                });
                Button button_4_3 = findViewById(R.id.button_4_3);
                button_4_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.setAspectRatio(4,3);
                    }
                });
                Button button_3_4 = findViewById(R.id.button_3_4);
                button_3_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.setAspectRatio(3,4);
                    }
                });
                Button button_16_9 = findViewById(R.id.button_16_9);
                button_16_9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.setAspectRatio(16,9);
                    }
                });
                Button button_9_16 = findViewById(R.id.button_9_16);
                button_9_16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.setAspectRatio(9,16);
                    }
                });
                Button button_custom = findViewById(R.id.button_custom);
                button_custom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final BottomSheetDialog bottomSheetDialog_custom = new BottomSheetDialog(activity);
                        View dialogView = View.inflate(activity, R.layout.dialog_crop_custom, null);
                        final EditText dialog_width = dialogView.findViewById(R.id.dialog_width);
                        final EditText dialog_height = dialogView.findViewById(R.id.dialog_high);

                        Button action_ok = dialogView.findViewById(R.id.action_ok);
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int width = Integer.parseInt(dialog_width.getText().toString());
                                int height = Integer.parseInt(dialog_height.getText().toString());

                                cropImageView.setAspectRatio(width,height);
                                bottomSheetDialog_custom.cancel();

                            }
                        });
                        Button action_cancel = dialogView.findViewById(R.id.action_cancel);
                        action_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog_custom.cancel();
                            }
                        });
                        bottomSheetDialog_custom.setContentView(dialogView);
                        bottomSheetDialog_custom.show();
                        MsgUtil.setBottomSheetBehavior(bottomSheetDialog_custom, dialogView, BottomSheetBehavior.STATE_EXPANDED);
                    }
                });
                ImageButton button_right = findViewById(R.id.button_right);
                button_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.rotateImage(90);
                    }
                });
                ImageButton button_left = findViewById(R.id.button_left);
                button_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.rotateImage(270);
                    }
                });
                ImageButton button_horizontal = findViewById(R.id.button_horizontal);
                button_horizontal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.flipImageHorizontally();
                    }
                });
                ImageButton button_vertical = findViewById(R.id.button_vertical);
                button_vertical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImageView.flipImageVertically();
                    }
                });

                ImageButton button_apply_not = findViewById(R.id.button_apply_not);
                button_apply_not.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appbar.setVisibility(View.VISIBLE);
                        cropBar.setVisibility(View.INVISIBLE);
                        cropImageView.setVisibility(View.INVISIBLE);
                        mGPUImageView.setVisibility(View.VISIBLE);
                    }
                });
                ImageButton button_apply = findViewById(R.id.button_apply);
                button_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bitmap cropped = cropImageView.getCroppedImage();
                        mGPUImageView.setImage(cropped);
                        appbar.setVisibility(View.VISIBLE);
                        cropBar.setVisibility(View.INVISIBLE);
                        cropImageView.setVisibility(View.INVISIBLE);
                        mGPUImageView.setVisibility(View.VISIBLE);
                        applyFilter();
                    }
                });
            }
        });

        ImageButton button_text = findViewById(R.id.button_text);
        button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                appbar.setVisibility(View.INVISIBLE);
                textBar.setVisibility(View.VISIBLE);
                badged_iv_layout.setVisibility(View.VISIBLE);
                mGPUImageView.setVisibility(View.INVISIBLE);
                textColor = getColor(R.color.colorAccent);

                final LinearLayout textPositionLayout = findViewById(R.id.textPositionLayout);
                final LinearLayout textToolsLayout = findViewById(R.id.textToolsLayout);


                final BadgedImageView badged_iv_bottom_end = findViewById(R.id.badged_iv_bottom_end);  //4
                final BadgedImageView badged_iv_bottom_start = findViewById(R.id.badged_iv_bottom_start);  //3
                final BadgedImageView badged_iv_top_start = findViewById(R.id.badged_iv_top_start);  //1
                final BadgedImageView badged_iv_top_end = findViewById(R.id.badged_iv_top_end);  //2

                badged_iv_bottom_end.setVisibility(View.VISIBLE);
                badged_iv_bottom_start.setVisibility(View.INVISIBLE);
                badged_iv_top_end.setVisibility(View.INVISIBLE);
                badged_iv_top_start.setVisibility(View.INVISIBLE);
                badgedImageView_int = 4;

                badged_iv_top_start.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                badged_iv_top_end.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                badged_iv_bottom_start.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                badged_iv_bottom_end.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());

                final EditText dialog_text = findViewById(R.id.dialog_text);
                dialog_text.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        switch(badgedImageView_int) {
                            case 1:
                                badged_iv_top_start.setBadge(s.toString(), textColor);
                                break;
                            case 2:
                                badged_iv_top_end.setBadge(s.toString(), textColor);
                                break;
                            case 3:
                                badged_iv_bottom_start.setBadge(s.toString(), textColor);
                                break;
                            case 4:
                                badged_iv_bottom_end.setBadge(s.toString(), textColor);
                                break;
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                ImageButton button_textApply_not = findViewById(R.id.button_textApply_not);
                button_textApply_not.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appbar.setVisibility(View.VISIBLE);
                        textBar.setVisibility(View.INVISIBLE);
                        badged_iv_layout.setVisibility(View.INVISIBLE);
                        mGPUImageView.setVisibility(View.VISIBLE);
                    }
                });
                ImageButton button_textApply = findViewById(R.id.button_textApply);
                button_textApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch(badgedImageView_int) {
                            case 1:
                                mGPUImageView.setImage(getBitmapFromView(badged_iv_top_start));
                                break;
                            case 2:
                                mGPUImageView.setImage(getBitmapFromView(badged_iv_top_end));
                                break;
                            case 3:
                                mGPUImageView.setImage(getBitmapFromView(badged_iv_bottom_start));
                                break;
                            case 4:
                                mGPUImageView.setImage(getBitmapFromView(badged_iv_bottom_end));
                                break;
                        }
                        appbar.setVisibility(View.VISIBLE);
                        textBar.setVisibility(View.INVISIBLE);
                        badged_iv_layout.setVisibility(View.INVISIBLE);
                        mGPUImageView.setVisibility(View.VISIBLE);
                        applyFilter();
                    }
                });

                ImageButton button_position = findViewById(R.id.button_position);
                button_position.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textToolsLayout.setVisibility(View.INVISIBLE);
                        textPositionLayout.setVisibility(View.VISIBLE);

                        ImageButton position_topLeft = findViewById(R.id.position_topLeft);
                        position_topLeft.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textToolsLayout.setVisibility(View.VISIBLE);
                                textPositionLayout.setVisibility(View.INVISIBLE);
                                badgedImageView_int = 1;
                                badged_iv_bottom_end.setVisibility(View.INVISIBLE);
                                badged_iv_bottom_start.setVisibility(View.INVISIBLE);
                                badged_iv_top_end.setVisibility(View.INVISIBLE);
                                badged_iv_top_start.setVisibility(View.VISIBLE);
                                badged_iv_top_start.setBadge(dialog_text.getText().toString(), textColor);
                            }
                        });
                        ImageButton position_topRight = findViewById(R.id.position_topRight);
                        position_topRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textToolsLayout.setVisibility(View.VISIBLE);
                                textPositionLayout.setVisibility(View.INVISIBLE);
                                badgedImageView_int = 2;
                                badged_iv_bottom_end.setVisibility(View.INVISIBLE);
                                badged_iv_bottom_start.setVisibility(View.INVISIBLE);
                                badged_iv_top_end.setVisibility(View.VISIBLE);
                                badged_iv_top_start.setVisibility(View.INVISIBLE);
                                badged_iv_top_end.setBadge(dialog_text.getText().toString(), textColor);
                            }
                        });
                        ImageButton position_bottomLeft = findViewById(R.id.position_bottomLeft);
                        position_bottomLeft.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textToolsLayout.setVisibility(View.VISIBLE);
                                textPositionLayout.setVisibility(View.INVISIBLE);
                                badgedImageView_int = 3;
                                badged_iv_bottom_end.setVisibility(View.INVISIBLE);
                                badged_iv_bottom_start.setVisibility(View.VISIBLE);
                                badged_iv_top_end.setVisibility(View.INVISIBLE);
                                badged_iv_top_start.setVisibility(View.INVISIBLE);
                                badged_iv_bottom_start.setBadge(dialog_text.getText().toString(), textColor);
                            }
                        });
                        ImageButton button_bottomRight = findViewById(R.id.button_bottomRight);
                        button_bottomRight.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textToolsLayout.setVisibility(View.VISIBLE);
                                textPositionLayout.setVisibility(View.INVISIBLE);
                                badgedImageView_int = 4;
                                badged_iv_bottom_end.setVisibility(View.VISIBLE);
                                badged_iv_bottom_start.setVisibility(View.INVISIBLE);
                                badged_iv_top_end.setVisibility(View.INVISIBLE);
                                badged_iv_top_start.setVisibility(View.INVISIBLE);
                                badged_iv_bottom_end.setBadge(dialog_text.getText().toString(), textColor);
                            }
                        });
                    }
                });

                ImageButton button_color = findViewById(R.id.button_color);
                button_color.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ColorPickerDialogBuilder
                                .with(activity)
                                .setTitle(getString(R.string.dialog_color))
                                .initialColor(textColor)
                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                .density(10)
                                .setOnColorSelectedListener(new OnColorSelectedListener() {
                                    @Override
                                    public void onColorSelected(int selectedColor) {
                                    }
                                })
                                .setPositiveButton(getString(R.string.dialog_ok), new ColorPickerClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                        textColor = selectedColor;
                                        switch(badgedImageView_int) {
                                            case 1:
                                                badged_iv_top_start.setBadge(dialog_text.getText().toString(), textColor);
                                                break;
                                            case 2:
                                                badged_iv_top_end.setBadge(dialog_text.getText().toString(), textColor);
                                                break;
                                            case 3:
                                                badged_iv_bottom_start.setBadge(dialog_text.getText().toString(), textColor);
                                                break;
                                            case 4:
                                                badged_iv_bottom_end.setBadge(dialog_text.getText().toString(), textColor);
                                                break;
                                        }
                                    }
                                })
                                .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .build()
                                .show();
                    }
                });
            }
        });

        ImageButton button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterSaving = "afterSaving_save";
                try {
                    saveBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                    afterSaving = "afterSaving_not";
                }
            }
        });

        ImageButton button_open = findViewById(R.id.button_open);
        button_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                afterSaving = "afterSaving_open";
                try {
                    saveBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                    afterSaving = "afterSaving_not";
                }
            }
        });
    }

    public static Bitmap getBitmapFromView(View v) {
        v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }

    private void saveBitmap() throws IOException {

        applyFilter();

        Bitmap bitmap = mGPUImageView.getGPUImage().getBitmapWithFilterApplied();

        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        String fileName = date + ".jpg";

        OutputStream fos;

        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "FOSS_ImageEditor");
        final Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).flush();
        fos.close();

        mGPUImageView.setFilter(new GPUImageFilter());
        mGPUImageView.setImage(bitmap);
        filterApply.setEnabled(false);
        filterCancel.setEnabled(false);
        seekBar.setEnabled(false);

        switch (afterSaving) {
            case "afterSaving_open":
                Intent openIntent = new Intent();
                openIntent.setAction(Intent.ACTION_VIEW);
                openIntent.setDataAndType(imageUri, "image/*");
                startActivity(Intent.createChooser(openIntent, null));
                break;
            case "afterSaving_not":
                MsgUtil.show(activity, getString(R.string.dialog_load_not));
                break;
            case "afterSaving_save":
                MsgUtil.show(activity, getString(R.string.dialog_save_ok));
                break;
        }
    }

    private void switchFilterTo(final GPUImageFilter filter) {
        mGPUImageView.setFilter(filter);
        mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);

        filterApply.setEnabled(true);
        filterCancel.setEnabled(true);

        if (mFilterAdjuster.canAdjust()) {
            seekBar.setEnabled(true);
            mGPUImageView.requestRender();
        } else {
            seekBar.setEnabled(false);
            mGPUImageView.requestRender();
        }
    }

    private void applyFilter() {
        if (applyFilterCount == 0) {
            try {
                Bitmap bitmap = mGPUImageView.getGPUImage().getBitmapWithFilterApplied();
                mGPUImageView.setImage(bitmap);
                mGPUImageView.setFilter(new GPUImageFilter());
                applyFilterCount = 1;
                applyFilter();
            } catch (Exception e) {
                e.printStackTrace();
                MsgUtil.show(activity, getString(R.string.dialog_save_not));
            }
        } else {
            Bitmap bitmap = mGPUImageView.getGPUImage().getBitmapWithFilterApplied();
            mGPUImageView.setImage(bitmap);
            mGPUImageView.setFilter(new GPUImageFilter());
        }
        filterApply.setEnabled(false);
        filterCancel.setEnabled(false);
        seekBar.setEnabled(false);
    }

    private void handleImage() {
        if (uriSource != null) {
            try {
                applyFilterCount = 0;
                mGPUImageView.setFilter(new GPUImageFilter());
                seekBar.setEnabled(false);
                mGPUImageView.setImage(uriSource);
            } catch (Exception e) {
                e.printStackTrace();
                MsgUtil.show(activity, getString(R.string.dialog_load_not));
            }
        } else {
            MsgUtil.show(activity, getString(R.string.dialog_load_not));
        }
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        if (mFilterAdjuster != null) {
            mFilterAdjuster.adjust(progress);
        }
        mGPUImageView.requestRender();
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }
}
