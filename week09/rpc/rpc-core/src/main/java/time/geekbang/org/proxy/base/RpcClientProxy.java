package time.geekbang.org.proxy.base;

public interface RpcClientProxy<T> {

    T createProxy(final Class<T> service, final String url);
}
