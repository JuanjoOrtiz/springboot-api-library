package com.springboot.api.library.controllers;

import com.springboot.api.library.dtos.MemberRequestDTO;
import com.springboot.api.library.dtos.MemberResponseDTO;
import com.springboot.api.library.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "Get all members", description = "Retrieve a paginated list of all members")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of members retrieve successfully"),
            @ApiResponse(responseCode = "204", description = "members not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @GetMapping
    public ResponseEntity<Page<?>> findAllMembers(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        try {
            log.info("Fetching all members with pagination: page={}, size={}",
                    pageable.getPageNumber(), pageable.getPageSize());

            Page<MemberResponseDTO> members = memberService.findAll(pageable);
            if (members.isEmpty()) {
                log.info("Members not found");
                return ResponseEntity.noContent().build();
            }
            log.info("Members found: {}", members.getTotalElements());
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            log.error("Error occurred while fetching members", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Get member by ID", description = "Retrieve a member by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member found and returned"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findMemberById(@PathVariable Long id) {
        try {
            log.info("Fetching member by Id: {}", id);

            return memberService.findById(id)
                    .map(member -> {
                        log.info("Member found with id: {}", id);
                        return ResponseEntity.ok(member);
                    })
                    .orElseGet(() -> {
                        log.info("Member not found with id: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error occurred while fetching member with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Create a new member", description = "Create a new member entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "member created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<MemberResponseDTO> createMember(@Valid @RequestBody MemberRequestDTO memberRequestDto) {
        try {
            log.info("Creating new member: {}", memberRequestDto);
            MemberResponseDTO createdMember = memberService.create(memberRequestDto);
            log.info("Member created successfully with id: {}", createdMember.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
        } catch (Exception e) {
            log.error("Error occurred while creating member", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Update a member", description = "Update an existing member by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequestDTO memberRequestDto) {
        try {
            log.info("Updating member with id: {}", id);
            MemberResponseDTO updatedMember = memberService.update(memberRequestDto, id);
            log.info("member updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedMember);
        } catch (EntityNotFoundException e) {
            log.warn("member not found for update with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while updating member with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @Operation(summary = "Delete a member", description = "Delete a member by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        try {
            log.info("Deleting member with id: {}", id);
            if (memberService.delete(id)) {
                log.info("member deleted successfully with id: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                log.info("member not found for deletion with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting member with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
