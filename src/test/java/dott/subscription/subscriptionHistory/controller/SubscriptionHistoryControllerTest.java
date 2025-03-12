package dott.subscription.subscriptionHistory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dott.subscription.channel.entity.Channel;
import dott.subscription.constant.ChannelType;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.entity.Member;
import dott.subscription.subscriptionHistory.dto.SubscriptionHistoryDto;
import dott.subscription.subscriptionHistory.entity.SubscriptionHistory;
import dott.subscription.subscriptionHistory.service.SubscriptionHistoryService;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class SubscriptionHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SubscriptionHistoryService subscriptionHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * 구독 & 구독 해지 이력 조회 테스트
     */
    @Test
    @DisplayName("구독 & 구독 해지 이력 조회 API 테스트")
    void getSubscriptionHistories() throws Exception {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now();
        SubscriptionHistoryDto subscriptionHistoryDto = new SubscriptionHistoryDto("01012341234");

        // 회원 세팅
        Member member = new Member();
        member.setId(1L);
        member.setPhoneNumber("01012341234");
        member.setCreatedAt(localDateTime);
        member.setModifiedAt(localDateTime);

        // 채널 세팅
        Channel channel = new Channel();
        channel.setName("홈페이지");
        channel.setId(1L);
        channel.setChannelType(ChannelType.BOTH);
        channel.setCreatedAt(localDateTime);
        channel.setModifiedAt(localDateTime);

        SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
        subscriptionHistory.setId(1L);
        subscriptionHistory.setPreviousSubscriptionStatus(SubscriptionStatus.NONE);
        subscriptionHistory.setNewSubscriptionStatus(SubscriptionStatus.BASIC);
        subscriptionHistory.setChannel(channel);
        subscriptionHistory.setMember(member);
        subscriptionHistory.setCreatedAt(localDateTime);
        subscriptionHistory.setModifiedAt(localDateTime);

        List<SubscriptionHistory> subscriptionHistoryResponse = new ArrayList<>();
        subscriptionHistoryResponse.add(subscriptionHistory);

        given(subscriptionHistoryService.getSubscriptionHistory(Mockito.anyString())).willReturn(subscriptionHistoryResponse);

        // When & Then
        mockMvc.perform(get("/api/subscription/histories/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionHistoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].previousSubscriptionStatus").value(SubscriptionStatus.NONE.toString()))
                .andExpect(jsonPath("$.data[0].newSubscriptionStatus").value(SubscriptionStatus.BASIC.toString()))
                .andExpect(jsonPath("$.data[0].member.id").value(1L))
                .andExpect(jsonPath("$.data[0].member.phoneNumber").value("01012341234"))
                .andExpect(jsonPath("$.data[0].channel.id").value(1L))
                .andExpect(jsonPath("$.data[0].channel.name").value("홈페이지"))
                .andExpect(jsonPath("$.data[0].channel.channelType").value(ChannelType.BOTH.toString()))
                .andDo(document("subscription-history-by-phoneNumber",
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("구독조회 이력 ID"),
                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("구독 날짜"),
                                fieldWithPath("data[].modifiedAt").type(JsonFieldType.STRING).description("구독 정보 수정 날짜"),
                                fieldWithPath("data[].cancelledAt").type(JsonFieldType.NULL).description("구독 해지 날짜"),
                                fieldWithPath("data[].previousSubscriptionStatus").type(JsonFieldType.STRING).description("이전 구독 상태"),
                                fieldWithPath("data[].newSubscriptionStatus").type(JsonFieldType.STRING).description("현재 구독 상태"),
                                fieldWithPath("data[].member").type(JsonFieldType.OBJECT).description("회원 클래스"),
                                fieldWithPath("data[].member.id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                fieldWithPath("data[].member.phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호"),
                                fieldWithPath("data[].member.createdAt").type(JsonFieldType.STRING).description("회원 가입 일자"),
                                fieldWithPath("data[].member.modifiedAt").type(JsonFieldType.STRING).description("회원 수정 이력 일자"),
                                fieldWithPath("data[].channel").type(JsonFieldType.OBJECT).description("채널 클래스"),
                                fieldWithPath("data[].channel.id").type(JsonFieldType.NUMBER).description("채널 ID"),
                                fieldWithPath("data[].channel.name").type(JsonFieldType.STRING).description("채널명"),
                                fieldWithPath("data[].channel.channelType").type(JsonFieldType.STRING).description("채널 창구 타입"),
                                fieldWithPath("data[].channel.createdAt").type(JsonFieldType.STRING).description("채널 생성 일자"),
                                fieldWithPath("data[].channel.modifiedAt").type(JsonFieldType.STRING).description("채널 수정 일자")
                        )
                ));
    }
}