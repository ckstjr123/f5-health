package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "POST")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_CODE")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "VIEW")
    private long view;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "POST_LIKE_COUNT")
    private long likeCount;

    @Column(name = "POST_COMMENT_COUNT")
    private long commentCount;

    @Column(name = "POSTED_AT")
    private LocalDateTime postedAt;

    @Column(name = "EDITED_AT")
    private LocalDateTime editedAt;
}
