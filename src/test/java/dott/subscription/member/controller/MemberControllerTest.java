package dott.subscription.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.dto.*;
import dott.subscription.member.entity.Member;
import dott.subscription.member.service.MemberService;
import dott.subscription.member.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private MemberMapper memberMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * 회원 가입 테스트
     */
    @Test
    @DisplayName("회원 가입 API 테스트")
    void createMemberTest() throws Exception {
        // Given
        MemberPostDto memberPostDto = new MemberPostDto("01012345678");
        LocalDateTime localDateTime = LocalDateTime.now();
        MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "01012345678", localDateTime, localDateTime);

        Member member = new Member();
        member.setPhoneNumber("01012345678");

        given(memberMapper.memberPostDtoToMember(Mockito.any(MemberPostDto.class))).willReturn(member);

        Member member2 = new Member();
        member2.setId(1L);
        member2.setPhoneNumber("01012345678");
        member2.setCreatedAt(localDateTime);
        member2.setModifiedAt(localDateTime);
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(member2);
        given(memberMapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(memberResponseDto);

        // When & Then
        mockMvc.perform(post("/api/members/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPostDto)))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.phoneNumber").value("01012345678"))
                .andExpect(status().isCreated())
                .andDo(document("member-create",
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 가입 날짜"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정 날짜")
                        )
                ));
    }

    /**
     * 전화번호 변경 테스트
     */
    @Test
    @DisplayName("회원 전화번호 변경 API 테스트")
    void updateMemberPhoneNumberTest() throws Exception {
        // Given
        MemberPatchDto memberPatchDto = new MemberPatchDto(1L, "01056781234");
        MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "01056781234", LocalDateTime.now(), LocalDateTime.now());

        Member member = new Member();
        member.setId(1L);
        member.setPhoneNumber("01056781234");

        given(memberMapper.memberPatchDtoToMember(Mockito.any(MemberPatchDto.class))).willReturn(member);

        Member member2 = new Member();
        member2.setId(1L);
        member2.setPhoneNumber("01056781234");
        LocalDateTime localDateTime = LocalDateTime.now();
        member2.setCreatedAt(localDateTime);
        member2.setModifiedAt(localDateTime);
        given(memberService.updatePhoneNumber(Mockito.any(Member.class))).willReturn(member2);
        given(memberMapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(memberResponseDto);

        // When & Then
        mockMvc.perform(patch("/api/members/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPatchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phoneNumber").value("01056781234"))
                .andDo(print())
                .andDo(document("member-update-phone",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("변경할 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("변경된 전화번호"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 가입 날짜"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정 날짜")
                        )
                ));
    }

    /**
     * 회원 상세 조회 테스트
     */
    @Test
    @DisplayName("회원 상세 조회 테스트")
    void getMemberDetailsTest() throws Exception {
        long memberId = 1L;
        Member member = new Member();
        member.setId(1L);

        given(memberService.findMemberByMemberId(Mockito.anyLong())).willReturn(member);

        LocalDateTime localDateTime = LocalDateTime.now();
        MemberDetailsResponseDto memberDetailsResponseDto = new MemberDetailsResponseDto();
        memberDetailsResponseDto.setId(1L);
        memberDetailsResponseDto.setPhoneNumber("01056781234");
        memberDetailsResponseDto.setCreatedAt(localDateTime);
        memberDetailsResponseDto.setModifiedAt(localDateTime);
        memberDetailsResponseDto.setSubscriptionStatus(SubscriptionStatus.NONE);
        given(memberMapper.memberToMemberDetailsResponseDto(Mockito.any(Member.class),Mockito.any())).willReturn(memberDetailsResponseDto);

        // When & Then
        mockMvc.perform(get("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId)))
                .andExpect(status().isOk())
                .andDo(document("member-details",
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("삭제된 회원 ID"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("변경된 전화번호"),
                                fieldWithPath("data.subscriptionStatus").type(JsonFieldType.STRING).description("회원 구독 상태"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 가입 날짜"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 정보 수정 날짜")
                        )
                ));
    }

    /**
     * 회원 삭제 테스트
     */
    @Test
    @DisplayName("회원 삭제 API 테스트")
    void deleteMemberTest() throws Exception {
        // Given
        MemberDeleteDto memberDeleteDto = new MemberDeleteDto(1L,"01056781234");
        Member member = new Member();
        member.setId(1L);
        MemberResponseDto memberResponseDto = new MemberResponseDto();

        given(memberMapper.memberDeleteDtoToMember(Mockito.any(MemberDeleteDto.class))).willReturn(member);
        given(memberService.deleteMember(Mockito.any(Member.class))).willReturn(member);
        given(memberMapper.memberToMemberResponseDto(Mockito.any(Member.class))).willReturn(memberResponseDto);

        // When & Then
        mockMvc.perform(delete("/api/members/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDeleteDto)))
                .andExpect(status().isNoContent())
                .andDo(document("member-delete",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("삭제할 회원 ID"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("변경할 전화번호")
                        )
                ));
    }
}
