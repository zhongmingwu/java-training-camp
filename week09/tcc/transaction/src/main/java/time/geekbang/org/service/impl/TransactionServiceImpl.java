package time.geekbang.org.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import time.geekbang.org.entity.Account;
import time.geekbang.org.service.AccountService;
import time.geekbang.org.service.TransactionService;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    final private AccountService accountService;

    @Autowired(required = false)
    public TransactionServiceImpl(AccountService accountService1) {
        this.accountService = accountService1;
    }

    @Override
    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cancelOrderStatus")
    public void transaction() {
        transactionA();
        transactionB();
    }

    private void transactionA() {
        log.info("transactionA");
        Account account = new Account();
        account.setId(1L);
        account.setUs_wallet(-1L);
        account.setCny_wallet(7L);
        accountService.pay(account);
    }

    private void transactionB() {
        log.info("transactionB");
        Account account = new Account();
        account.setId(2L);
        account.setUs_wallet(1L);
        account.setCny_wallet(-7L);
        accountService.pay(account);
    }

    public void confirmOrderStatus() {
        log.info("confirmOrderStatus");
    }

    public void cancelOrderStatus() {
        log.info("cancelOrderStatus");
    }
}
