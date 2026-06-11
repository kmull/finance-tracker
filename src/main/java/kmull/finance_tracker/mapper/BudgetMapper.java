package kmull.finance_tracker.mapper;

import kmull.finance_tracker.dto.BudgetRequest;
import kmull.finance_tracker.dto.BudgetResponse;
import kmull.finance_tracker.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "categoryDescription",
            expression = "java(kmull.finance_tracker.enums.CategoryType.valueOf(budget.getCategory()).getDescription())")
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "exceeded", ignore = true)
    BudgetResponse toResponse(Budget budget);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Budget toEntity(BudgetRequest request);
}
