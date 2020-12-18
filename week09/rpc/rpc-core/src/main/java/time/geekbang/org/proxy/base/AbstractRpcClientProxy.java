package time.geekbang.org.proxy.base;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class AbstractRpcClientProxy<T> implements RpcClientProxy<T> {

    private final Map<String, T> cache = Maps.newConcurrentMap();

    @Override
    public T createProxy(final Class<T> service, final String url) {
        cache.putIfAbsent(service.getName(), newProxy(service, new RpcInvocationHandler<>(service, url)));
        return cache.get(service.getName());
    }

    protected abstract T newProxy(final Class<T> service, final RpcInvocationHandler<T> handler);
}
