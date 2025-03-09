package dott.subscription.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dott.subscription.member.dto.MemberDeleteDto;
import dott.subscription.member.dto.MemberPatchDto;
import dott.subscription.member.dto.MemberPostDto;
import dott.subscription.member.dto.MemberResponseDto;
import dott.subscription.member.entity.Member;
import dott.subscription.member.service.MemberService;
import dott.subscription.member.mapper.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private MemberMapper memberMapper;

    @MockitoBean
    private MemberController memberController;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 회원 가입 테스트
     */
    @Test
    @DisplayName("회원 가입 API 테스트")
    void createMemberTest() throws Exception {
        // Given
        MemberPostDto memberPostDto = new MemberPostDto("01012345678");
        MemberResponseDto memberResponseDto = new MemberResponseDto(1L, "01012345678", LocalDateTime.now(), LocalDateTime.now());

        given(memberMapper.memberPostDtoToMember(any())).willReturn(new Member());
        given(memberService.createMember(any())).willReturn(new Member());
        given(memberMapper.memberToMemberResponseDto(any())).willReturn(memberResponseDto);

        // When & Then
        mockMvc.perform(post("/api/members/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberPostDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.phoneNumber").value("01012345678"))
                .andDo(document("member-create",
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호")
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
        member.setPhoneNumber("01056781234");

        given(memberMapper.memberPatchDtoToMember(memberPatchDto)).willReturn(member);

        member.setId(1L);
        given(memberService.updatePhoneNumber(member)).willReturn(member);
        given(memberMapper.memberToMemberResponseDto(member)).willReturn(memberResponseDto);

        // When & Then
        mockMvc.perform(patch("/api/members/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"phoneNumber\": \"01056781234\"}"))
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
                                fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("변경된 전화번호")
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
        MemberDeleteDto memberDeleteDto = new MemberDeleteDto();
        Member member = new Member();
        member.setId(1L);

        given(memberMapper.memberDeleteDtoToMember(any())).willReturn(member);
        given(memberService.deleteMember(any())).willReturn(member);

        // When & Then
        mockMvc.perform(patch("/api/members/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDeleteDto)))
                .andExpect(status().isOk())
                .andDo(document("member-delete",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("삭제할 회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("삭제된 회원 ID")
                        )
                ));
    }
}
