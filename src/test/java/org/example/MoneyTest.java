package org.example;

import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyWithValidAmount() {
        Money money = new Money(BigDecimal.valueOf(100));
        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThatThrownBy(() -> new Money(null))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Money can't be Null");
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-10)))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Money can't be below zero");
    }

    @Test
    void zeroShouldReturnZeroMoney() {
        Money zero = Money.zero();
        assertThat(zero.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void addShouldSumAmounts() {
        Money m1 = new Money(BigDecimal.valueOf(100));
        Money m2 = new Money(BigDecimal.valueOf(50));
        Money result = m1.add(m2);
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    void subtractShouldSubtractAmounts() {
        Money m1 = new Money(BigDecimal.valueOf(100));
        Money m2 = new Money(BigDecimal.valueOf(30));
        Money result = m1.subtract(m2);
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(70));
    }

    @Test
    void ofShouldCreateMoneyFromLong() {
        Money money = Money.of(500);
        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.valueOf(500));
    }

    @Test
    void isZeroOrNegativeShouldReturnTrueForZero() {
        Money zero = Money.zero();
        assertThat(zero.isZeroOrNegative()).isTrue();
    }

    @Test
    void isZeroOrNegativeShouldReturnFalseForPositive() {
        Money positive = new Money(BigDecimal.valueOf(10));
        assertThat(positive.isZeroOrNegative()).isFalse();
    }

    @Test
    void getAmountInCentsShouldConvertCorrectly() {
        Money money = new Money(BigDecimal.valueOf(123.45));
        assertThat(money.getAmountInCents()).isEqualTo(12345L);
    }
}