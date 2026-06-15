package com.coreglobal.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.coreglobal.account.domain.model.AccountRecord;
import com.coreglobal.account.domain.repository.AccountRepository;
import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.event.BankingEventPublisher;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountApplicationServiceTest {
    @Mock AccountRepository repository;
    @Mock BankingEventPublisher events;
    @InjectMocks AccountApplicationService service;

    @Test
    void executesAccountCommandAndPublishesEvent() {
        when(repository.save(any(AccountRecord.class))).thenAnswer(call -> call.getArgument(0));
        when(events.publish(any(), any(), any())).thenReturn(UUID.randomUUID());

        var response = service.execute(new CommandRequest("ACCOUNT_OPENED", "customer-42", new BigDecimal("2500"), Map.of("product", "CORE_CURRENT")));

        assertThat(response.status()).isEqualTo("ACCEPTED");
        assertThat(response.topic()).isEqualTo("account-updated");
        verify(repository).save(any(AccountRecord.class));
        verify(events).publish(any(), any(), any());
    }
}

