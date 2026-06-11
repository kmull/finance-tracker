package kmull.finance_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kmull.finance_tracker.dto.BudgetRequest;
import kmull.finance_tracker.dto.BudgetResponse;
import kmull.finance_tracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@SecurityRequirement(name = "barerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    @Operation(summary = "Utwórz budżet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Budżet utworzony"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane"),
            @ApiResponse(responseCode = "$01", description = "Brak autoryzacji")
    })
    @PostMapping
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.create(request));
    }

    @Operation(summary = "Zaktualizuj budżet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budżet zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Budżet nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request
    ) {
        return ResponseEntity.ok(budgetService.update(id, request));
    }

    @Operation(summary = "Pobierz wszystkie budżety")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista budżetów"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> findAll() {
        return ResponseEntity.ok(budgetService.findAll());
    }

    @Operation(summary = "Pobierz budżet po ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Budżet znaleziony"),
            @ApiResponse(responseCode = "404", description = "Budżet nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.findById(id));
    }

    @Operation(summary = "Usuń budżet")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Budżet usunięty"),
            @ApiResponse(responseCode = "404", description = "Budżet nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
