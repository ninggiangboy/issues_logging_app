package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.dto.category.CategoryCreateRequest;
import dev.ngb.issues_logging_app.application.dto.category.CategoryItemResponse;
import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.application.exception.DuplicationException;
import dev.ngb.issues_logging_app.application.exception.ValidationException;
import dev.ngb.issues_logging_app.application.mapper.CategoryMapper;
import dev.ngb.issues_logging_app.application.service.CategoryService;
import dev.ngb.issues_logging_app.application.validator.ValidatorFactory;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import dev.ngb.issues_logging_app.domain.repository.CategoryRepository;
import dev.ngb.issues_logging_app.infrastructure.cache.CacheConstant;
import dev.ngb.issues_logging_app.infrastructure.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;
    private final ValidatorFactory validatorFactory;
    private final Cache cache;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ValidatorFactory validatorFactory, CacheFactory cacheFactory) {
        this.categoryRepository = categoryRepository;
        this.validatorFactory = validatorFactory;
        this.cache = cacheFactory.getCache(CacheConstant.CATEGORIES_CACHE_NAME);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CategoryItemResponse> getAllCategories() {
        return (List<CategoryItemResponse>) Optional.ofNullable(cache
                        .get(CacheConstant.ALL_CATEGORIES_KEY_NAME, List.class))
                .orElseGet(() -> {
                    List<CategoryItemResponse> categoriesFromDb = categoryRepository.findAll()
                            .stream()
                            .map(categoryMapper::mapToResponse)
                            .toList();
                    cache.put(CacheConstant.ALL_CATEGORIES_KEY_NAME, categoriesFromDb);
                    return categoriesFromDb;
                });
    }

    @Override
    public CategoryItemResponse createCategory(CategoryCreateRequest request) {
        validateRequest(request);
        ensureCategoryNameIsUnique(request.name());
        var categoryEntity = categoryMapper.mapToEntity(request);
        var savedCategory = categoryRepository.save(categoryEntity);
        cache.evict(CacheConstant.ALL_CATEGORIES_KEY_NAME);
        return categoryMapper.mapToResponse(savedCategory);
    }

    private void validateRequest(CategoryCreateRequest request) {
        ValidatorChain<CategoryCreateRequest> validator = validatorFactory.getValidator(CategoryCreateRequest.class);
        if (validator.isNotValid(request)) {
            throw new ValidationException(validator.getErrors(), validator.support());
        }
    }

    private void ensureCategoryNameIsUnique(String tagName) {
        if (categoryRepository.existsByName(tagName)) {
            throw new DuplicationException("Category with name '" + tagName + "' already exists");
        }
    }
}
