package com.coreglobal.account.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.coreglobal.account.domain.model.AccountRecord;
import com.coreglobal.account.domain.repository.AccountRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired AccountRepository repository;

    @Test
    void filtersAccountsByOwner() {
        repository.save(new AccountRecord("ACC-100", "avery", "ACTIVE", new BigDecimal("1200"), "{}"));
        assertThat(repository.findByOwnerIdContainingIgnoreCase("AVE", PageRequest.of(0, 10))).hasSize(1);
    }
}

