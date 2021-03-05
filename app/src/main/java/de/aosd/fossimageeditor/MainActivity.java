package de.aosd.fossimageeditor;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Resources;
import android.content.res.TypedArray;
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
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private Uri imageUri;
    private ImageButton button_open;
    private ImageButton button_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .errorActivity(InfoActivity.class) //default: null (default error activity)
                .apply();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);

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
                textView.setVisibility(View.GONE);
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
                textView.setVisibility(View.GONE);
            }
        });

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && type.startsWith("image/")) {
            uriSource = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            handleImage();
        } else if (Intent.ACTION_EDIT.equals(action) && type != null && type.startsWith("image/")) {
            uriSource = intent.getData();
            handleImage();
        } else if ("com.android.camera.action.CROP".equals(action) && type != null && type.startsWith("image/")) {
            uriSource = intent.getData();
            handleImage();
        }

        final ImageButton button_edit = findViewById(R.id.button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GPUImageFilterTools.showDialog(activity, textView, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
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
                try {
                    cropImageView.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                    cropImageView.setAspectRatio(mGPUImageView.getGPUImage().getBitmapWithFilterApplied().getWidth(),
                            mGPUImageView.getGPUImage().getBitmapWithFilterApplied().getHeight());
                    appbar.setVisibility(View.INVISIBLE);
                    cropBar.setVisibility(View.VISIBLE);
                    cropImageView.setVisibility(View.VISIBLE);
                    mGPUImageView.setVisibility(View.INVISIBLE);

                    final Button button_1_1 = findViewById(R.id.button_1_1);
                    final Button button_4_3 = findViewById(R.id.button_4_3);
                    final Button button_3_4 = findViewById(R.id.button_3_4);
                    final Button button_16_9 = findViewById(R.id.button_16_9);
                    final Button button_9_16 = findViewById(R.id.button_9_16);
                    final Button button_custom = findViewById(R.id.button_custom);
                    button_custom.setTextColor(getResources().getColor(R.color.colorAccent, null));

                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = getTheme();
                    theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
                    @SuppressLint("Recycle")
                    TypedArray arr = obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.textColorPrimary});
                    final int color = arr.getColor(0, -1);

                    button_1_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cropImageView.setAspectRatio(1,1);
                            button_1_1.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            button_4_3.setTextColor(color);
                            button_3_4.setTextColor(color);
                            button_16_9.setTextColor(color);
                            button_9_16.setTextColor(color);
                            button_custom.setTextColor(color);
                        }
                    });
                    button_4_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cropImageView.setAspectRatio(4,3);
                            button_1_1.setTextColor(color);
                            button_4_3.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            button_3_4.setTextColor(color);
                            button_16_9.setTextColor(color);
                            button_9_16.setTextColor(color);
                            button_custom.setTextColor(color);
                        }
                    });
                    button_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cropImageView.setAspectRatio(3,4);
                            button_1_1.setTextColor(color);
                            button_4_3.setTextColor(color);
                            button_3_4.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            button_16_9.setTextColor(color);
                            button_9_16.setTextColor(color);
                            button_custom.setTextColor(color);
                        }
                    });
                    button_16_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cropImageView.setAspectRatio(16,9);
                            button_1_1.setTextColor(color);
                            button_4_3.setTextColor(color);
                            button_3_4.setTextColor(color);
                            button_16_9.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            button_9_16.setTextColor(color);
                            button_custom.setTextColor(color);
                        }
                    });
                    button_9_16.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cropImageView.setAspectRatio(9,16);
                            button_1_1.setTextColor(color);
                            button_4_3.setTextColor(color);
                            button_3_4.setTextColor(color);
                            button_16_9.setTextColor(color);
                            button_9_16.setTextColor(getResources().getColor(R.color.colorAccent, null));
                            button_custom.setTextColor(color);
                        }
                    });
                    button_custom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final BottomSheetDialog bottomSheetDialog_custom = new BottomSheetDialog(activity);
                            View dialogView = View.inflate(activity, R.layout.dialog_crop_custom, null);
                            final EditText dialog_width = dialogView.findViewById(R.id.dialog_width);
                            final EditText dialog_height = dialogView.findViewById(R.id.dialog_high);
                            ImageButton action_ok = dialogView.findViewById(R.id.button_apply);
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String height = dialog_height.getText().toString();
                                    String width = dialog_width.getText().toString();
                                    if (!height.isEmpty() && !width.isEmpty()) {
                                        int w = Integer.parseInt(width);
                                        int h = Integer.parseInt(height);
                                        cropImageView.setAspectRatio(w,h);
                                        bottomSheetDialog_custom.cancel();
                                        button_1_1.setTextColor(color);
                                        button_4_3.setTextColor(color);
                                        button_3_4.setTextColor(color);
                                        button_16_9.setTextColor(color);
                                        button_9_16.setTextColor(color);
                                        button_custom.setTextColor(getResources().getColor(R.color.colorAccent, null));
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.dialog_noInput),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            ImageButton action_cancel = dialogView.findViewById(R.id.button_apply_not);
                            action_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    bottomSheetDialog_custom.cancel();
                                }
                            });
                            ImageButton action_reset = dialogView.findViewById(R.id.button_reset);
                            action_reset.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cropImageView.setAspectRatio(mGPUImageView.getGPUImage().getBitmapWithFilterApplied().getWidth(),
                                            mGPUImageView.getGPUImage().getBitmapWithFilterApplied().getHeight());
                                    bottomSheetDialog_custom.cancel();
                                    button_1_1.setTextColor(color);
                                    button_4_3.setTextColor(color);
                                    button_3_4.setTextColor(color);
                                    button_16_9.setTextColor(color);
                                    button_9_16.setTextColor(color);
                                    button_custom.setTextColor(getResources().getColor(R.color.colorAccent, null));
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
                            button_save.setEnabled(true);
                            applyFilter();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    mGPUImageView.setFilter(new GPUImageFilter());
                    mGPUImageView.requestRender();
                    MsgUtil.show(activity, getString(R.string.dialog_loading));
                }
            }
        });

        ImageButton button_text = findViewById(R.id.button_text);
        button_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final LinearLayout textPositionLayout = findViewById(R.id.textPositionLayout);
                    final LinearLayout textToolsLayout = findViewById(R.id.textToolsLayout);
                    final BadgedImageView badged_iv_bottom_end = findViewById(R.id.badged_iv_bottom_end);  //4
                    final BadgedImageView badged_iv_bottom_start = findViewById(R.id.badged_iv_bottom_start);  //3
                    final BadgedImageView badged_iv_top_start = findViewById(R.id.badged_iv_top_start);  //1
                    final BadgedImageView badged_iv_top_end = findViewById(R.id.badged_iv_top_end);  //2

                    badged_iv_top_start.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                    badged_iv_top_end.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                    badged_iv_bottom_start.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
                    badged_iv_bottom_end.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());

                    badged_iv_bottom_end.setVisibility(View.VISIBLE);
                    badged_iv_bottom_start.setVisibility(View.INVISIBLE);
                    badged_iv_top_end.setVisibility(View.INVISIBLE);
                    badged_iv_top_start.setVisibility(View.INVISIBLE);
                    badgedImageView_int = 4;

                    appbar.setVisibility(View.INVISIBLE);
                    textBar.setVisibility(View.VISIBLE);
                    badged_iv_layout.setVisibility(View.VISIBLE);
                    mGPUImageView.setVisibility(View.INVISIBLE);
                    textColor = getColor(R.color.colorAccent);

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
                            button_save.setEnabled(true);
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
                } catch (Exception e) {
                    e.printStackTrace();
                    mGPUImageView.setFilter(new GPUImageFilter());
                    mGPUImageView.requestRender();
                    MsgUtil.show(activity, getString(R.string.dialog_loading));
                }
            }
        });

        button_save = findViewById(R.id.button_save);
        button_save.setEnabled(false);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                    MsgUtil.show(activity, getString(R.string.dialog_save_not));
                }
            }
        });

        button_open = findViewById(R.id.button_open);
        button_open.setEnabled(false);
        button_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openIntent = new Intent();
                openIntent.setAction(Intent.ACTION_VIEW);
                openIntent.setDataAndType(imageUri, "image/*");
                startActivity(openIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ContentResolver resolver = getContentResolver();
        try {
            OutputStream fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
            Objects.requireNonNull(fos).close();
            button_open.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            button_open.setEnabled(false);
        }
    }

    private static Bitmap getBitmapFromView(View v) {
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
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).flush();
        fos.close();

        mGPUImageView.setFilter(new GPUImageFilter());
        mGPUImageView.setImage(bitmap);
        filterApply.setEnabled(false);
        filterCancel.setEnabled(false);
        button_open.setEnabled(true);
        seekBar.setEnabled(false);
        MsgUtil.show(activity, getString(R.string.dialog_save_ok));
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
                button_save.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                MsgUtil.show(activity, getString(R.string.dialog_save_not));
            }
        } else {
            Bitmap bitmap = mGPUImageView.getGPUImage().getBitmapWithFilterApplied();
            mGPUImageView.setImage(bitmap);
            mGPUImageView.setFilter(new GPUImageFilter());
            button_save.setEnabled(true);
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
