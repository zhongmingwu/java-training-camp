package time.geekbang.org.netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import time.geekbang.org.meta.RpcException;
import time.geekbang.org.meta.RpcRequest;
import time.geekbang.org.meta.RpcResponse;
import time.geekbang.org.netty.packet.RpcPacket;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcPacket> {

    private final ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcPacket msg) throws Exception {
        ctx.writeAndFlush(toRpcPacket(invoke(buildRpcRequest(msg)))).sync();
    }

    private RpcRequest buildRpcRequest(RpcPacket rpcPacket) {
        return JSON.parseObject(new String(rpcPacket.getPayload(), CharsetUtil.UTF_8), RpcRequest.class);
    }

    private RpcResponse invoke(RpcRequest rpcRequest) {
        RpcResponse rpcResponse = new RpcResponse();
        try {
            Object serviceBean = applicationContext.getBean(rpcRequest.getService());
            Optional<Method> methodOpt = tryResolveMethod(serviceBean.getClass(), rpcRequest.getMethod());
            if (!methodOpt.isPresent()) {
                throw new RpcException("method can not be resolved, rpcRequest=" + rpcRequest);
            }
            Object result = methodOpt.get().invoke(serviceBean, rpcRequest.getArgs());
            rpcResponse.setSuccess(true);
            rpcResponse.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
        } catch (Exception e) {
            rpcResponse.setSuccess(false);
            rpcResponse.setRpcException(new RpcException(e.getCause().getMessage()));
        }
        log.info("invoke, rpcRequest={}, rpcResponse={}", rpcRequest, rpcResponse);
        return rpcResponse;
    }

    private RpcPacket toRpcPacket(RpcResponse rpcResponse) {
        byte[] payload = JSON.toJSONString(rpcResponse).getBytes(CharsetUtil.UTF_8);
        return RpcPacket.builder().len(payload.length).payload(payload).build();
    }

    private Optional<Method> tryResolveMethod(Class<?> klass, String method) {
        return Arrays.stream(klass.getMethods()).filter(m -> method.equals(m.getName())).findFirst();
    }
}
