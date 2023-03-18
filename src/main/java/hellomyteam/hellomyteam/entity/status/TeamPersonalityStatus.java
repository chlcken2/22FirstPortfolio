package hellomyteam.hellomyteam.entity.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//TODO: 동아리 확장시 스포츠 타입별 생성
@Getter
@RequiredArgsConstructor
public enum TeamPersonalityStatus {
    FOOTBALL("축구");

    private final String value;
}
