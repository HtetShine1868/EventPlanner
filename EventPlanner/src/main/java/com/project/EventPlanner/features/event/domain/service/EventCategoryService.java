package com.project.EventPlanner.features.event.domain.service;

import com.project.EventPlanner.features.event.domain.Mapper.CategoryMapper;
import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import com.project.EventPlanner.features.event.domain.repository.EventCategoryRepository;
import com.project.EventPlanner.features.event.domain.Mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventCategoryService {

    private final EventCategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryDto createCategory(CategoryDto dto) {
        EventCategory category = mapper.ToEntity(dto);
        return mapper.ToDto(repository.save(category));
    }

    public List<CategoryDto> getAllCategories() {
        return mapper.ToDtoList(repository.findAll());
    }

    public CategoryDto updateCategory(Long id,CategoryDto dto) {
        EventCategory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setName(dto.getName());
        return mapper.ToDto(repository.save(existing));
    }

    public void deleteCategory(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        repository.deleteById(id);
    }
}
