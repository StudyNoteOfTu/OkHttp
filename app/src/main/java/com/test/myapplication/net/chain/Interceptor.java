package com.test.myapplication.net.chain;


import com.test.myapplication.net.Response;

import java.io.IOException;

public interface Interceptor {

    Response intercept(InterceptorChain chain) throws IOException;

}
