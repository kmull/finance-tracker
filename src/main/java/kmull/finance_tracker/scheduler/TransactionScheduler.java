package kmull.finance_tracker.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionScheduler {

    private final StatisticsService statisticsService;

    @Scheduled(cron = "${scheduler.transaction-stats.cron:0 0 * * * *}")
    public void logStatistics() {
        log.info("⏰ Scheduler: {}", LocalDateTime.now());
        statisticsService.generateStatistics(); // wywołanie przez proxy — @Async działa!
    }
}
