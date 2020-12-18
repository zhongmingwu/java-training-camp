package time.geekbang.org.proxy.base;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import time.geekbang.org.meta.RpcRequest;
import time.geekbang.org.meta.RpcResponse;
import time.geekbang.org.netty.client.RpcNettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

@Slf4j
@AllArgsConstructor
public class RpcInvocationHandler<T> implements InvocationHandler, MethodInterceptor {

    private final Class<T> services;
    private final String url;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return request(services, method, args, url);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return request(services, method, args, url);
    }

    private Object request(Class<?> service, Method method, Object[] args, String url) {
        RpcRequest rpcRequest = RpcRequest.builder().service(service.getName()).method(method.getName()).args(args).build();

        RpcResponse rpcResponse;
        try {
            rpcResponse = RpcNettyClient.getInstance().sendRequest(rpcRequest, url);
        } catch (InterruptedException | URISyntaxException e) {
            RpcNettyClient.getInstance().destroy();
            log.error("request fail, rpcRequest=" + rpcRequest, e);
            return null;
        }

        if (!rpcResponse.getSuccess()) {
            log.error("request fail, rpcRequest={}, rpcResponse={}", rpcRequest, rpcResponse);
            return null;
        }

        log.info("request success, rpcRequest={}, rpcResponse={}", rpcRequest, rpcResponse);
        return JSON.parse(rpcResponse.getResult().toString());
    }
}
