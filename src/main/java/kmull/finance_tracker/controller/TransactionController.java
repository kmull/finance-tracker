package kmull.finance_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kmull.finance_tracker.dto.TransactionRequest;
import kmull.finance_tracker.dto.TransactionResponse;
import kmull.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Utwórz transakcję")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transakcja utworzona"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Pobierz wszystkie transakcje")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista transakcji"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findAll(
            @RequestParam(required = false) String sortBy) {
        return ResponseEntity.ok(transactionService.findAll(sortBy));
    }

    @Operation(summary = "Pobierz transakcję po ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transakcja znaleziona"),
            @ApiResponse(responseCode = "404", description = "Transakcja nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @Operation(summary = "Zaktualizuj transakcję")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transakcja zaktualizowana"),
            @ApiResponse(responseCode = "404", description = "Transakcja nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @Operation(summary = "Usuń transakcję")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transakcja usunięta"),
            @ApiResponse(responseCode = "404", description = "Transakcja nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
