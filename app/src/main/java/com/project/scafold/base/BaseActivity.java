package com.project.scafold.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.project.scafold.R;
import com.project.scafold.helpers.ColorHelper;
import com.project.scafold.helpers.LogHelper;
import com.project.scafold.interfaces.OnConfirmDialogListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by jayan on 8/27/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    MaterialDialog dialogLoading;
    MaterialDialog dialogConfirm;
    MaterialDialog.Builder dialogConfirmBuilder;
    FoldingCube drawableLoading = new FoldingCube();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawableLoading.setScale(0.6f);
        drawableLoading.setColor(ColorHelper.getColor(this, R.color.colorAccent));
        dialogLoading = new MaterialDialog.Builder(this)
                .content(R.string.dialog_message_pleasewait)
                .positiveColor(ColorHelper.getColor(this,R.color.colorAccent))
                .negativeColor(ColorHelper.getColor(this,R.color.colorSecondaryText))
                .titleColor(ColorHelper.getColor(this,R.color.colorAccent))
                .progress(true, 0)
                .build();
        dialogLoading.getProgressBar().setIndeterminateDrawable(drawableLoading);

        dialogConfirmBuilder = new MaterialDialog.Builder(this)
                .positiveColor(ColorHelper.getColor(this,R.color.colorAccent))
                .negativeColor(ColorHelper.getColor(this,R.color.colorSecondaryText))
                .titleColor(ColorHelper.getColor(this,R.color.colorAccent))
                .canceledOnTouchOutside(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        drawableLoading.setScale(0.6f);
        drawableLoading.setColor(ColorHelper.getColor(this,R.color.colorAccent));
        dialogLoading = new MaterialDialog.Builder(this)
                .content(R.string.dialog_message_pleasewait)
                .progress(true, 0).build();
        dialogLoading.getProgressBar().setIndeterminateDrawable(drawableLoading);
        dialogConfirmBuilder = new MaterialDialog.Builder(this)
                .positiveColor(ColorHelper.getColor(this,R.color.colorAccent))
                .negativeColor(ColorHelper.getColor(this,R.color.colorSecondaryText))
                .titleColor(ColorHelper.getColor(this,R.color.colorAccent))
                .canceledOnTouchOutside(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else{
            finish();
            animateToRight(this);
        }
    }

    public void setError(final TextInputLayout textInputLayout, final String message) {
        textInputLayout.setError(message);
        textInputLayout.setErrorEnabled(true);
        textInputLayout.getEditText().requestFocus();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isValidEmail(final String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void showLoading(){
        dialogLoading.setContent(R.string.dialog_message_pleasewait);
        dialogLoading.show();
    }

    public void showLoading(String content){
        dialogLoading.setContent(content);
        dialogLoading.show();
    }

    public void dismissLoading(){
        dialogLoading.hide();
    }

    public void showConfirmDialog(final String header, final String message,
                                  final String positiveText, final String negativeText,
                                  final OnConfirmDialogListener onConfirmDialogListener) {

        if (dialogConfirm != null && dialogConfirm.isShowing()) dialogConfirm.dismiss();

        if (header != null && !header.isEmpty()){
            dialogConfirmBuilder.title(header);
        }

        if (message != null && !message.isEmpty()) {
            dialogConfirmBuilder.content(message);
        }

        if (!positiveText.isEmpty()) {
            dialogConfirmBuilder.positiveText(positiveText);
            dialogConfirmBuilder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (onConfirmDialogListener != null) {
                        onConfirmDialogListener.onConfirmed();
                    }
                }
            });
        }

        if (!negativeText.isEmpty()) {
            dialogConfirmBuilder.negativeText(negativeText);
            dialogConfirmBuilder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (onConfirmDialogListener != null) {
                        onConfirmDialogListener.onCancelled();
                    }
                }
            });
        }
        dialogConfirm = dialogConfirmBuilder.build();
        dialogConfirm.show();
    }

    public void dismissConfirm(){
        if (dialogConfirm != null && dialogConfirm.isShowing()) dialogConfirm.dismiss();
    }

    public void showSnackbarSuccess(final String message) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSuccessMessage));
        snack.show();
    }

    public void showSnackbarError(final String message) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorErrorMessage));
        snack.show();
    }

    /**
     * check network connection availability
     */
    public boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            isConnected = true;
        } else {
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mData == null) {
                isConnected = false;
            } else {
                boolean isDataEnabled = mData.isConnected();
                isConnected = isDataEnabled ? true : false;
            }
        }
        return isConnected;
    }

    public void animateToLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public static void animateToRight(Activity activity) {
        activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    public Drawable getImageById(final int id) {
        return ContextCompat.getDrawable(this, id);
    }

    public void setToolbarTitle(final String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void updateToolbarTitle(final String title) {
        getSupportActionBar().setTitle(title);
    }


    public String getStringResource(int stringId){
        return getResources().getString(stringId);
    }

    public SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    public SimpleDateFormat getSDF() {
        return new SimpleDateFormat("EEE, yyyy-MM-dd hh:mm a");
    }

    public PrettyTime getPrettyTime() {
        return new PrettyTime();
    }

    public String getFilePath(final Intent data) {
        final Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        final int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        final String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    public Bitmap getBitmapFromURI(final Uri uri) {
        final ParcelFileDescriptor parcelFileDescriptor;
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            final FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogHelper.log("img", "file not found exception --> " + e.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LogHelper.log("img", "io exception ---> " + ioe.getMessage());
        }
        return null;
    }

    public File getFile(final Uri uri, final String fileName) {
        try {
            final ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            final FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            final byte[] bitmapdata = bos.toByteArray();

            final File f = new File(getCacheDir(), fileName);
            final FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (IOException e) {
            LogHelper.log("select_image", "unable to select image --> " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public File rotateBitmap(String path) {
        final File file = new File(path);

        try {
            final ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotation = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
            }

            final Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            resizeImage(file, matrix);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    private void resizeImage(final File file, final Matrix matrix) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 0; //try to decrease decoded image
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (Exception ex) {
        }
    }

    public File createImageFile() {
        final String imageFileName = UUID.randomUUID().toString() + ".png";

        final File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                imageFileName);
        return mediaStorageDir;
    }

    public boolean isFacebookInstalled() {
        try {
            getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void moveToOtherActivity(Class clz) {
        startActivity(new Intent(this, clz));
        animateToLeft(this);
    }

    public void moveToOtherActivityWithSharedElements(Class clz, View view, String transitionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, (View) view, transitionName);
            startActivity(new Intent(this, clz), options.toBundle());
        }else{
            startActivity(new Intent(this, clz));
        }
    }

    public void moveToOtherActivityWithSharedElements(Class clz, ActivityOptionsCompat options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(new Intent(this, clz), options.toBundle());
        }else{
            startActivity(new Intent(this, clz));
        }
    }
}
