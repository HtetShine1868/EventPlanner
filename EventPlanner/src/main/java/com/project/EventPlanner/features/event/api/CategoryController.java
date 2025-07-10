package com.project.EventPlanner.features.event.api;

import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import com.project.EventPlanner.features.event.domain.service.EventCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final EventCategoryService eventCategoryService;

    @PutMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(eventCategoryService.create(categoryDto));
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(eventCategoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(eventCategoryService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
                                                      @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(eventCategoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        eventCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
