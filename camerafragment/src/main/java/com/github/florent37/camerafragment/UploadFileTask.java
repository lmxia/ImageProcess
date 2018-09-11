package com.github.florent37.camerafragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadFileTask extends AsyncTask<String, Void, ImageProcessResponse> {
    private static final String[] MSG = {"上传成功！", "上传失败！", "文件不存在！"};


    private AsynUpdateListener asynUpdateListener;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;

    public UploadFileTask(Activity activity) {
        mActivity = activity;
        mProgressDialog = ProgressDialog.show(mActivity, "正在上传...",
                "系统正在处理您的请求");
    }

    public AsynUpdateListener getAsynUpdateListener() {
        return asynUpdateListener;
    }

    public UploadFileTask setAsynUpdateListener(AsynUpdateListener asynUpdateListener) {
        this.asynUpdateListener = asynUpdateListener;
        return this;
    }

    @Override
    protected ImageProcessResponse doInBackground(String... params) {
        try {
            return UploadUtils.postFileToURL(new File(params[0]), params[1],
                    new URL(params[2]), params[3], ImageProcessResponse.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ImageProcessResponse result) {
        mProgressDialog.dismiss();
        if (result.getCode().equals("200")){
            asynUpdateListener.onDataReceivedSuccess(result.data);
            Toast.makeText(mActivity,"识别成功", Toast.LENGTH_LONG).show();
        }else{
            asynUpdateListener.onDataReceivedFailed();
            Toast.makeText(mActivity,"上传/识别失败", Toast.LENGTH_LONG).show();
        }

    }
}