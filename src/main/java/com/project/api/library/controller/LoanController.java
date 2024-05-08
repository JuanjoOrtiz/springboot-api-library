package com.project.api.library.controller;

import com.project.api.library.dto.LoanDTO;
import com.project.api.library.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/loans")
    public ResponseEntity<Page<LoanDTO>> findAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ){
        Page<LoanDTO> loanDTO= loanService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok().body(loanDTO);

    }

    @GetMapping("/loan/{id}")
    public Optional<LoanDTO> findById(@PathVariable Long id){
        return loanService.findById(id);
    }

    @PostMapping("/loan")
    public ResponseEntity<LoanDTO> save(@RequestBody LoanDTO loanDTO) {
        LoanDTO savedLoanDTO = loanService.save(loanDTO);
        return ResponseEntity.ok().body(savedLoanDTO);

    }

    @PutMapping("/loan/{id}")
    public ResponseEntity<LoanDTO> update(@PathVariable Long id, @RequestBody LoanDTO loanDTO){
        loanDTO = loanService.update(id, loanDTO);
        return ResponseEntity.ok().body(loanDTO);
    }

    @DeleteMapping("/loan/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        loanService.delete(id);
        return ResponseEntity.ok().build();
    }



}
