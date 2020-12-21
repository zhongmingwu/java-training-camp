package time.geekbang.org.service;

import org.dromara.hmily.annotation.Hmily;
import time.geekbang.org.entity.Account;

public interface AccountService {

    @Hmily
    boolean pay(Account account);
}
