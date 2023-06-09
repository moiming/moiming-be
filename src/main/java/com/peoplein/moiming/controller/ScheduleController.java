package com.peoplein.moiming.controller;


import com.peoplein.moiming.NetworkSetting;
import com.peoplein.moiming.domain.Member;
import com.peoplein.moiming.model.ResponseModel;
import com.peoplein.moiming.model.dto.domain.ScheduleMemberDto;
import com.peoplein.moiming.model.dto.request.ScheduleRequestDto;
import com.peoplein.moiming.model.dto.response.ScheduleResponseDto;
import com.peoplein.moiming.service.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Moim 일정 관련")
@RequiredArgsConstructor
@RequestMapping(NetworkSetting.API_SERVER + NetworkSetting.API_MOIM_VER + NetworkSetting.API_MOIM + NetworkSetting.API_MOIM_SCHEDULE)
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseModel<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        Member curMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ScheduleResponseDto responseData = scheduleService.createSchedule(scheduleRequestDto, curMember);
        return ResponseModel.createResponse(responseData);
    }

    /*
     모임 모든 일정 조회
     */
    @GetMapping
    public ResponseModel<List<ScheduleResponseDto>> viewAllMoimSchedule(@RequestParam(name = "moimId") Long moimId) {
        Member curMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ScheduleResponseDto> responseData = scheduleService.viewAllMoimSchedule(moimId, curMember);
        return ResponseModel.createResponse(responseData);
    }

    /*
     특정 일정 세부조회
     */
    @GetMapping("/{scheduleId}")
    public String viewMoimSchedule(@PathVariable(name = "scheduleId") Long scheduleId) {
        Member curMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "필요한 API 라면 말씀해주세요.. 불필요해보여서.. ";
    }

    /*
     일정 수정
     */
    @PatchMapping("/update")
    public ResponseModel<ScheduleResponseDto> updateSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        Member curMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleRequestDto, curMember);
        return ResponseModel.createResponse(scheduleResponseDto);
    }

    /*
     일정 삭제
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseModel<String> deleteSchedule(@PathVariable(name = "scheduleId") Long scheduleId) {
        Member curMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        scheduleService.deleteSchedule(scheduleId, curMember);
        return ResponseModel.createResponse("OK");
    }


    /*
     일정 참가 요청, 일정 불참 요청
     schedule id
     change state
    */
    @PatchMapping("/state/{scheduleId}")
    public ResponseModel<ScheduleMemberDto> changeMemberState(@PathVariable(name = "scheduleId") Long scheduleId, @RequestParam(name = "join") boolean isJoin) {
        Member curMember = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ScheduleService.ChangeMemberTuple tuple = scheduleService.changeMemberState(scheduleId, isJoin, curMember);
        ScheduleMemberDto scheduleMemberDto = ScheduleMemberDto.create(tuple);

        return ResponseModel.createResponse(scheduleMemberDto);
    }


}
