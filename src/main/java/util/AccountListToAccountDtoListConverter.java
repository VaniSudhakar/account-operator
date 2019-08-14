package util;

import com.revolut.account.dto.AccountDto;
import com.revolut.account.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountListToAccountDtoListConverter implements ObjectConverter<List<Account>, List<AccountDto>> {

    @Override
    public List<AccountDto> apply(List<Account> accounts) {
        List<AccountDto> accountDtos = new ArrayList<>();
        for (Account acc : accounts) {
            accountDtos.add(new AccountDto(acc.getId(), acc.getName(), acc.getBalance().get()));
        }
        return accountDtos;
    }

}
