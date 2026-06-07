package kmull.finance_tracker.service;

import jakarta.transaction.Transactional;
import kmull.finance_tracker.aspect.ValidateTransaction;
import kmull.finance_tracker.model.User;
import kmull.finance_tracker.strategy.TransactionSortStrategy;
import lombok.RequiredArgsConstructor;
import kmull.finance_tracker.dto.TransactionRequest;
import kmull.finance_tracker.dto.TransactionResponse;
import kmull.finance_tracker.exception.TransactionNotFoundException;
import kmull.finance_tracker.model.Transaction;
import kmull.finance_tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final Map<String, TransactionSortStrategy> sortStrategies;
    private final AuthService authService;

    @Transactional
    @ValidateTransaction
    public TransactionResponse create(TransactionRequest request) {
        User user = authService.getCurrentUser();

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .category(request.category())
                .description(request.description())
                .date(request.date())
                .user(user)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    public TransactionResponse findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return toResponse(transaction);
    }

    @Transactional
    public void delete(Long id) {
//        transactionRepository.deleteById(id);
        long deleted = transactionRepository.deleteTransactionById(id);
        if (deleted == 0) {
            throw new TransactionNotFoundException(id);
        }
    }

    @Transactional
    @ValidateTransaction
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        transaction.setAmount(request.amount());
        transaction.setCategory(request.category());
        transaction.setDescription(request.description());
        transaction.setDate(request.date());

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    public List<TransactionResponse> findAll(String sortBy) {
        User user = authService.getCurrentUser();
        List<Transaction> transactions = transactionRepository.findByUser(user);


        TransactionSortStrategy strategy = sortStrategies.get(sortBy);
        List<Transaction> sorted = strategy != null ? strategy.sort(transactions) : transactions;

        return sorted.stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAmount(),
                t.getCategory(),
                t.getDescription(),
                t.getDate()
        );
    }
}
