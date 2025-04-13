package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "COMMENT")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column(name = "MEMBER_ID")
    private Long memberId; // 외래키 X

    @Column(name = "WRITER_NAME")
    private String writerName;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "COMMENTED_AT")
    private LocalDateTime commentedAt;

    @Column(name = "EDITED_AT")
    private LocalDateTime editedAt;
}
