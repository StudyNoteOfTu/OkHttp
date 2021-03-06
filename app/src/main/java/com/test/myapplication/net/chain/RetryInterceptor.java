package com.test.myapplication.net.chain;

import android.util.Log;

import com.test.myapplication.net.Call;
import com.test.myapplication.net.Response;

import java.io.IOException;

public class RetryInterceptor implements Interceptor{

    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.e("interceprot", "重试拦截器....");
        Call call = chain.call;
        IOException exception = null;
        for (int i= 0; i < chain.call.client().retrys(); i ++){
            if(call.isCanceled()){
                throw new IOException("Canceled");
            }
            try {
                Response response = chain.proceed();
                return response;
            }catch (IOException e){
                exception = e;
            }
        }
        throw exception;
    }
}
