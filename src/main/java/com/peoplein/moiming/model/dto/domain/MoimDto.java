package com.peoplein.moiming.model.dto.domain;

import com.peoplein.moiming.domain.DomainChecker;
import com.peoplein.moiming.domain.Moim;
import com.peoplein.moiming.domain.embeddable.Area;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoimDto {

    private Long moimId;
    private String moimName;
    private String moimInfo;
    private String moimPfImg;
    private Area area;
    private int curMemberCount;
    private boolean hasRuleJoin;
    private boolean hasRulePersist;

    private LocalDateTime createdAt;
    private String createdUid;
    private LocalDateTime updatedAt;
    private String updatedUid;

    /*
     Constructor -1
     Moim Entity 를 통한 Dto 형성
     */
    public MoimDto(Moim moim) {

        /*
         응답을 위한 형성시 MoimDto 는 NotNull
         */

        DomainChecker.checkWrongObjectParams(this.getClass().getName(), moim);

        this.moimId = moim.getId();
        this.moimName = moim.getMoimName();
        this.moimInfo = moim.getMoimInfo();
        this.moimPfImg = moim.getMoimPfImg();
        this.area = moim.getMoimArea();
        this.curMemberCount = moim.getCurMemberCount();
        this.hasRuleJoin = moim.isHasRuleJoin();
        this.hasRulePersist = moim.isHasRulePersist();
        this.createdAt = moim.getCreatedAt();
        this.createdUid = moim.getCreatedUid();
        this.updatedAt = moim.getUpdatedAt();
        this.updatedUid = moim.getUpdatedUid();

    }

    /*
     Constructor -2
     각 필드 주입을 통한 Dto 형성
     */
    public MoimDto(Long moimId, String moimName, String moimInfo, String moimPfImg, Area area, int curMemberCount
            , boolean hasRuleJoin, boolean hasRulePersist, LocalDateTime createdAt, String createdUid, LocalDateTime updatedAt, String updatedUid) {

        DomainChecker.checkRightString(this.getClass().getName(), false, moimName, createdUid);

        this.moimId = moimId;
        this.moimName = moimName;
        this.moimInfo = moimInfo;
        this.moimPfImg = moimPfImg;
        this.area = area;
        this.curMemberCount = curMemberCount;
        this.hasRuleJoin = hasRuleJoin;
        this.hasRulePersist = hasRulePersist;
        this.createdAt = createdAt;
        this.createdUid = createdUid;
        this.updatedAt = updatedAt;
        this.updatedUid = updatedUid;
    }


    /*
     Request Model Test 를 위한 Constructor, 인앱 사용금지
     초반에 유저가 Create Moim 을 위해 작성해서 보낼 수 있는 정보들로만 구성
     */
    public MoimDto(String moimName, String moimInfo, String moimPfImg, Area area, boolean hasRuleJoin) {
        this.moimName = moimName;
        this.moimInfo = moimInfo;
        this.moimPfImg = moimPfImg;
        this.area = area;
        this.hasRuleJoin = hasRuleJoin;
    }

}