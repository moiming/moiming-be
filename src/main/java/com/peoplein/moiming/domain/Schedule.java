package com.peoplein.moiming.domain;

import com.peoplein.moiming.domain.enums.ScheduleMemberState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "schedule_id")
    private Long id;
    private String scheduleTitle;
    private String scheduleLocation;
    private LocalDateTime scheduleDate;
    private int maxCount;
    private boolean isClosed;
    private String createdUid;
    private String updatedUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<MemberScheduleLinker> memberScheduleLinkers = new ArrayList<>();

    public static Schedule createSchedule(String scheduleTitle, String scheduleLocation, LocalDateTime scheduleDate, int maxCount, Moim moim, Member creator) {
        Schedule schedule = new Schedule(scheduleTitle, scheduleLocation, scheduleDate, maxCount, moim, creator);
        return schedule;
    }

    private Schedule(String scheduleTitle, String scheduleLocation, LocalDateTime scheduleDate, int maxCount, Moim moim, Member creator) {

        DomainChecker.checkRightString(this.getClass().getName(), false, scheduleTitle, scheduleLocation);
        DomainChecker.checkWrongObjectParams(this.getClass().getName(), scheduleDate, moim, creator);

        this.scheduleTitle = scheduleTitle;
        this.scheduleLocation = scheduleLocation;
        this.scheduleDate = scheduleDate;
        this.maxCount = maxCount;
        this.createdUid = creator.getUid();

        /*
         초기화
         */
        this.isClosed = false;

        /*
         연관관계 및 편의 메소드
         */
        MemberScheduleLinker.memberJoinSchedule(creator, this, ScheduleMemberState.CREATOR);
        // this.memberScheduleLinkers.add(MemberScheduleLinker.memberJoinSchedule(creator, this, ScheduleMemberState.CREATOR));
        this.moim = moim;
    }

    public void changeScheduleDate(LocalDateTime scheduleDate) {
        DomainChecker.checkWrongObjectParams(this.getClass().getName(), scheduleDate);
        this.scheduleDate = scheduleDate;
    }

    public void changeScheduleLocation(String scheduleLocation) {
        DomainChecker.checkRightString(this.getClass().getName(), false, scheduleLocation);
        this.scheduleLocation = scheduleLocation;
    }

    public void changeScheduleTitle(String scheduleTitle) {
        DomainChecker.checkRightString(this.getClass().getName(), false, scheduleTitle);
        this.scheduleTitle = scheduleTitle;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setUpdatedUid(String updatedUid) {
        this.updatedUid = updatedUid;
    }

    public void addScheduleLinker(MemberScheduleLinker memberScheduleLinker) {
        if (this.maxCount < this.memberScheduleLinkers.size() + 1) {
            throw new RuntimeException("모임 스케쥴의 최대값을 넘는 값입니다.");
        }
        memberScheduleLinkers.add(memberScheduleLinker);
    }

    public boolean hasAnyUpdate(String changedTitle,
                                String changedLocation,
                                LocalDateTime changedTime,
                                int changedMaxCount) {

        return !this.scheduleTitle.equals(changedTitle) ||
                !this.scheduleLocation.equals(changedLocation) ||
                !this.scheduleDate.equals(changedTime) ||
                this.maxCount != changedMaxCount;
    }


}
