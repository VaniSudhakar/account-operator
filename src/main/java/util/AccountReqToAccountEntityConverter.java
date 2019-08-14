package util;

import com.google.inject.Singleton;
import com.revolut.account.dto.AccountInputRequest;
import com.revolut.account.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class AccountReqToAccountEntityConverter implements ObjectConverter<AccountInputRequest, Account> {
    @Override
    public Account apply(AccountInputRequest accountReq) {
        return new Account(accountReq.getId(), accountReq.getName(), new AtomicReference<BigDecimal>(accountReq.getAmount()), getCurrentDate());
    }

    private String getCurrentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        return now.format(formatter);
    }
}
