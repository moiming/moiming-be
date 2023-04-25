package com.peoplein.moiming.service;

import com.peoplein.moiming.domain.Member;
import com.peoplein.moiming.domain.enums.MemberSessionState;
import com.peoplein.moiming.domain.enums.SessionCategoryType;
import com.peoplein.moiming.domain.session.MemberSessionCategoryLinker;
import com.peoplein.moiming.domain.session.MemberSessionLinker;
import com.peoplein.moiming.domain.session.MoimSession;
import com.peoplein.moiming.domain.session.SessionCategoryItem;
import com.peoplein.moiming.model.dto.domain.MoimSessionDto;
import com.peoplein.moiming.model.dto.domain.SessionCategoryItemDto;
import com.peoplein.moiming.model.dto.request.MoimSessionRequestDto;
import com.peoplein.moiming.model.dto.response.MoimSessionResponseDto;
import com.peoplein.moiming.service.input.MoimSessionServiceInput;
import com.peoplein.moiming.service.shell.MoimSessionServiceShell;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MoimSessionService {

    private final MoimSessionServiceShell moimSessionServiceShell;


    public MoimSessionResponseDto createMoimSession(MoimSessionRequestDto moimSessionRequestDto, Member curMember) {

        // 생성 자체는 일반 유저가 불가능
        moimSessionServiceShell.checkAuthority("CREATE", moimSessionRequestDto.getMoimSessionDto().getMoimId(), curMember);

        // moimSessionRequestDto 를 전달하여 Repository 단에서 통신 준비를 마치고, 준비된 애들을 가지고 와준다.
        MoimSessionServiceInput entityInputs = moimSessionServiceShell.createInputForNewMoimSesion(moimSessionRequestDto);

        MoimSessionDto moimSessionDto = moimSessionRequestDto.getMoimSessionDto();
        MoimSession moimSession = MoimSession.createMoimSession(
                moimSessionDto.getSessionName(), moimSessionDto.getSessionInfo()
                , moimSessionDto.getTotalCost(), moimSessionDto.getTotalSenderCount(), curMember.getUid()
                , entityInputs.getMoimOfNewMoimSession(), entityInputs.getScheduleOfNewMoimSession()
        );

        moimSessionRequestDto.getSessionCategoryDetailsDtos().forEach(categoryDetails -> {
                    SessionCategoryType sessionCategoryType = categoryDetails.getSessionCategoryType();

                    int costCnt = 0;

                    for (SessionCategoryItemDto item : categoryDetails.getSessionCategoryItems()) {

                        // TODO :: 전송하는 기본 Data Set 지정 필요 > 그 항목으로 들어올 경우, DEFAULT 로 저장됨

                        String itemName = item.getItemName();
                        if (itemName.equals("기본") || itemName.equals("")) {
                            itemName = SessionCategoryItem.DEFAULT_ITEM_NAME;
                        }

                        // CASCADE 로 moimSession 저장시 자동 저장
                        SessionCategoryItem sessionCategoryItem = SessionCategoryItem.createSessionCategoryItem(
                                itemName, item.getItemCost(), moimSession,
                                entityInputs.getSessionCategoryByType(sessionCategoryType)
                        );

                        costCnt += item.getItemCost();
                    }

                    if (categoryDetails.getCategoryTotalCost() != costCnt) throw new RuntimeException("카테고리 정산이 일치하지 않습니다");
                }
        );

        moimSessionRequestDto.getMemberSessionLinkerDtos().forEach(memberSessionLinkerDto -> {

            // MoimSession push 시 cascade
            MemberSessionLinker memberSessionLinker = MemberSessionLinker.createMemberSessionLinker(
                    memberSessionLinkerDto.getSingleCost(), MemberSessionState.UNSENT
                    , entityInputs.getMemberById(memberSessionLinkerDto.getMemberId())
                    , moimSession
            );

            // MemberSessionLinker push 시 cascade
            memberSessionLinkerDto.getSessionCategoryTypes().forEach(categoryType -> {
                MemberSessionCategoryLinker memberSessionCategoryLinker = new MemberSessionCategoryLinker(
                        memberSessionLinker, entityInputs.getSessionCategoryByType(categoryType)
                );
            });
        });

        // DB 에 푸시 후 RETURN Model 준비
        moimSessionServiceShell.saveMoimSession(moimSession);

        return moimSessionServiceShell.buildAllResponseModel(moimSession, curMember);
    }

    public List<MoimSessionDto> getAllMoimSessions(Long moimId, Member curMember) {

        /*
         1. MoimId 를 통해 모임을 조회
         2. MoimSession 을 조회할 수 있도록 한다
         3. MoimSession 들의 기본 정보 형성 및 전달을 위해선?
         */

        List<MoimSession> moimSessions = moimSessionServiceShell.getAllMoimSessions(moimId);
        List<MoimSessionDto> moimSessionDtos = new ArrayList<>();

        moimSessions.forEach(moimSession -> {
            moimSessionDtos.add(new MoimSessionDto(moimSession));
        });

        return moimSessionDtos;
    }

    public MoimSessionResponseDto getMoimSession(Long moimSessionId, Member curMember) {
        // MoimSession 을 가지고 와서 ResponseModel 을 만든다
        MoimSession moimSession = moimSessionServiceShell.getMoimSession(moimSessionId);
        return moimSessionServiceShell.buildAllResponseModel(moimSession, curMember);
    }

    public MoimSessionResponseDto updateMoimSession(MoimSessionRequestDto moimSessionRequestDto, Member curMember) {

        // Long sessionId 에 val 이 하나 건너와야 한다
        MoimSession moimSession = moimSessionServiceShell.getMoimSession(moimSessionRequestDto.getMoimSessionDto().getSessionId());
        moimSessionServiceShell.checkAuthority("UPDATE", moimSession.getMoim().getId(), curMember);


        // TODO :: 생각해볼 사항
        //         정산활동 같은 경우 여러 도메인들이 엮여 있음
        //         가령, 특정한 Category 내 CategoryItem 이 완전히 바뀌는 것을 "수정"이라고 해보면,
        //         거의 새로운 정산활동을 만들어줘야 하는 것 > 수정의 요소를 판단하기 어려움
        //         또한, 금액 수정도 서로 연관이 되어 있음. 하나만 바꿔줘도, 다 바꿔줘야 할 수도 있음
        //         수정이 꽤나 복잡한 로직을 탈 수 있을 것으로 보임
        //         일단 들어온 것 삭제 후, 재생성 로직 진행하는 것으로 구현해 두었다.

        deleteMoimSession(moimSessionRequestDto.getMoimSessionDto().getSessionId(), curMember);
        return createMoimSession(moimSessionRequestDto, curMember);

    }

    public void deleteMoimSession(Long sessionId, Member curMember) {

        // moimSession 을 삭제하기 위해선 권한 확인
        MoimSession moimSession = moimSessionServiceShell.getMoimSession(sessionId);
        moimSessionServiceShell.checkAuthority("DELETE", moimSession.getMoim().getId(), curMember);
        moimSessionServiceShell.processDelete(moimSession);

    }

}
