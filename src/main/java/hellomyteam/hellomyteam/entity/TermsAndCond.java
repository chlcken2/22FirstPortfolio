package hellomyteam.hellomyteam.entity;

import hellomyteam.hellomyteam.entity.status.TermsAndCondStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonIgnore
    private Member member;

    public void updateMember(Member member) {
        this.member = member;
    }
}
