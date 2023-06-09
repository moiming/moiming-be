package com.peoplein.moiming.model.dto.request;

import com.peoplein.moiming.domain.enums.MoimMemberStateAction;
import com.peoplein.moiming.domain.enums.MoimRoleType;
import lombok.Data;

@Data
public class MoimMemberActionRequestDto {

    private Long moimId;
    private Long memberId;
    private MoimMemberStateAction stateAction;
    private MoimRoleType roleAction;
    private String inactiveReason;
    private boolean banRejoin;

    public MoimMemberActionRequestDto(Long moimId, Long memberId, MoimMemberStateAction stateAction, MoimRoleType roleAction, String inactiveReason, boolean banRejoin) {
        this.moimId = moimId;
        this.memberId = memberId;
        this.stateAction = stateAction;
        this.roleAction = roleAction;
        this.inactiveReason = inactiveReason;
        this.banRejoin = banRejoin;
    }
}
