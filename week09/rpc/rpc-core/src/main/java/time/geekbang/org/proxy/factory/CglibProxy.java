package time.geekbang.org.proxy.factory;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import time.geekbang.org.proxy.base.AbstractRpcClientProxy;
import time.geekbang.org.proxy.base.RpcInvocationHandler;

@Slf4j
public class CglibProxy<T> extends AbstractRpcClientProxy<T> {

    @Override
    protected T newProxy(final Class<T> service, final RpcInvocationHandler<T> handler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(handler);
        enhancer.setSuperclass(service);
        return (T) enhancer.create();
    }
}
