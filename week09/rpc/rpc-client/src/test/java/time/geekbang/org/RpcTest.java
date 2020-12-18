package time.geekbang.org;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import time.geekbang.org.bean.Order;
import time.geekbang.org.proxy.base.RpcClientProxy;
import time.geekbang.org.proxy.factory.ByteBuddyProxy;
import time.geekbang.org.proxy.factory.CglibProxy;
import time.geekbang.org.proxy.factory.JdkProxy;
import time.geekbang.org.service.OrderService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Slf4j
public class RpcTest {

    private static final String HOST = System.getProperty("host", "localhost");
    private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    private static final String URL = String.format("http://%s:%d", HOST, PORT);

    @Test
    public void jdkProxyTest() {
        RpcClientProxy<OrderService> rpcClientProxy = new JdkProxy<>();
        OrderService orderService = rpcClientProxy.createProxy(OrderService.class, URL);
        Order order = orderService.findById(0);
        assertNotNull(order);
        log.info("{}", order);

        // output
        //  00:32:23.361 [main] INFO time.geekbang.org.proxy.base.RpcInvocationHandler - request success, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findById, args=[0]), rpcResponse=RpcResponse(success=true, result={"@type":"time.geekbang.org.bean.Order","id":1608309143324}, rpcException=null)
        //  00:32:23.362 [main] INFO time.geekbang.org.RpcTest - Order(id=1608309143324)
    }

    @Test
    public void cglibProxyTest() {
        RpcClientProxy<OrderService> rpcClientProxy = new CglibProxy<>();
        OrderService orderService = rpcClientProxy.createProxy(OrderService.class, URL);
        Order order = orderService.findById(0);
        assertNotNull(order);
        log.info("{}", order);

        // output
        //  00:32:23.651 [main] INFO time.geekbang.org.proxy.base.RpcInvocationHandler - request success, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findById, args=[0]), rpcResponse=RpcResponse(success=true, result={"@type":"time.geekbang.org.bean.Order","id":1608309143651}, rpcException=null)
        //  00:32:23.652 [main] INFO time.geekbang.org.RpcTest - Order(id=1608309143651)
    }

    @Test
    public void byteBuddyProxyTest() {
        RpcClientProxy<OrderService> rpcClientProxy = new ByteBuddyProxy<>();
        OrderService orderService = rpcClientProxy.createProxy(OrderService.class, URL);
        Order order = orderService.findById(0);
        assertNotNull(order);
        log.info("{}", order);

        // output
        //  00:32:23.576 [main] INFO time.geekbang.org.proxy.base.RpcInvocationHandler - request success, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findById, args=[0]), rpcResponse=RpcResponse(success=true, result={"@type":"time.geekbang.org.bean.Order","id":1608309143575}, rpcException=null)
        //  00:32:23.577 [main] INFO time.geekbang.org.RpcTest - Order(id=1608309143575)
    }

    @Test
    public void exceptionTest() {
        RpcClientProxy<OrderService> rpcClientProxy = new ByteBuddyProxy<>();
        OrderService orderService = rpcClientProxy.createProxy(OrderService.class, URL);
        Order order = orderService.findWithException();
        assertNull(order);

        // output
        //  00:32:23.566 [main] ERROR time.geekbang.org.proxy.base.RpcInvocationHandler - request fail, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findWithException, args=[]), rpcResponse=RpcResponse(success=false, result=null, rpcException=RpcException(message=server timestamp : 1608309143557))
    }
}
