package com.project.EventPlanner.features.event.api;

import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.service.EventCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final EventCategoryService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id,
                                                   @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(service.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
