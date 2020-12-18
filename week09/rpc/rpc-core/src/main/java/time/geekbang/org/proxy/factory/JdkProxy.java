package time.geekbang.org.proxy.factory;

import time.geekbang.org.proxy.base.AbstractRpcClientProxy;
import time.geekbang.org.proxy.base.RpcInvocationHandler;

import java.lang.reflect.Proxy;

public class JdkProxy<T> extends AbstractRpcClientProxy<T> {

    @Override
    protected T newProxy(final Class<T> service, final RpcInvocationHandler<T> handler) {
        return (T) Proxy.newProxyInstance(JdkProxy.class.getClassLoader(), new Class<?>[]{service}, handler);
    }
}
