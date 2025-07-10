package com.project.EventPlanner.features.event.domain.service;

import com.project.EventPlanner.features.event.domain.Mapper.CategoryMapper;
import com.project.EventPlanner.features.event.domain.dto.CategoryDto;
import com.project.EventPlanner.features.event.domain.model.EventCategory;
import com.project.EventPlanner.features.event.domain.repository.EventCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getAllCategories() {
        return categoryMapper.toDTOList(eventCategoryRepository.findAll());
    }
    public CategoryDto create(CategoryDto categoryDto) {
        EventCategory category = categoryMapper.ToEntity(categoryDto);
        category = eventCategoryRepository.save(category);
        return categoryMapper.ToDto(category);
    }
    public CategoryDto findById(Long id) {
        EventCategory category = eventCategoryRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Category not found"));
       return categoryMapper.ToDto(category);
    }
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        EventCategory existing = eventCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));

        existing.setName(dto.getName()); // only one field
        EventCategory updated = eventCategoryRepository.save(existing);

        return categoryMapper.ToDto(updated);
    }
    public void deleteCategory(Long id) {
        if (!eventCategoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id " + id);
        }
        eventCategoryRepository.deleteById(id);
    }
}
