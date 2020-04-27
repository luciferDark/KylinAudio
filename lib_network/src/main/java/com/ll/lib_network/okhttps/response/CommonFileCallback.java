package com.ll.lib_network.okhttps.response;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.ll.lib_network.okhttps.response.listener.DisposeDataHandler;
import com.ll.lib_network.okhttps.response.listener.DisposeDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * okhttps网络Json类型响应封装
 *
 * @author kylin
 * @data 2020/04/28
 */
public class CommonFileCallback extends CommonBaseCallback {
    private static final int MSG_PROGRESS = 0x01;

    private Handler mDeliverHandler;//用于主线程切换
    private DisposeDownloadListener mDisposeDownloadListener;
    private String mFilePath;
    private int mProgress;

    public CommonFileCallback(DisposeDataHandler mHandler) {
        this.mDisposeDownloadListener = (DisposeDownloadListener) mHandler.mDisposeDataListener;
        this.mFilePath = mHandler.mSource;

        mDeliverHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_PROGRESS:
                        int progress = (int) msg.obj;
                        onProgress(mDisposeDownloadListener, progress);
                        break;
                }
            }
        };
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        handlerMainUI(mDeliverHandler, new Runnable() {
            @Override
            public void run() {
                onFailed(mDisposeDownloadListener, NETWORK_ERROR, e);
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        final File file = onHandlerResponse(response);
        handlerMainUI(mDeliverHandler, new Runnable() {
            @Override
            public void run() {
                if (null == file) {
                    onFailed(mDisposeDownloadListener, IO_ERROR, EMPTY_MSG);
                } else {
                    onSuccess(mDisposeDownloadListener, file);
                }
            }
        });
    }

    /**
     * 处理文件下载
     *
     * @param response
     * @return
     */
    private File onHandlerResponse(Response response) {
        if (null == response) {
            return null;
        }

        File mFile = null;
        InputStream mInputStream = null;
        FileOutputStream mFileOutputStream = null;
        byte[] buffer = new byte[1024 * 2];

        mFile = checkLocalFile(mFilePath);
        if (null == mFile) {
            return null;
        }

        int readLength = 0; //每次读取文件长度
        double fileSumLength = 0; //文件总大小
        double currentLength = 0;//当前文件长度
        try {
            mInputStream = response.body().byteStream();
            fileSumLength = response.body().contentLength();
            mFileOutputStream = new FileOutputStream(mFile);

            while ((readLength = mInputStream.read(buffer)) != -1) {
                mFileOutputStream.write(buffer, 0, buffer.length);

                currentLength += readLength;
                mProgress = (int) (currentLength / fileSumLength * 100);
                if (null != mDeliverHandler) {
                    mDeliverHandler.obtainMessage(MSG_PROGRESS, mProgress);
                }
            }
            mFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            mFile = null;
        } finally {
            try {
                if (null != mInputStream) {
                    mInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != mFileOutputStream) {
                    mFileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mFile;
    }

    /**
     * 校验下载目录和文件
     *
     * @param localFilePath
     * @return
     */
    private File checkLocalFile(String localFilePath) {
        if (TextUtils.isEmpty(localFilePath)) {
            return null;
        }
        File mLocalFile = null;
        try {
            String localFileDir = localFilePath.substring(0, localFilePath.indexOf("/") + 1);
            File mLocalFileDirFile = new File(localFileDir);
            if (!mLocalFileDirFile.exists()) {
                mLocalFileDirFile.mkdirs();
            }
            mLocalFile = new File(localFilePath);
            if (!mLocalFile.exists()) {
                mLocalFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mLocalFile;
    }
}
