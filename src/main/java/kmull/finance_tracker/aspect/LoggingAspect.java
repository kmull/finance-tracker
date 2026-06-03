package kmull.finance_tracker.aspect;

import kmull.finance_tracker.dto.TransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Value("${transaction.max-amount}")
    private double maxAmount;

    // Pointcut — wszystkie metody we wszystkich klasach w pakiecie service
    @Pointcut("execution(* kmull.finance_tracker.service.*.*(..)) " +
            "&& !execution(* kmull.finance_tracker.service.JwtService.*(..))")
    public void serviceMethods() {
    }

    // Before — loguj przed wywołaniem metody
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("- Wywołanie: {}.{}() | argumenty: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // AfterReturning — loguj po pomyślnym zakończeniu
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("✅ Zakończono: {}.{}() | wynik: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        log.error("❌ Błąd w: {}.{}() | wyjątek: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }

    // Around — pomiar czasu wykonania
    @Around("serviceMethods()")
    public Object logExectutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.info("⏱️  {}.{}() wykonano w {} ms",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                duration);

        return result;
    }

    @Before("@annotation(validateTransaction)")
    public void validateTransaction(JoinPoint joinPoint, ValidateTransaction validateTransaction) {

        // jeśli adnotacja ma własną wartość — użyj jej, inaczej weź z properties
        double limit = validateTransaction.maxAmount() > 0
                ? validateTransaction.maxAmount()
                : maxAmount;

        Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof TransactionRequest)
                .map(arg -> (TransactionRequest) arg)
                .forEach(request -> {
                    if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
                        log.error("❌ Kwota musi być większa od zera");
                        throw new IllegalArgumentException("❌ Kwota musi być większa od zera");
                    }
                    if (request.amount().compareTo(BigDecimal.valueOf(limit)) > 0) {
                        log.error("❌ Kwota przekracza limit: {} zł", limit);
                        throw new IllegalArgumentException("❌ Kwota przekracza limit " + limit + " zł");
                    }
                });
        log.info("✅ Walidacja AOP przeszła dla: {}", joinPoint.getSignature().getName());
    }

}
