package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.dto.issue.IssueCreateRequest;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchCriteria;
import dev.ngb.issues_logging_app.application.dto.issue.IssueSearchResponse;
import dev.ngb.issues_logging_app.application.exception.ValidationException;
import dev.ngb.issues_logging_app.application.mapper.IssueMapper;
import dev.ngb.issues_logging_app.application.service.IssueService;
import dev.ngb.issues_logging_app.application.validator.ValidatorFactory;
import dev.ngb.issues_logging_app.common.factory.PageFactory;
import dev.ngb.issues_logging_app.common.model.PageData;
import dev.ngb.issues_logging_app.common.util.SecurityUtils;
import dev.ngb.issues_logging_app.common.validator.ValidatorChain;
import dev.ngb.issues_logging_app.domain.entity.*;
import dev.ngb.issues_logging_app.domain.repository.*;
import dev.ngb.issues_logging_app.domain.specification.IssueSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class IssueServiceImpl implements IssueService {

    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper = IssueMapper.INSTANCE;
    private final ValidatorFactory validatorFactory;
    private final ApplicationEventPublisher eventPublisher;

    public IssueServiceImpl(ProjectRepository projectRepository, CategoryRepository categoryRepository, TagRepository tagRepository, UserRepository userRepository, IssueRepository issueRepository, ValidatorFactory validatorFactory, ApplicationEventPublisher eventPublisher) {
        this.projectRepository = projectRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
        this.validatorFactory = validatorFactory;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public PageData<IssueSearchResponse> searchIssues(IssueSearchCriteria criteria, Pageable page) {
        Specification<Issue> issueSpecification = getSearchSpecification(criteria);
        Page<IssueSearchResponse> issuesPage = issueRepository.findAll(issueSpecification, page)
                .map(issueMapper::mapToSearchResponse);
        return PageFactory.createPage(issuesPage);
    }

//    @Override
//    public void createIssue(IssueCreateRequest request) {
//        ValidatorChain<IssueCreateRequest> validator = validatorFactory.getValidator(IssueCreateRequest.class);
//        if (!validator.isNotValid(request)) {
//            throw new ValidationException(validator.getErrors(), validator.support());
//        }
//        User assignee = userRepository.findById(request.assigneeId())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//        Project project = projectRepository.findOneByIdWithMembers(request.projectId())
//                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
//        project.getMembers().stream()
//                .filter(member -> member.getId().equals(assignee.getId()))
//                .findAny()
//                .orElseThrow(() -> new IllegalArgumentException("Assignee is not a member of the project"));
//        project.getMembers().stream()
//                .filter(member -> member.getId().equals(SecurityUtils.getCurrentUserId()))
//                .findAny()
//                .orElseThrow(() -> new IllegalArgumentException("Current user is not a member of the project"));
//        Category category = categoryRepository.findById(request.categoryId())
//                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
//        Set<Tag> tags = tagRepository.findAllByIdIn(request.tagIds());
//        if (tags.size() != request.tagIds().size()) {
//            throw new IllegalArgumentException("Some tags not found");
//        }
//        Issue issue = issueMapper.mapToEntity(request);
//        issue.setCategory(category);
//        issue.getTags().addAll(tags);
//        issue.setReviewAssignee(assignee);
//        issue.setReporter(SecurityUtils.getCurrentUser());
//        issue.setProject(project);
//        Issue savedIssue = issueRepository.save(issue);
//    }

    private Specification<Issue> getSearchSpecification(IssueSearchCriteria criteria) {
        List<Issue.Status> notVisibleStatuses = List.of(Issue.Status.DRAFT, Issue.Status.REVIEWING);

        Specification<Issue> issueSpecification = Specification
                .where(IssueSpecification.hasStatusNotIn(notVisibleStatuses));

        issueSpecification = getProjectAuthorizationSpecification(criteria.projectId(), issueSpecification);

        issueSpecification = Optional.ofNullable(criteria.keyword())
                .map(IssueSpecification::hasKeyword)
                .map(issueSpecification::and)
                .orElse(issueSpecification);

        issueSpecification = Optional.ofNullable(criteria.keyword())
                .map(IssueSpecification::hasKeyword)
                .map(issueSpecification::and)
                .orElse(issueSpecification);

        issueSpecification = Optional.ofNullable(criteria.status())
                .map(IssueSpecification::hasStatus)
                .map(issueSpecification::and)
                .orElse(issueSpecification);

        issueSpecification = Optional.ofNullable(criteria.categoryIds())
                .map(IssueSpecification::hasCategoryIdIn)
                .map(issueSpecification::and)
                .orElse(issueSpecification);

        return issueSpecification;
    }

    private Specification<Issue> getProjectAuthorizationSpecification(Integer projectId, Specification<Issue> issueSpecification) {
        // If the user is admin, they can access all projects
        // If the user is not admin, they can only access projects they are a member of or public projects
        boolean isAdmin = SecurityUtils.isCurrentUserAdmin();
        List<Integer> accessibleProjectIds = getAccessibleProjectIds(isAdmin);
        if (projectId != null && isUserCanAccessProject(projectId, accessibleProjectIds, isAdmin)) {
            // return issues that are associated with the project
            return issueSpecification.and(IssueSpecification.hasProjectId(projectId));
        }
        if (isAdmin) {
            // return all issues if the user is admin
            return issueSpecification;
        }
        // return issues that are not associated with any project or associated with accessible projects
        return issueSpecification.and(
                IssueSpecification.notHasProjectId()
                        .or(IssueSpecification.hasProjectIdIn(accessibleProjectIds))
        );
    }

    private List<Integer> getAccessibleProjectIds(boolean isAdmin) {
        if (isAdmin) {
            return List.of(); // admin can access all projects
        }
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return projectRepository.findAllProjectIdsByMemberId(currentUserId);
    }

    private boolean isUserCanAccessProject(Integer projectId, List<Integer> accessibleProjectIds, boolean isAdmin) {
        return isAdmin || accessibleProjectIds.contains(projectId);
    }
}
