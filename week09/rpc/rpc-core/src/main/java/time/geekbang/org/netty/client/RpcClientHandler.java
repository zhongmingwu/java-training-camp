package time.geekbang.org.netty.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import time.geekbang.org.meta.RpcResponse;
import time.geekbang.org.netty.packet.RpcPacket;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcPacket> {

    private final CountDownLatch latch;
    private RpcResponse response;

    RpcClientHandler(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcPacket msg) {
        response = JSON.parseObject(new String(msg.getPayload(), CharsetUtil.UTF_8), RpcResponse.class);
        latch.countDown();
    }

    public RpcResponse waitForResponse() throws InterruptedException {
        latch.await();
        return response;
    }
}
