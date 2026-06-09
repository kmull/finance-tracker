package kmull.finance_tracker.mapper;


import kmull.finance_tracker.dto.TransactionResponse;
import kmull.finance_tracker.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);
}
