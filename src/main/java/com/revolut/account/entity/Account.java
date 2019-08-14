package com.revolut.account.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Entity
@Table(name="ACCOUNT")
public class Account {

    @Id
    @Column(name="ID")
    public int id;
    @Column(name="NAME")
    public String name;
    @Column(name="BALANCE")
    public AtomicReference<BigDecimal> balance;
    @Column(name="DATE")
    private String transactionDate;

    public Account() {
        // TODO Auto-generated constructor stub
    }

    public Account(int id, String name, AtomicReference<BigDecimal> balance, String transactionDate) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account)) {
            return false;
        }
        Account account = (Account) obj;
        return new EqualsBuilder().append(this.id, account.id).append(this.name, account.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ID: ", this.id).append("Name: ", this.name).append("Balance: ", this.balance).toString();
    }

}
