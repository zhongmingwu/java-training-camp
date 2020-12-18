package time.geekbang.org.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse {
    private Boolean success;
    private Object result;
    private RpcException rpcException;
}
