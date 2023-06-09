package com.peoplein.moiming.repository;


import com.peoplein.moiming.domain.Member;
import com.peoplein.moiming.model.query.QueryDuplicateColumnMemberDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Long save(Member member);

    Member findMemberById(Long id);

    Member findMemberAndMemberInfoById(Long id);

    Member findMemberByUid(String uid);

    Member findMemberWithRolesByUid(String uid);

    Member findMemberAndMemberInfoWithRolesById(Long id);

    List<Member> findMembersByIds(List<Long> memberIds);

    Optional<Member> findOptionalByPhoneNumber(String memberPhoneNumber);
}
