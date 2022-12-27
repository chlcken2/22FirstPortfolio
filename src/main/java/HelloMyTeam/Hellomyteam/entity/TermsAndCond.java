package HelloMyTeam.Hellomyteam.entity;

import HelloMyTeam.Hellomyteam.entity.status.TermsAndCondStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class TermsAndCond {
    @Id
    @GeneratedValue
    @Column(name = "termsAndCond_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TermsAndCondStatus termsOfServiceYn;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TermsAndCondStatus privacyYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
