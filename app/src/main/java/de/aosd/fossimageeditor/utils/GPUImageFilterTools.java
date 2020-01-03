/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.aosd.fossimageeditor.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.Matrix;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

import de.aosd.fossimageeditor.R;
import jp.co.cyberagent.android.gpuimage.filter.*;

import java.util.LinkedList;
import java.util.List;

public class GPUImageFilterTools {

    public static void showDialog (final Context context, final OnGpuImageFilterChosenListener listener) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = View.inflate(context, R.layout.dialog_filter, null);

        LinearLayout menu_filter_saturation = dialogView.findViewById(R.id.filter_saturation);
        menu_filter_saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.SATURATION));
                bottomSheetDialog.cancel();
            }
        });
        LinearLayout menu_filter_exposure = dialogView.findViewById(R.id.filter_exposure);
        menu_filter_exposure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.EXPOSURE));
                bottomSheetDialog.cancel();
            }
        });
        LinearLayout menu_filter_contrast = dialogView.findViewById(R.id.filter_contrast);
        menu_filter_contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.CONTRAST));
                bottomSheetDialog.cancel();
            }
        });
        LinearLayout menu_filter_white = dialogView.findViewById(R.id.filter_white);
        menu_filter_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.WHITE_BALANCE));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_hue = dialogView.findViewById(R.id.filter_hue);
        menu_filter_hue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.HUE));
                bottomSheetDialog.cancel();
            }
        });
        LinearLayout menu_filter_gamma = dialogView.findViewById(R.id.filter_gamma);
        menu_filter_gamma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.GAMMA));
                bottomSheetDialog.cancel();
            }
        });
        LinearLayout menu_filter_colorBalance = dialogView.findViewById(R.id.filter_colorBalance);
        menu_filter_colorBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.COLOR_BALANCE));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_monochrome = dialogView.findViewById(R.id.filter_monochrome);
        menu_filter_monochrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.SEPIA));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_rgb = dialogView.findViewById(R.id.filter_rgb);
        menu_filter_rgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.RGB));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_haze = dialogView.findViewById(R.id.filter_haze);
        menu_filter_haze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.HAZE));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_vignette = dialogView.findViewById(R.id.filter_vignette);
        menu_filter_vignette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.VIGNETTE));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_blur = dialogView.findViewById(R.id.filter_blur);
        menu_filter_blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.GAUSSIAN_BLUR));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_sharpen = dialogView.findViewById(R.id.filter_sharpen);
        menu_filter_sharpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.SHARPEN));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_toon = dialogView.findViewById(R.id.filter_toon);
        menu_filter_toon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.TOON));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_invert = dialogView.findViewById(R.id.filter_invert);
        menu_filter_invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.INVERT));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_halftone = dialogView.findViewById(R.id.filter_halftone);
        menu_filter_halftone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.HALFTONE));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_poster = dialogView.findViewById(R.id.filter_poster);
        menu_filter_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.POSTERIZE));
                bottomSheetDialog.cancel();
            }
        });
        CardView menu_filter_pixel = dialogView.findViewById(R.id.filter_pixelation);
        menu_filter_pixel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGpuImageFilterChosenListener(createFilterForType(context, FilterType.PIXELATION));
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        MsgUtil.setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    private static GPUImageFilter createFilterForType(final Context context, final FilterType type) {

        switch (type) {

            case CONTRAST:
                return new GPUImageContrastFilter(2.0f);
            case GAMMA:
                return new GPUImageGammaFilter(2.0f);
            case INVERT:
                return new GPUImageColorInvertFilter();
            case PIXELATION:
                return new GPUImagePixelationFilter();
            case HUE:
                return new GPUImageHueFilter(90.0f);
            case BRIGHTNESS:
                return new GPUImageBrightnessFilter(1.5f);
            case GRAYSCALE:
                return new GPUImageGrayscaleFilter();
            case SEPIA:
                return new GPUImageSepiaToneFilter();
            case SHARPEN:
                return new GPUImageSharpenFilter();
            case SOBEL_EDGE_DETECTION:
                return new GPUImageSobelEdgeDetectionFilter();
            case THRESHOLD_EDGE_DETECTION:
                return new GPUImageThresholdEdgeDetectionFilter();
            case THREE_X_THREE_CONVOLUTION:
                return new GPUImage3x3ConvolutionFilter();
            case EMBOSS:
                return new GPUImageEmbossFilter();
            case POSTERIZE:
                return new GPUImagePosterizeFilter();
            case FILTER_GROUP:
                List<GPUImageFilter> filters = new LinkedList<>();
                filters.add(new GPUImageContrastFilter());
                filters.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                filters.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(filters);
            case SATURATION:
                return new GPUImageSaturationFilter(1.0f);
            case EXPOSURE:
                return new GPUImageExposureFilter(0.0f);
            case HIGHLIGHT_SHADOW:
                return new GPUImageHighlightShadowFilter(
                    0.0f,
                    1.0f
            );
            case MONOCHROME:
                return new GPUImageMonochromeFilter(1.0f, new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            case OPACITY:
                return new GPUImageOpacityFilter(1.0f);
            case RGB:
                return new GPUImageRGBFilter(1.0f, 1.0f, 1.0f);
            case WHITE_BALANCE:
                return new GPUImageWhiteBalanceFilter(
                    5000.0f,
                    0.0f
            );
            case VIGNETTE:
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[] {0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case LUMINANCE:
                return new GPUImageLuminanceFilter();
            case LUMINANCE_THRESHSOLD:
                return new GPUImageLuminanceThresholdFilter(0.5f);
            case BLEND_DIFFERENCE:
                createBlendFilter(context, GPUImageDifferenceBlendFilter.class);
            case BLEND_SOURCE_OVER:
                createBlendFilter(context, GPUImageSourceOverBlendFilter.class);
            case BLEND_COLOR_BURN:
                createBlendFilter(context, GPUImageColorBurnBlendFilter.class);
            case BLEND_COLOR_DODGE:
                createBlendFilter(context, GPUImageColorDodgeBlendFilter.class);
            case BLEND_DARKEN:
                createBlendFilter(context, GPUImageDarkenBlendFilter.class);
            case BLEND_DISSOLVE:
                createBlendFilter(context, GPUImageDissolveBlendFilter.class);
            case BLEND_EXCLUSION:
                createBlendFilter(context, GPUImageExclusionBlendFilter.class);
            case BLEND_HARD_LIGHT:
                createBlendFilter(context, GPUImageHardLightBlendFilter.class);
            case BLEND_LIGHTEN:
                createBlendFilter(context, GPUImageLightenBlendFilter.class);
            case BLEND_ADD:
                createBlendFilter(context, GPUImageAddBlendFilter.class);
            case BLEND_DIVIDE:
                createBlendFilter(context, GPUImageDivideBlendFilter.class);
            case BLEND_MULTIPLY:
                createBlendFilter(context, GPUImageMultiplyBlendFilter.class);
            case BLEND_OVERLAY:
                createBlendFilter(context, GPUImageOverlayBlendFilter.class);
            case BLEND_SCREEN:
                createBlendFilter(context, GPUImageScreenBlendFilter.class);
            case BLEND_ALPHA:
                createBlendFilter(context, GPUImageAlphaBlendFilter.class);
            case BLEND_COLOR:
                createBlendFilter(context, GPUImageColorBlendFilter.class);
            case BLEND_HUE:
                createBlendFilter(context, GPUImageHueBlendFilter.class);
            case BLEND_SATURATION:
                createBlendFilter(context, GPUImageSaturationBlendFilter.class);
            case BLEND_LUMINOSITY:
                createBlendFilter(context, GPUImageLuminosityBlendFilter.class);
            case BLEND_LINEAR_BURN:
                createBlendFilter(context, GPUImageLinearBurnBlendFilter.class);
            case BLEND_SOFT_LIGHT:
                createBlendFilter(context, GPUImageSoftLightBlendFilter.class);
            case BLEND_SUBTRACT:
                createBlendFilter(context, GPUImageSubtractBlendFilter.class);
            case BLEND_CHROMA_KEY:
                createBlendFilter(context, GPUImageChromaKeyBlendFilter.class);
            case BLEND_NORMAL:
                createBlendFilter(context, GPUImageNormalBlendFilter.class);
            case GAUSSIAN_BLUR:
                return new GPUImageGaussianBlurFilter();
            case CROSSHATCH:
                return new GPUImageCrosshatchFilter();
            case BOX_BLUR:
                return new GPUImageBoxBlurFilter();
            case CGA_COLORSPACE:
                return new GPUImageCGAColorspaceFilter();
            case DILATION:
                return new GPUImageDilationFilter();
            case KUWAHARA:
                return new GPUImageKuwaharaFilter();
            case RGB_DILATION:
                return new GPUImageRGBDilationFilter();
            case SKETCH:
                return new GPUImageSketchFilter();
            case TOON:
                return new GPUImageToonFilter();
            case SMOOTH_TOON:
                return new GPUImageSmoothToonFilter();
            case BULGE_DISTORTION:
                return new GPUImageBulgeDistortionFilter();
            case GLASS_SPHERE:
                return new GPUImageGlassSphereFilter();
            case HAZE:
                return new GPUImageHazeFilter();
            case LAPLACIAN:
                return new GPUImageLaplacianFilter();
            case NON_MAXIMUM_SUPPRESSION:
                return new GPUImageNonMaximumSuppressionFilter();
            case SPHERE_REFRACTION:
                return new GPUImageSphereRefractionFilter();
            case SWIRL:
                return new GPUImageSwirlFilter();
            case WEAK_PIXEL_INCLUSION:
                return new GPUImageWeakPixelInclusionFilter();
            case FALSE_COLOR:
                return new GPUImageFalseColorFilter();
            case COLOR_BALANCE:
                return new GPUImageColorBalanceFilter();
            case LEVELS_FILTER_MIN:
                return new GPUImageLevelsFilter();
            case HALFTONE:
                return new GPUImageHalftoneFilter();
            case BILATERAL_BLUR:
                return new GPUImageBilateralBlurFilter();
            case ZOOM_BLUR:
                return new GPUImageZoomBlurFilter();
            case TRANSFORM2D:
                return new GPUImageTransformFilter();
            case SOLARIZE:
                return new GPUImageSolarizeFilter();
            case VIBRANCE:
                return new GPUImageVibranceFilter();

            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }

    private static void createBlendFilter(Context context, Class<? extends GPUImageTwoInputFilter> filterClass) {
        try {
            GPUImageTwoInputFilter filter = filterClass.newInstance();
            filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.raw.tone_cuver_sample));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnGpuImageFilterChosenListener {
        void onGpuImageFilterChosenListener(GPUImageFilter filter);
    }

    public enum FilterType {CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THRESHOLD_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, LUMINANCE, LUMINANCE_THRESHSOLD, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN,
        BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, ZOOM_BLUR, HALFTONE, TRANSFORM2D, SOLARIZE, VIBRANCE
    }

    public static class FilterAdjuster {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        public FilterAdjuster(final GPUImageFilter filter) {

            if (filter instanceof GPUImageSharpenFilter) {
                adjuster = new SharpnessAdjuster().filter(filter);
            }

            else if (filter instanceof GPUImageSepiaToneFilter) {
                adjuster = new SepiaAdjuster().filter(filter);
            }



            else if (filter instanceof GPUImageContrastFilter) {
                adjuster = new ContrastAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGammaFilter) {
                adjuster = new GammaAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBrightnessFilter) {
                adjuster = new BrightnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageEmbossFilter) {
                adjuster = new EmbossAdjuster().filter(filter);
            } else if (filter instanceof GPUImage3x3TextureSamplingFilter) {
                adjuster = new GPU3x3TextureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHueFilter) {
                adjuster = new HueAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePosterizeFilter) {
                adjuster = new PosterizeAdjuster().filter(filter);
            } else if (filter instanceof GPUImagePixelationFilter) {
                adjuster = new PixelationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSaturationFilter) {
                adjuster = new SaturationAdjuster().filter(filter);
            } else if (filter instanceof GPUImageExposureFilter) {
                adjuster = new ExposureAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHighlightShadowFilter) {
                adjuster = new HighlightShadowAdjuster().filter(filter);
            } else if (filter instanceof GPUImageMonochromeFilter) {
                adjuster = new MonochromeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageOpacityFilter) {
                adjuster = new OpacityAdjuster().filter(filter);
            } else if (filter instanceof GPUImageRGBFilter) {
                adjuster = new RGBAdjuster().filter(filter);
            } else if (filter instanceof GPUImageWhiteBalanceFilter) {
                adjuster = new WhiteBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageVignetteFilter) {
                adjuster = new VignetteAdjuster().filter(filter);
            } else if (filter instanceof GPUImageDissolveBlendFilter) {
                adjuster = new DissolveBlendAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGaussianBlurFilter) {
                adjuster = new GaussianBlurAdjuster().filter(filter);
            } else if (filter instanceof GPUImageCrosshatchFilter) {
                adjuster = new CrosshatchBlurAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBulgeDistortionFilter) {
                adjuster = new BulgeDistortionAdjuster().filter(filter);
            } else if (filter instanceof GPUImageGlassSphereFilter) {
                adjuster = new GlassSphereAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHazeFilter) {
                adjuster = new HazeAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSphereRefractionFilter) {
                adjuster = new SphereRefractionAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSwirlFilter) {
                adjuster = new SwirlAdjuster().filter(filter);
            } else if (filter instanceof GPUImageColorBalanceFilter) {
                adjuster = new ColorBalanceAdjuster().filter(filter);
            } else if (filter instanceof GPUImageLevelsFilter) {
                adjuster = new LevelsMinMidAdjuster().filter(filter);
            } else if (filter instanceof GPUImageTransformFilter) {
                adjuster = new RotateAdjuster().filter(filter);
            } else if (filter instanceof GPUImageHalftoneFilter) {
                adjuster = new HalftoneAdjuster().filter(filter);
            } else {
                adjuster = null;
            }
        }

        public boolean canAdjust() {
            return adjuster != null;
        }

        public void adjust(final int percentage) {
            if (adjuster != null) {
                adjuster.adjust(percentage);
            }
        }

        @SuppressWarnings("WeakerAccess")
        private abstract class Adjuster<T extends GPUImageFilter> {
            private T filter;

            @SuppressWarnings("unchecked")
            public Adjuster<T> filter(final GPUImageFilter filter) {
                this.filter = (T) filter;
                return this;
            }

            public T getFilter() {
                return filter;
            }

            public abstract void adjust(int percentage);

            protected float range(final int percentage, final float start, final float end) {
                return (end - start) * percentage / 100.0f + start;
            }

            protected int range(final int percentage) {
                return (50 - 1) * percentage / 100 + 1;
            }
        }

        private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSharpness(range(percentage, -4.0f, 4.0f));
            }
        }

        private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
          @Override
          public void adjust(final int percentage) {
              getFilter().setPixel(range(percentage, 1.0f, 100.0f));
          }
        }

        private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
          @Override
          public void adjust(final int percentage) {
            getFilter().setHue(range(percentage, 0.0f, 360.0f));
          }
        }

        private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setContrast(range(percentage, 0.0f, 2.0f));
            }
        }

        private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setGamma(range(percentage, 0.0f, 3.0f));
            }
        }

        private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setBrightness(range(percentage, -1.0f, 1.0f));
            }
        }

        private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 4.0f));
            }
        }

        private class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
            @Override
            public void adjust(final int percentage) {
                // In theorie to 256, but only first 50 are interesting
                getFilter().setColorLevels(range(percentage));
            }
        }

        private class GPU3x3TextureAdjuster extends Adjuster<GPUImage3x3TextureSamplingFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setLineSize(range(percentage, 0.0f, 5.0f));
            }
        }

        private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSaturation(range(percentage, 0.0f, 2.0f));
            }
        }

        private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setExposure(range(percentage, -10.0f, 10.0f));
            }
        }

        private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setShadows(range(percentage, 0.0f, 1.0f));
                getFilter().setHighlights(range(percentage, 0.0f, 1.0f));
            }
        }

        private class SepiaAdjuster extends Adjuster<GPUImageSepiaToneFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 2.0f));
            }
        }

        private class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setIntensity(range(percentage, 0.0f, 1.0f));
                //getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            }
        }

        private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setOpacity(range(percentage, 0.0f, 1.0f));
            }
        }

        private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRed(range(percentage, 0.0f, 1.0f));
                //getFilter().setGreen(range(percentage, 0.0f, 1.0f));
                //getFilter().setBlue(range(percentage, 0.0f, 1.0f));
            }
        }

        private class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setTemperature(range(percentage, 2000.0f, 8000.0f));
                //getFilter().setTint(range(percentage, -100.0f, 100.0f));
            }
        }

        private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setVignetteStart(range(percentage, 0.0f, 1.0f));
            }
        }

        private class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setMix(range(percentage, 0.0f, 1.0f));
            }
        }

        private class GaussianBlurAdjuster extends Adjuster<GPUImageGaussianBlurFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setBlurSize(range(percentage, 0.0f, 1.0f));
            }
        }

        private class CrosshatchBlurAdjuster extends Adjuster<GPUImageCrosshatchFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setCrossHatchSpacing(range(percentage, 0.0f, 0.06f));
                getFilter().setLineWidth(range(percentage, 0.0f, 0.006f));
            }
        }

        private class HalftoneAdjuster extends Adjuster<GPUImageHalftoneFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setFractionalWidthOfAPixel(range(percentage, 0.0f, 0.5f));
            }
        }

        private class BulgeDistortionAdjuster extends Adjuster<GPUImageBulgeDistortionFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRadius(range(percentage, 0.0f, 1.0f));
                getFilter().setScale(range(percentage, -1.0f, 1.0f));
            }
        }

        private class GlassSphereAdjuster extends Adjuster<GPUImageGlassSphereFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRadius(range(percentage, 0.0f, 1.0f));
            }
        }

        private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setDistance(range(percentage, -0.3f, 0.3f));
                getFilter().setSlope(range(percentage, -0.3f, 0.3f));
            }
        }

        private class SphereRefractionAdjuster extends Adjuster<GPUImageSphereRefractionFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setRadius(range(percentage, 0.0f, 1.0f));
            }
        }

        private class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setAngle(range(percentage, 0.0f, 2.0f));
            }
        }

        private class ColorBalanceAdjuster extends Adjuster<GPUImageColorBalanceFilter> {

            @Override
            public void adjust(int percentage) {
                getFilter().setMidtones(new float[]{
                        range(percentage, 0.0f, 1.0f),
                        range(percentage / 2, 0.0f, 1.0f),
                        range(percentage / 3, 0.0f, 1.0f)});
            }
        }

        private class LevelsMinMidAdjuster extends Adjuster<GPUImageLevelsFilter> {
            @Override
            public void adjust(int percentage) {
                getFilter().setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f);
            }
        }

        private class RotateAdjuster extends Adjuster<GPUImageTransformFilter> {
            @Override
            public void adjust(final int percentage) {
                float[] transform = new float[16];
                Matrix.setRotateM(transform, 0, 360 * percentage / 100, 0, 0, 1.0f);
                getFilter().setTransform3D(transform);
            }
        }

    }
}
