package com.peoplein.moiming.repository.jpa;

import antlr.preprocessor.PreprocessorTokenTypes;
import com.peoplein.moiming.domain.Member;
import com.peoplein.moiming.domain.QMember;
import com.peoplein.moiming.model.query.QueryDuplicateColumnMemberDto;
import com.peoplein.moiming.repository.MemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.peoplein.moiming.domain.QMember.*;
import static com.peoplein.moiming.domain.QMemberInfo.*;
import static com.peoplein.moiming.domain.QMemberRoleLinker.*;
import static com.peoplein.moiming.domain.fixed.QRole.*;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Member findMemberById(Long memberId) {

        /*
        Query : select * from member m
                where m.member_id = {id};
         */

        return queryFactory.selectFrom(member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public Member findMemberAndMemberInfoById(Long memberId) {


        /*
         JPQL Query : select m from Member m
                        join fetch m.memberInfo mi where m.id = :id;

         */

        return queryFactory.selectFrom(member)
                .join(member.memberInfo, memberInfo).fetchJoin()
                .where(member.id.eq(memberId))
                .fetchOne();

    }

    @Override
    public Member findMemberByUid(String uid) {

        /*
        Query : select * from member m
                where m.uid = {uid};
         */

        return queryFactory.selectFrom(member)
                .where(member.uid.eq(uid))
                .fetchOne();
    }


    @Override
    public Member findMemberWithRolesByUid(String uid) {
        /*
         JPQL : select distinct m from Member m
                    join fetch m.roles mri
                    join fetch mri.role r
                    where m.uid = {uid}
         */

        return queryFactory.selectFrom(member).distinct()
                .join(member.roles, memberRoleLinker).fetchJoin()
                .join(memberRoleLinker.role, role).fetchJoin()
                .where(member.uid.eq(uid))
                .fetchOne();
    }

    @Override
    public Member findMemberAndMemberInfoWithRolesById(Long id) {
        /*
         JPQL : select distinct m from Member m
                    join fetch m.memberInfo mi
                    join fetch m.roles mri
                    join fetch mri.role r
                    where m.id = :{id}
         */

        return queryFactory.selectFrom(member).distinct()
                .join(member.memberInfo, memberInfo).fetchJoin()
                .join(member.roles, memberRoleLinker).fetchJoin()
                .join(memberRoleLinker.role, role).fetchJoin()
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<Member> findMembersByIds(List<Long> memberIds) {
        return queryFactory.selectFrom(member)
                .where(member.id.in(memberIds))
                .fetch();
    }

    @Override
    public Optional<Member> findOptionalByPhoneNumber(String memberPhoneNumber) {
        return Optional.ofNullable(
                queryFactory.selectFrom(member)
                        .join(member.memberInfo, memberInfo).fetchJoin()
                        .where(member.memberInfo.memberPhone.eq(memberPhoneNumber))
                        .fetchOne()
        );

    }

}
