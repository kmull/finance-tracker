package kmull.finance_tracker.service;

import jakarta.transaction.Transactional;
import kmull.finance_tracker.dto.BudgetRequest;
import kmull.finance_tracker.dto.BudgetResponse;
import kmull.finance_tracker.exception.BudgetNotFoundException;
import kmull.finance_tracker.mapper.BudgetMapper;
import kmull.finance_tracker.model.Budget;
import kmull.finance_tracker.model.Transaction;
import kmull.finance_tracker.model.User;
import kmull.finance_tracker.repository.BudgetRepository;
import kmull.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetMapper budgetMapper;
    private final AuthService authService;

    @Transactional
    public BudgetResponse create(BudgetRequest request) {
        User user = authService.getCurrentUser();

        Budget budget = budgetMapper.toEntity(request);
        budget.setUser(user);

        Budget saved = budgetRepository.save(budget);
        return toResponseWithStats(saved);
    }

    @Transactional
    public List<BudgetResponse> findAll() {
        User user = authService.getCurrentUser();

        return budgetRepository.findByUser(user)
                .stream()
                .map(this::toResponseWithStats)
                .toList();
    }

    @Transactional
    public BudgetResponse findById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
        return toResponseWithStats(budget);
    }

    @Transactional
    public void delete(Long id) {
        budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
        budgetRepository.deleteById(id);
    }

    @Transactional
    public BudgetResponse update(Long id, BudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));

        budget.setCategory(request.category());
        budget.setLimit(request.limit());
        budget.setMonth(request.month());

        Budget saved = budgetRepository.save(budget);
        return toResponseWithStats(saved);
    }

    private BudgetResponse toResponseWithStats(Budget budget) {
        List<Transaction> transactions = transactionRepository.findByUser(budget.getUser());

        BigDecimal totalSpent = transactions.stream()
                .filter(t -> t.getCategory().equals(budget.getCategory()))
                .filter(t -> t.getDate().getYear() == budget.getMonth().getYear()
                        && t.getDate().getMonth() == budget.getMonth().getMonth())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BudgetResponse response = budgetMapper.toResponse(budget);

        return new BudgetResponse(
                response.id(),
                response.category(),
                response.categoryDescription(),
                response.limit(),
                response.month(),
                totalSpent,
                totalSpent.compareTo(budget.getLimit()) > 0
        );
    }

}
