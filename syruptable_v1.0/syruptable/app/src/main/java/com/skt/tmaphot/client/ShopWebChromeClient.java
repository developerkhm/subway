package com.skt.tmaphot.client;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.skt.tmaphot.ObservableWebView;

import static com.skt.tmaphot.temp.MainActivityWebViewUp.FILECHOOSER_LOLLIPOP_REQ_CODE;
import static com.skt.tmaphot.temp.MainActivityWebViewUp.FILECHOOSER_RESULTCODE;
import static com.skt.tmaphot.temp.MainActivityTab.placeholderFragment;

/**
 * Created by home on 2018-05-26.
 */

public class ShopWebChromeClient extends WebChromeClient {

    private Activity context;
    private ObservableWebView webView;

    public ShopWebChromeClient(Activity activity, ObservableWebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                .setCancelable(false)
                .create()
                .show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton("Yes",
                        new AlertDialog.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                .setNegativeButton("No",
                        new AlertDialog.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                .setCancelable(false)
                .create()
                .show();
        return true;
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        super.onGeolocationPermissionsShowPrompt(origin, callback);
        callback.invoke(origin, true, false);
    }

    // For Android 3.0-
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg){
        placeholderFragment.mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        context.startActivityForResult(Intent.createChooser(i,"File Chooser"),FILECHOOSER_RESULTCODE);
    }

    // For Android 3.0+
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){
        placeholderFragment.mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        context.startActivityForResult(Intent.createChooser(i,"File Browser"), FILECHOOSER_RESULTCODE);
    }

    // For Android 4.1+
    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
        placeholderFragment.mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        context.startActivityForResult(Intent.createChooser(i,"File Browser"), FILECHOOSER_RESULTCODE);
    }

    // For Android 5.0+
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams){
        Log.d("MainActivity", "5.0+");
        if(placeholderFragment.filePathCallbackLollipop != null){
            placeholderFragment.filePathCallbackLollipop.onReceiveValue(null);
            placeholderFragment.filePathCallbackLollipop = null;
        }

        placeholderFragment.filePathCallbackLollipop = filePathCallback ;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        context.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_LOLLIPOP_REQ_CODE);

        return true;
    }
}