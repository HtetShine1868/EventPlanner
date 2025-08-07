package com.project.EventPlanner.features.event.api;

import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.service.EventCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final EventCategoryService service;


    @Operation(summary = "Create category", description = "Admin creates a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category created")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    @Operation(summary = "Get all categories", description = "Returns all event categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved")
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @Operation(summary = "Update category", description = "Admin updates a category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id,
                                                   @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(service.updateCategory(id, dto));
    }


    @Operation(summary = "Delete category", description = "Admin deletes a category")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
