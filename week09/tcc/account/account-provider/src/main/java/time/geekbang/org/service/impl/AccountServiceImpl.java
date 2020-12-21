package time.geekbang.org.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import time.geekbang.org.entity.Account;
import time.geekbang.org.mapper.AccountMapper;
import time.geekbang.org.service.AccountService;

@Slf4j
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean pay(Account account) {
        boolean isSuccess = accountMapper.pay(account);
        Account current = accountMapper.queryOne(account);
        log.info("pay, account={}, isSuccess={}, current={}", account, isSuccess, current);
        return isSuccess;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(Account account) {
        log.info("confirm, account={}", account);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Account account) {
        log.info("cancel, account={}", account);
        return true;
    }
}
