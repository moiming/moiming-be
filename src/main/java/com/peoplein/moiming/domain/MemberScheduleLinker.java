package com.peoplein.moiming.domain;

import com.peoplein.moiming.domain.enums.ScheduleMemberState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberScheduleLinker extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_schedule_linker_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private ScheduleMemberState memberState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static MemberScheduleLinker memberJoinSchedule(Member member, Schedule schedule, ScheduleMemberState memberState) {
        MemberScheduleLinker memberScheduleLinker = new MemberScheduleLinker(member, schedule, memberState);
        return memberScheduleLinker;
    }

    private MemberScheduleLinker(Member member, Schedule schedule, ScheduleMemberState memberState) {

        DomainChecker.checkWrongObjectParams(this.getClass().getName(), member, schedule, memberState);

        this.member = member;
        this.memberState = memberState;

        /*
         연관관계 및 편의 메소드
         */
        this.schedule = schedule;
        this.schedule.addScheduleLinker(this);
    }

    public void changeMemberState(ScheduleMemberState memberState) {
        DomainChecker.checkWrongObjectParams(this.getClass().getName(), memberState);
        this.memberState = memberState;
    }

    public void changeMemberStateWithJoin(boolean isJoin) {
        if (isJoin)
            this.memberState = ScheduleMemberState.ATTEND;
        else
            this.memberState = ScheduleMemberState.NONATTEND;

    }



}
