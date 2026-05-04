package org.example.domain.common;

import org.example.domain.common.exception.DomainValidationException;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {
    public Money {
        if (amount == null) {
            throw new DomainValidationException("Money can't be Null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainValidationException("Money can't be below zero");
        }
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(amount.subtract(other.amount));
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public boolean isZeroOrNegative() {
        return amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    public Money plus(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public long getAmountInCents() {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
}

