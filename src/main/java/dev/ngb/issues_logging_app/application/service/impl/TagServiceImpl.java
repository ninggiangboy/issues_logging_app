package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.dto.tag.TagCreateRequest;
import dev.ngb.issues_logging_app.application.dto.tag.TagItemResponse;
import dev.ngb.issues_logging_app.application.exception.DuplicationException;
import dev.ngb.issues_logging_app.application.exception.ValidationException;
import dev.ngb.issues_logging_app.application.mapper.TagMapper;
import dev.ngb.issues_logging_app.application.service.TagService;
import dev.ngb.issues_logging_app.application.validator.ValidatorFactory;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import dev.ngb.issues_logging_app.domain.entity.Tag;
import dev.ngb.issues_logging_app.domain.repository.TagRepository;
import dev.ngb.issues_logging_app.infrastructure.cache.CacheConstant;
import dev.ngb.issues_logging_app.infrastructure.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper = TagMapper.INSTANCE;
    private final Cache cache;
    private final ValidatorFactory validatorFactory;

    public TagServiceImpl(TagRepository tagRepository, CacheFactory cacheFactory, ValidatorFactory validatorFactory) {
        this.tagRepository = tagRepository;
        this.cache = cacheFactory.getCache(CacheConstant.TAGS_CACHE_NAME);
        this.validatorFactory = validatorFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagItemResponse> getAllTags() {
        return (List<TagItemResponse>) Optional.ofNullable(cache
                        .get(CacheConstant.ALL_TAGS_KEY_NAME, List.class))
                .orElseGet(() -> {
                    List<TagItemResponse> tagsFromDb = tagRepository.findAll()
                            .stream()
                            .map(tagMapper::mapToResponse)
                            .toList();
                    cache.put(CacheConstant.ALL_TAGS_KEY_NAME, tagsFromDb);
                    return tagsFromDb;
                });
    }

    @Override
    public TagItemResponse createTag(TagCreateRequest request) {
        validateRequest(request);
        ensureTagNameIsUnique(request.name());
        Tag tagEntity = tagMapper.mapToEntity(request);
        Tag savedTag = tagRepository.save(tagEntity);
        cache.evict(CacheConstant.ALL_TAGS_KEY_NAME);
        return tagMapper.mapToResponse(savedTag);
    }

    private void validateRequest(TagCreateRequest request) {
        ValidatorChain<TagCreateRequest> validator = validatorFactory.getValidator(TagCreateRequest.class);
        if (validator.isNotValid(request)) {
            throw new ValidationException(validator.getErrors(), TagCreateRequest.class);
        }
    }

    private void ensureTagNameIsUnique(String tagName) {
        if (tagRepository.existsByName(tagName)) {
            throw new DuplicationException("Tag with name '" + tagName + "' already exists");
        }
    }
}
