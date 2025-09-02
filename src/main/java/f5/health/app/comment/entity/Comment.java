package f5.health.app.comment.entity;

import f5.health.app.common.BaseEntity;
import f5.health.app.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "COMMENT")
public class Comment extends BaseEntity {

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
}
