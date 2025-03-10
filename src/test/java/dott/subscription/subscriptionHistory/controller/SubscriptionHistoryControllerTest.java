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
        member.setPhoneNumber("01012341234");
        member.setId(1L);

        // 채널 세팅
        Channel channel = new Channel();
        channel.setName("홈페이지");
        channel.setId(1L);
        channel.setChannelType(ChannelType.BOTH);

        List<SubscriptionHistory> subscriptionHistoryResponse = null;
        SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
        subscriptionHistory.setPreviousSubscriptionStatus(SubscriptionStatus.NONE);
        subscriptionHistory.setNewSubscriptionStatus(SubscriptionStatus.BASIC);
        subscriptionHistory.setChannel(channel);
        subscriptionHistory.setMember(member);
        given(subscriptionHistoryService.getSubscriptionHistory(Mockito.anyString())).willReturn(subscriptionHistoryResponse);
        // When & Then
        mockMvc.perform(get("/api/subscription/histories/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionHistoryDto)))
                .andExpect(jsonPath("$.data.phoneNumber").value("01012341234"))
                .andExpect(status().isCreated())
                .andDo(document("channel-create",
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("채널 ID"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("채널명"),
                                fieldWithPath("data.channelType").type(JsonFieldType.STRING).description("구독 창구 타입"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("채널 가입 날짜"),
                                fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("채널 정보 수정 날짜")
                        )
                ));
    }
}