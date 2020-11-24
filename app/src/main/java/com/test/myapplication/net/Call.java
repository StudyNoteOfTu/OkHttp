package com.test.myapplication.net;

import com.test.myapplication.net.chain.CallServiceInterceptor;
import com.test.myapplication.net.chain.ConnectionInterceptor;
import com.test.myapplication.net.chain.HeadersInterceptor;
import com.test.myapplication.net.chain.Interceptor;
import com.test.myapplication.net.chain.InterceptorChain;
import com.test.myapplication.net.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;

public class Call {
    Request request;
    HttpClient client;
    /**
     * 是否执行过
     */
    boolean executed;

    //取消
    boolean canceled;

    public Call(Request request, HttpClient client) {
        this.request = request;
        this.client = client;
    }

    public Request request() {
        return request;
    }

    public Call enqueue(Callback callback) {
        //不能重复执行
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Execute");
            }
            executed = true;
        }
        client.dispatcher().enqueue(new AsyncCall(callback));
        return this;
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public HttpClient client() {
        return client;
    }

    final class AsyncCall implements Runnable {

        private final Callback callback;

        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            //是否已经通知过callback
            boolean signalledCallback = false;
            try {
                Response response = getResponse();
                if (canceled) {
                    signalledCallback = true;
                    callback.onFailure(Call.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onResponse(Call.this, response);
                }
            } catch (IOException e) {
                if (!signalledCallback) {
                    callback.onFailure(Call.this, e);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.url().host;
        }
    }

    private Response getResponse() throws IOException {
        //添加拦截器
        ArrayList<Interceptor>  interceptors= new ArrayList<>();
        interceptors.addAll(client.interceptors());
        interceptors.add(new RetryInterceptor());
        interceptors.add(new HeadersInterceptor());
        interceptors.add(new ConnectionInterceptor());
        interceptors.add(new CallServiceInterceptor());

        InterceptorChain interceptorChain = new InterceptorChain(interceptors,
                0, this, null);

        return interceptorChain.proceed();
    }
}
