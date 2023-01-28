package HelloMyTeam.Hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TermsAndCondStatus {

    YES("동의"),
    NO("미동의");

    private final String value;


}
