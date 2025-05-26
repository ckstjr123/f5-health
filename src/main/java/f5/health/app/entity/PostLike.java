package f5.health.app.entity;

import f5.health.app.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "POST_LIKE", uniqueConstraints = @UniqueConstraint(columnNames = {"MEMBER_ID", "POST_ID"}))
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "POST_LIKE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;
}
