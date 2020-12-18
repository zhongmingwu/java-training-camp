package time.geekbang.org.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import time.geekbang.org.netty.packet.RpcPacket;

public class RpcEncoder extends MessageToByteEncoder<RpcPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcPacket msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getPayload());
    }
}
