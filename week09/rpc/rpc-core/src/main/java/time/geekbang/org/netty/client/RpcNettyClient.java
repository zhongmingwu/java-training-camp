package time.geekbang.org.netty.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import time.geekbang.org.meta.RpcRequest;
import time.geekbang.org.meta.RpcResponse;
import time.geekbang.org.netty.codec.RpcDecoder;
import time.geekbang.org.netty.codec.RpcEncoder;
import time.geekbang.org.netty.packet.RpcPacket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class RpcNettyClient {

    private static final RpcNettyClient INSTANCE = new RpcNettyClient();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private RpcNettyClient() {
    }

    public static RpcNettyClient getInstance() {
        return INSTANCE;
    }

    public RpcResponse sendRequest(RpcRequest rpcRequest, String url) throws InterruptedException, URISyntaxException {
        URI uri = new URI(url);
        Channel channel = createChannel(uri.getHost(), uri.getPort());
        RpcClientHandler handler = new RpcClientHandler(new CountDownLatch(1));
        channel.pipeline().replace(RpcClientHandler.class.getSimpleName(), RpcClientHandler.class.getSimpleName(), handler);
        channel.writeAndFlush(toRpcPacket(rpcRequest)).sync();
        return handler.waitForResponse();
    }

    private Channel createChannel(String address, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.AUTO_CLOSE, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(RpcEncoder.class.getSimpleName(), new RpcEncoder());
                        pipeline.addLast(RpcDecoder.class.getSimpleName(), new RpcDecoder());
                        pipeline.addLast(RpcClientHandler.class.getSimpleName(), new RpcClientHandler(new CountDownLatch(1)));
                    }
                });
        return bootstrap.connect(address, port).sync().channel();
    }

    private RpcPacket toRpcPacket(RpcRequest rpcRequest) {
        byte[] payload = JSON.toJSONString(rpcRequest).getBytes(CharsetUtil.UTF_8);
        return RpcPacket.builder().len(payload.length).payload(payload).build();
    }

    public void destroy() {
        workerGroup.shutdownGracefully();
    }
}
