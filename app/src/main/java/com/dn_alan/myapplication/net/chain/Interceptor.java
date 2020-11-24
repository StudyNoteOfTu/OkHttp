package com.dn_alan.myapplication.net.chain;


import com.dn_alan.myapplication.net.Response;

import java.io.IOException;

public interface Interceptor {

    Response intercept(InterceptorChain chain) throws IOException;

}
