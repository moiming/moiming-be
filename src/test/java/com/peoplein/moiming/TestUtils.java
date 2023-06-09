package com.peoplein.moiming;

import com.peoplein.moiming.domain.*;
import com.peoplein.moiming.domain.embeddable.Area;
import com.peoplein.moiming.domain.enums.*;
import com.peoplein.moiming.domain.fixed.Category;
import com.peoplein.moiming.domain.fixed.Role;
import com.peoplein.moiming.domain.rules.RuleJoin;
import com.peoplein.moiming.model.dto.domain.MoimDto;
import com.peoplein.moiming.model.dto.domain.RuleJoinDto;
import com.peoplein.moiming.model.dto.request.MoimMemberActionRequestDto;
import com.peoplein.moiming.model.dto.request.MoimPostRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public class TestUtils {


    /*
    Member 정보
     */
    public static String uid = "wrock.kang";
    public static String password = "1234";
    public static String encryptedPassword = new BCryptPasswordEncoder().encode(password);
    public static String refreshToken = "REFRESH_TOKEN";
    public static String fcmToken = "FCM_TOKEN";

    /*
     Member Info 정보
     */
    public static String memberEmail = "a@moiming.net";
    public static String memberName = "강우석";
    public static String memberPhone = "01087538643";
    public static MemberGender memberGender = MemberGender.M;

    /*
     추가 입력 정보
     */
    public static LocalDate memberBirth = LocalDate.of(1995, 12, 18);
    public static String memberPfImg = "/user/pf-img/wrock.kang.jpg";
    public static boolean isDormant = false;


    public static String moimName = "모이밍";
    public static String moimInfo = "모이밍을 만드는 사람들입니다";
    public static String moimPfImg = "";
    public static boolean hasRuleJoin = true;
    public static String areaState = "서울시";
    public static String areaCity = "중구";
    public static String createdUid = "wrock.kang";
    public static Area AREA = new Area(areaState, areaCity);
    public static boolean MOIM_DTO_RULE_JOIN = true;


    // Moim Rule
    public static int birthMaxForBigRange = 2020;
    public static int birthMinForBigRange = 1950;

    public static int moimCountBig = 1000;
    public static MemberGender memberGenderAny = MemberGender.N;

    public static boolean dupLeaderAvailable = true;
    public static boolean dupManagerAvailable = true;

    // moim post
    public static String postTitle = "postTitle";
    public static String postContent = "postContent";
    public static boolean isNotice = false;
    public static boolean hasFiles = false;
    public static MoimPostCategory moimPostCategory = MoimPostCategory.GREETING;


    // moimService

    public static MoimMemberStateAction MOIM_MEMBER_STATE_ACTION = MoimMemberStateAction.PERMIT;
    public static MoimRoleType MOIM_SERVICE_ROLE_TYPE = MoimRoleType.NORMAL;
    public static String INACTIVE_REASON = "temporary";
    public static boolean BAN_REJOIN = true;

    // Rule Join DTO
    public static int BIRTH_MAX = 20;
    public static int BIRTH_MIN = 10;
    public static int MOIM_MAX_COUNT = 10;
    public static boolean DUPLICATED_LEADER_ENABLE = true;
    public static boolean DUPLICATED_MANAGER_ENABLE = true;



    public static final Role role = initAdminRole();

    public static Member initOtherMemberAndMemberInfo() {
        Role role = initUserRole();

        Member member = Member.createMember("other" + uid
                , "other" + password
                , "other" + memberEmail
                , "other" + memberName
                , "other" + fcmToken
                , memberGender, role);
        member.getMemberInfo().setMemberBirth(memberBirth);

        return member;
    }

    public static Member initMemberAndMemberInfo() {
        Member member = Member.createMember(uid, password, memberEmail, memberName, fcmToken, memberGender, role);
        member.getMemberInfo().setMemberBirth(memberBirth);

        return member;
    }

    public static Member initMemberAndMemberInfo(String memberName, String memberEmail) {
        Member member = Member.createMember(uid, password, memberEmail, memberName, fcmToken, memberGender, role);
        member.getMemberInfo().setMemberBirth(memberBirth);

        return member;
    }

    public static Member initMemberAndMemberInfo(String uid, String memberName, String memberEmail) {
        Member member = Member.createMember(uid, password, memberEmail, memberName, fcmToken, memberGender, role);
        member.getMemberInfo().setMemberBirth(memberBirth);

        return member;
    }


    public static Role initAdminRole() {
        return new Role(1L, "admin", RoleType.ADMIN);
    }

    public static Role initUserRole() {
        return new Role(2L, "admin", RoleType.USER);
    }


    public static Moim initMoimAndRuleJoin() {
        Moim moim = Moim.createMoim(moimName, moimInfo, moimPfImg, new Area(areaState, areaCity), createdUid);
        RuleJoin ruleJoin = new RuleJoin(1, 1, MemberGender.F, 1, true, true, moim, createdUid);
        return moim;
    }

    public static Moim createMoimOnly() {
        Moim moim = Moim.createMoim(moimName, moimInfo, moimPfImg, new Area(areaState, areaCity), createdUid);
        moim.setHasRuleJoin(false);
        return moim;
    }

    public static Moim createMoimOnly(String moimName) {
        return Moim.createMoim(moimName, moimInfo, moimPfImg, new Area(areaState, areaCity), createdUid);
    }

    public static Moim createOtherMoimOnly(String moimName, Area area) {
        return Moim.createMoim(moimName, "other" + moimInfo, "other" + moimPfImg, area, createdUid);
    }

    public static MoimPost initMoimPost(Moim moim, Member member) {
        return MoimPost.createMoimPost(postTitle, postContent, moimPostCategory, isNotice, hasFiles, moim, member);
    }

    public static MoimPost initNoticeMoimPost(Moim moim, Member member) {
        return MoimPost.createMoimPost(postTitle, postContent, moimPostCategory, true, hasFiles, moim, member);
    }

    public static MoimDto initMoimDto() {
        return new MoimDto(moimName, moimInfo, moimPfImg, AREA, MOIM_DTO_RULE_JOIN);
    }

    public static MoimDto createOtherMoimDtoForUpdate() {
        return new MoimDto("other" + moimName,
                "other" + moimInfo,
                "other" + moimPfImg, AREA, MOIM_DTO_RULE_JOIN);
    }


    public static RuleJoinDto initRuleJoinDto() {
        return new RuleJoinDto(BIRTH_MAX, BIRTH_MIN, memberGender, MOIM_MAX_COUNT, DUPLICATED_LEADER_ENABLE, DUPLICATED_MANAGER_ENABLE);
    }

    public static MemberMoimLinker createLeaderMemberMoimLinker(Member member, Moim moim) {
        return MemberMoimLinker.memberJoinMoim(member, moim, MoimRoleType.LEADER, MoimMemberState.ACTIVE);
    }

    public static MemberMoimLinker createNormalMemberMoimLinker(Member member, Moim moim) {
        return MemberMoimLinker.memberJoinMoim(member, moim, MoimRoleType.NORMAL, MoimMemberState.ACTIVE);
    }

    public static MemberMoimLinker createNormalMemberMoimLinkerWithWait(Member member, Moim moim) {
        return MemberMoimLinker.memberJoinMoim(member, moim, MoimRoleType.NORMAL, MoimMemberState.WAIT_BY_MOIM_CNT);
    }


    public static List<CategoryName> initCategoryName() {
        return List.of(CategoryName.ALCOHOL, CategoryName.CODING);
    }


    public static RuleJoinDto createRuleJoinDto() {

        return null;
    }

    public static List<Category> createMoimCategories() {
        List<CategoryName> categoryNames = TestUtils.initCategoryName();
        Category newCategory = new Category();
        newCategory.setCategoryName(categoryNames.get(0));
        return List.of(newCategory);
    }

    public static List<Category> createMoimCategoriesWithTwo() {
        List<CategoryName> categoryNames = TestUtils.initCategoryName();
        Category newCategory1 = new Category();
        newCategory1.setCategoryName(categoryNames.get(0));

        Category newCategory2 = new Category();
        newCategory2.setCategoryName(categoryNames.get(1));

        return List.of(newCategory1, newCategory2);
    }


    public static void initDatabase(InitDatabaseQuery initDatabase) {
        initDatabase.initUserRole();
        initDatabase.initMemberWithAdminGrant();
        initDatabase.initMemberWithUserGrant();
        initDatabase.initMoimCategory();
        initDatabase.initMoimEntity();
        initDatabase.initMoimEntity2();
        initDatabase.joinMoim1OfMember2();
        initDatabase.initPostByMember1();
        initDatabase.initPostByMember2();
        initDatabase.initPostComment();
        initDatabase.initSchedule1InMoim1();
        initDatabase.initSchedule2InMoim1();
    }


    public static MoimPostRequestDto initMoimPostRequestDto() {

        List<CategoryName> categoryNames = TestUtils.initCategoryName();
        MoimPostRequestDto moimPostRequestDto = new MoimPostRequestDto(
                null, null,
                postTitle, postContent, isNotice, MoimPostCategory.EXTRA);
        return moimPostRequestDto;
    }

    public static MoimMemberActionRequestDto createActionRequestDto(Long moimId, Long memberId, MoimMemberStateAction moimMemberStateAction) {
        return new MoimMemberActionRequestDto(
                moimId, memberId, moimMemberStateAction, MoimRoleType.NORMAL, "", true);
    }

    public static void truncateAllTable(JdbcTemplate jdbcTemplate) {

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        List<String> tableNames = List.of(
                "category",
                "moim_rule",
                "rule_join",
                "rule_persist",
                "member",
                "member_info",
                "member_moim_linker",
                "member_role_linker",
                "member_schedule_linker",
                "moim",
                "moim_category_linker",
                "moim_post",
                "moim_review",
                "post_comment",
                "post_file",
                "review_answer",
                "schedule",
                "role");

        tableNames.forEach(tableName ->
                jdbcTemplate.execute("TRUNCATE TABLE " + tableName));

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }


    public static Area createAreaForTest() {
        return new Area(TestUtils.areaState, TestUtils.areaCity);
    }

    public static Category createCategoryForTest() {
        return new Category();
    }
}
