package time.geekbang.org.proxy.factory;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import time.geekbang.org.proxy.base.AbstractRpcClientProxy;
import time.geekbang.org.proxy.base.RpcInvocationHandler;

public class ByteBuddyProxy<T> extends AbstractRpcClientProxy<T> {

    @Override
    @SneakyThrows
    protected T newProxy(final Class<T> service, final RpcInvocationHandler<T> handler) {
        return (T) new ByteBuddy().subclass(Object.class)
                .implement(service)
                .intercept(InvocationHandlerAdapter.of(handler))
                .make()
                .load(ByteBuddyProxy.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}
