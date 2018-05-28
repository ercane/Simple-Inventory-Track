package com.mree.inc.track.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mree.inc.track.R;
import com.mree.inc.track.TrackApp;
import com.mree.inc.track.db.persist.Product;
import com.mree.inc.track.ui.dialog.ProductEditDialog;
import com.mree.inc.track.ui.dialog.ProductViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

/**
 * Created by murat on 9/6/17.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();


    public static void ChangeTextWithAnimation(final TextView textView, final String text) {
        Animation anim = new TranslateAnimation(0f, 0f, 0f, textView.getHeight() * 3 / 4);
        anim.setDuration(200);
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                textView.setText(text);

            }
        });
        textView.startAnimation(anim);
    }

    public static int getStatusBarHeight() {
        int result = 0;
        Context context = TrackApp.getContext();
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static int dp2px(float dp) {
        final float scale = TrackApp.getContext().getResources().getDisplayMetrics
                ().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(float px) {
        final float scale = TrackApp.getContext().getResources().getDisplayMetrics
                ().density;
        return (int) (px / scale);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService
                (Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null)
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // A method to find height of the status bar
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String pasteFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) TrackApp.getContext().
                getSystemService(Context.CLIPBOARD_SERVICE);

        String pasteData = "";

        if (!(clipboard.hasPrimaryClip())) {

        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
            // since the clipboard has data but it is not plain text
        } else {
            //since the clipboard contains plain text.
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            // Gets the clipboard as text.
            pasteData = item.getText().toString();
        }

        return pasteData;
    }

    public static boolean copyToClipboard(String msg) {
        Context context = TrackApp.getContext();
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                clipboard.setText(msg);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                        context.getSystemService(context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText(context
                        .getResources().getString(R.string.copy_msg), msg);
                clipboard.setPrimaryClip(clip);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(
                TrackApp.getContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static @ColorInt
    int getColor(@ColorRes int value) {
        return ResourcesCompat.getColor(TrackApp.getContext().getResources(),
                value,
                null);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                .matches();
    }

    public final static boolean isValidPass(CharSequence target) {
        String lowerReg = "(.*[a-z].*)";
        String upperReg = "(.*[A-Z].*)";
        String numericReg = "(.*\\d.*)";

        Pattern lowerPattern = Pattern.compile(lowerReg);
        Pattern upperPattern = Pattern.compile(upperReg);
        Pattern numericPattern = Pattern.compile(numericReg);

        return !TextUtils.isEmpty(target) &&
                lowerPattern.matcher(target).matches() &&
                upperPattern.matcher(target).matches() &&
                numericPattern.matcher(target).matches();
    }


    public static void changeBitmapColor(Bitmap sourceBitmap, ImageView image, int color) {

        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        image.setImageBitmap(resultBitmap);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
    }

    public static Drawable changeDrawableColor(Context context, Drawable icon, int newColor) {
        icon.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return icon;
    }

    public static ColorFilter getColorFilter(int newColor) {
        return new PorterDuffColorFilter(getColor(newColor), PorterDuff.Mode.SRC_IN);
    }


    public static Integer getRandomNumber() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    public static void sendExceptionEvent(Class clazz, Throwable throwable) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put("class", clazz.getSimpleName());
            eventProperties.put("message", throwable.getMessage());
            if (throwable.getStackTrace() != null) {
                for (int i = 0; i < throwable.getStackTrace().length; i++) {
                    StackTraceElement element = throwable.getStackTrace()[i];
                    if (element != null)
                        eventProperties.put("stacktrace" + i, element.toString());
                }
            }
        } catch (JSONException exception) {
        }
    }

    public static JSONObject getEventProperties(String title, String msg) {
        JSONObject eventProperties = new JSONObject();
        try {
            eventProperties.put(title, msg);
        } catch (JSONException exception) {
        }
        return eventProperties;
    }

    public static boolean isInternetAvailable() {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(100, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }


    public static String getMsAsHourAndMinute(long ms) {
        String str = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(ms),
                TimeUnit.MILLISECONDS.toMinutes(ms) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)));
        return str;
    }

    public static void startViewDialog(Context context, Product product) {
        ProductViewDialog dialog = new ProductViewDialog(context, product);
        dialog.show();
    }

    public static void startEditDialog(Context context, Product product) {
        ProductEditDialog dialog = new ProductEditDialog(context, product);
        dialog.show();
    }

    public static void startDeleteDialog(Context context, Product product) {

    }

}
