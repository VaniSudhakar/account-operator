package util;

import com.google.inject.Singleton;
import com.revolut.account.dto.AccountDto;
import com.revolut.account.dto.AccountInputRequest;
import com.revolut.account.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class AccountToAccountDtoConverter implements ObjectConverter<Account, AccountDto> {
    @Override
    public AccountDto apply(Account account) {
        return new AccountDto(account.getId(), account.getName(),account.getBalance().get());
    }
}
