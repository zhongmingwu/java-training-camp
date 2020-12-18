package time.geekbang.org.netty.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcPacket {
    private int len;
    private byte[] payload;
}
