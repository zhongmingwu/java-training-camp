package time.geekbang.org.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import time.geekbang.org.netty.packet.RpcPacket;

import java.util.List;

@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    private static final int INT_BYTES = 4;

    private volatile int len = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < INT_BYTES) {
            return;
        }

        if (len == 0) {
            len = in.readInt();
        }

        if (in.readableBytes() < len) {
            log.info("wait to receive more bytes, readable={}, expected={}", in.readableBytes(), len);
            return;
        }

        byte[] payload = new byte[len];
        in.readBytes(payload);
        out.add(RpcPacket.builder().len(len).payload(payload).build());

        len = 0;
    }
}
