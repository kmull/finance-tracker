package kmull.finance_tracker.mapper;


import kmull.finance_tracker.dto.TransactionResponse;
import kmull.finance_tracker.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "category",
            expression = "java(kmull.finance_tracker.enums.CategoryType.valueOf(transaction.getCategory()))")
    @Mapping(target = "categoryDescription",
            expression = "java(kmull.finance_tracker.enums.CategoryType.valueOf(transaction.getCategory()).getDescription())")
    TransactionResponse toResponse(Transaction transaction);
}
