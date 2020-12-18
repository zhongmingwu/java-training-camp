package time.geekbang.org;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import time.geekbang.org.netty.server.RpcNettyServer;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
public class RpcServerApplication implements ApplicationRunner {

    private final RpcNettyServer rpcNettyServer;

    @Override
    public void run(ApplicationArguments args) {
        try {
            rpcNettyServer.run();
        } catch (Exception e) {
            log.error("server fail", e);
        } finally {
            rpcNettyServer.destroy();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
    }

    // output
    //  2020-12-19 00:32:17.465  INFO 57790 --- [           main] t.g.org.netty.server.RpcNettyServer      : listen on port 8080
    //  2020-12-19 00:32:23.337  INFO 57790 --- [ntLoopGroup-3-1] t.g.org.netty.server.RpcServerHandler    : invoke, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findById, args=[0]), rpcResponse=RpcResponse(success=true, result={"@type":"time.geekbang.org.bean.Order","id":1608309143324}, rpcException=null)
    //  2020-12-19 00:32:23.558  INFO 57790 --- [ntLoopGroup-3-2] t.g.org.netty.server.RpcServerHandler    : invoke, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findWithException, args=[]), rpcResponse=RpcResponse(success=false, result=null, rpcException=RpcException(message=server timestamp : 1608309143557))
    //  2020-12-19 00:32:23.575  INFO 57790 --- [ntLoopGroup-3-3] t.g.org.netty.server.RpcServerHandler    : invoke, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findById, args=[0]), rpcResponse=RpcResponse(success=true, result={"@type":"time.geekbang.org.bean.Order","id":1608309143575}, rpcException=null)
    //  2020-12-19 00:32:23.651  INFO 57790 --- [ntLoopGroup-3-4] t.g.org.netty.server.RpcServerHandler    : invoke, rpcRequest=RpcRequest(service=time.geekbang.org.service.OrderService, method=findById, args=[0]), rpcResponse=RpcResponse(success=true, result={"@type":"time.geekbang.org.bean.Order","id":1608309143651}, rpcException=null)
}
