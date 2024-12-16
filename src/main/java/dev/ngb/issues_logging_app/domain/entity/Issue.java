package dev.ngb.issues_logging_app.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "issues", schema = "public")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "issue")
    private Set<Attachment> attachments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "issue")
    private Set<Comment> comments = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "issue_tags",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new LinkedHashSet<>();

    public enum IssueStatus {
        REVIEWING,
        OPENED,
        RESOLVED,
        CLOSED
    }
}