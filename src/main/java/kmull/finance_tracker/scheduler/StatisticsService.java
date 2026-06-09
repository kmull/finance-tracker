package kmull.finance_tracker.scheduler;

import kmull.finance_tracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final TransactionRepository transactionRepository;

    @Async
    public void generateStatistics() {
        log.info("📊 [ASYNC - wątek: {}] Liczba transakcji: {}",
                Thread.currentThread().getName(),
                transactionRepository.count());
    }
}