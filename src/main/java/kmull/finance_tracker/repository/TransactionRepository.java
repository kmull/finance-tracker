package kmull.finance_tracker.repository;

import kmull.finance_tracker.model.Transaction;
import kmull.finance_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    final String BASE = "SELECT t FROM Transaction t ";
    final String BASE_QUERY = BASE + "WHERE t.user = :user ";

//    List<Transaction> findByUser(User user);

    @Query(BASE_QUERY)
    List<Transaction> findByUser(@Param("user") User user);

    @Query(BASE_QUERY + "AND t.amount > :amount")
    List<Transaction> findByUserAndAmountGreaterThan(User user, BigDecimal amount);

    @Query(BASE_QUERY + "AND t.category = :category")
    List<Transaction> findByUserAndCategory(@Param("user") User user,
                                            @Param("category") String category);
    @Query(BASE + "JOIN FETCH t.user WHERE t.user = :user")
    List<Transaction> findByUserWithUser(@Param("user") User user);

    long deleteTransactionById(Long id);
}
