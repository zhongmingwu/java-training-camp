package time.geekbang.org.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import time.geekbang.org.netty.codec.RpcDecoder;
import time.geekbang.org.netty.codec.RpcEncoder;

@Slf4j
@Component
public class RpcNettyServer {

    private final ApplicationContext context;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public RpcNettyServer(ApplicationContext context) {
        this.context = context;
    }

    public void run() throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(RpcEncoder.class.getSimpleName(), new RpcEncoder());
                        pipeline.addLast(RpcDecoder.class.getSimpleName(), new RpcDecoder());
                        pipeline.addLast(RpcServerHandler.class.getSimpleName(), new RpcServerHandler(context));
                    }
                });

        int port = Integer.parseInt(System.getProperty("port", "8080"));
        Channel channel = serverBootstrap.bind(port).sync().channel();
        log.info("listen on port {}", port);
        channel.closeFuture().sync();
    }

    public void destroy() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
