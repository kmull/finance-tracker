package kmull.finance_tracker.service;

import lombok.RequiredArgsConstructor;
import kmull.finance_tracker.dto.TransactionRequest;
import kmull.finance_tracker.dto.TransactionResponse;
import kmull.finance_tracker.exception.TransactionNotFoundException;
import kmull.finance_tracker.model.Transaction;
import kmull.finance_tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionResponse create(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .category(request.category())
                .description(request.description())
                .date(request.date())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    public List<TransactionResponse> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return toResponse(transaction);
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
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
