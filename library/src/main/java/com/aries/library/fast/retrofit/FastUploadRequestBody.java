package com.aries.library.fast.retrofit;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created: AriesHoo on 2018/7/11 17:32
 * E-Mail: AriesHoo@126.com
 * Function: 封装上传进度监听
 * Description:
 */
public class FastUploadRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private FastUploadRequestListener mListener;

    public FastUploadRequestBody(RequestBody requestBody, FastUploadRequestListener uploadListener) {
        mRequestBody = requestBody;
        mListener = uploadListener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            if (mListener != null) {
                mListener.onFail(e);
            }
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        //BufferedSink 是 Buffer 类型;
        // 而实际进行网络请求的 BufferedSink 是 FixedLengthSink;
        // 所以修改 FastUploadRequestBody 里的 writeTo(BufferedSink sink) 方法;
        // 如果传入的 sink 为 Buffer 对象,则直接写入,不进行进度统计
        if (sink instanceof Buffer) {
            mRequestBody.writeTo(sink);
            return;
        }
        BufferedSink bufferedSink = Okio.buffer(new CountingSink(sink));
        mRequestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    class CountingSink extends ForwardingSink {

        private long mCurrent = 0;
        private long mTotal = -1;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            mCurrent += byteCount;
            if (mTotal == -1) {
                mTotal = contentLength();
            }
            if (mListener != null)
                mListener.onProgress(mCurrent * 1.0f / contentLength(), mCurrent, mTotal);
        }
    }
}
