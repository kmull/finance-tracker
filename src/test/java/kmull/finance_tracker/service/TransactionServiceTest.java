package kmull.finance_tracker.service;

import kmull.finance_tracker.dto.TransactionRequest;
import kmull.finance_tracker.dto.TransactionResponse;
import kmull.finance_tracker.exception.TransactionNotFoundException;
import kmull.finance_tracker.model.Transaction;
import kmull.finance_tracker.model.User;
import kmull.finance_tracker.repository.TransactionRepository;
import kmull.finance_tracker.strategy.TransactionSortStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthService authService;

    @Mock
    private Map<String, TransactionSortStrategy> sortStrategies;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal("150.00"))
                .category("Jedzenie")
                .description("Biedronka")
                .date(LocalDate.of(2026, 5, 1))
                .build();
    }

    @Test
    void findById_ShouldReturnResponse_whenTransactionExist() {
        // given
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // when
        TransactionResponse result = transactionService.findById(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.amount()).isEqualTo(new BigDecimal("150.00"));
        assertThat(result.category()).isEqualTo("Jedzenie");
    }

    @Test
    void findById_shouldThrowException_whenTransactionNotFound() {
        // given
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> transactionService.findById(99L))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void create_shouldReturnResponse_whenValidRequest() {

        // given
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("150.00"),
                "Jedzenie",
                "Biedronka",
                LocalDate.of(2026, 5, 1)
        );

        User user = User.builder()
                .id(1L)
                .email("krzys@gmail.com")
                .password("password")
                .name("Krzysztof")
                .build();

        when(authService.getCurrentUser()).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // when
        TransactionResponse result = transactionService.create(request);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.amount()).isEqualTo(new BigDecimal("150.00"));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void update_shouldReturnUpdatedResponse_whenTransactionExists() {
        // given
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("200.00"),
                "Transport",
                "Paliwo",
                LocalDate.of(2026, 5, 2)
        );

        Transaction updated = Transaction.builder()
                .id(1L)
                .amount(new BigDecimal("200.00"))
                .category("Transport")
                .description("Paliwo")
                .date(LocalDate.of(2026, 5, 2))
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updated);

        // when
        TransactionResponse result = transactionService.update(1L, request);

        // then
        assertThat(result.amount()).isEqualTo("200.00");
        assertThat(result.category()).isEqualTo("Transport");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void update_shouldThrowException_whenTransactionNotFound() {
        // given
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("200.00"),
                "Transport",
                "Paliwo",
                LocalDate.of(2026, 5, 2)
        );

        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> transactionService.update(99L, request))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void delete_shouldDeleteTransaction_whenTransactionExists() {
        // given
        when(transactionRepository.deleteTransactionById(1L)).thenReturn(1L);

        // when
        transactionService.delete(1L);

        // then
        verify(transactionRepository).deleteTransactionById(1L);
    }

    @Test
    void delete_shouldThrowException_whenTransactionNotFound() {
        // given
        when(transactionRepository.deleteTransactionById(99L)).thenReturn(0L);

        // then
        assertThatThrownBy(() -> transactionService.delete(99L))
                .isInstanceOf(TransactionNotFoundException.class);

        verify(transactionRepository, times(1)).deleteTransactionById(99L);
    }

}
