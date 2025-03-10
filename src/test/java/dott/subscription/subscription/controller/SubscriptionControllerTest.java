package dott.subscription.subscription.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dott.subscription.channel.entity.Channel;
import dott.subscription.constant.ChannelType;
import dott.subscription.constant.SubscriptionStatus;
import dott.subscription.member.entity.Member;
import dott.subscription.subscription.dto.SubscribeDto;
import dott.subscription.subscription.dto.SubscribeResponseDto;
import dott.subscription.subscription.entity.Subscription;
import dott.subscription.subscription.mapper.SubscriptionMapper;
import dott.subscription.subscription.service.SubscriptionService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class SubscriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private SubscriptionService subscriptionService;
    @MockitoBean
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * 구독 테스트
     */
    @Test
    @DisplayName("구독 API 테스트")
    void subscribe() throws Exception {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now();
        SubscribeDto subscribeDto = new SubscribeDto("01012341234", 1L, SubscriptionStatus.BASIC);

        // 구독할 회원 세팅
        Member member = new Member();
        member.setId(1L);
        member.setPhoneNumber("01012341234");

        // 구독할 창구(채널 세팅)
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.BOTH);
        channel.setId(1L);
        channel.setName("홈페이지");

        // 구독 세팅
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setMember(member);
        subscription.setChannel(channel);
        subscription.setSubscriptionStatus(SubscriptionStatus.BASIC);
        subscription.setModifiedAt(localDateTime);
        given(subscriptionService.subscribe(Mockito.any(SubscribeDto.class))).willReturn(subscription);

        SubscribeResponseDto subscribeResponseDto = new SubscribeResponseDto();
        subscribeResponseDto.setId(1L);
        subscribeResponseDto.setSubscriptionStatus(SubscriptionStatus.BASIC);
        given(subscriptionMapper.subscriptionToSubscribeResponseDto(Mockito.any(Subscription.class))).willReturn(subscribeResponseDto);

        // When & Then
        mockMvc.perform(post("/api/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscribeDto)))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.subscriptionStatus").value("BASIC"))
                .andExpect(status().isCreated())
                .andDo(document("subscription-subscribe",
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호"),
                                fieldWithPath("channelId").type(JsonFieldType.NUMBER).description("채널 ID"),
                                fieldWithPath("subscriptionStatus").type(JsonFieldType.STRING).description("구독 상태")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("구독 ID"),
                                fieldWithPath("data.subscriptionStatus").type(JsonFieldType.STRING).description("구독 상태")
                        )
                ));
    }
    /**
     * 구독 해지 테스트
     */
    @Test
    @DisplayName("구독 해지 API 테스트")
    void unsubscribe() throws Exception {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now();
        SubscribeDto subscribeDto = new SubscribeDto("01012341234", 1L, SubscriptionStatus.NONE);

        // 구독 해지할 회원 세팅
        Member member = new Member();
        member.setId(1L);
        member.setPhoneNumber("01012341234");

        // 구독 해지할 창구(채널 세팅)
        Channel channel = new Channel();
        channel.setChannelType(ChannelType.BOTH);
        channel.setId(1L);
        channel.setName("홈페이지");

        // 구독 해지 세팅
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setMember(member);
        subscription.setChannel(channel);
        subscription.setSubscriptionStatus(SubscriptionStatus.NONE);
        subscription.setModifiedAt(localDateTime);
        given(subscriptionService.unsubscribe(Mockito.any(SubscribeDto.class))).willReturn(subscription);

        SubscribeResponseDto subscribeResponseDto = new SubscribeResponseDto();
        subscribeResponseDto.setId(1L);
        subscribeResponseDto.setSubscriptionStatus(SubscriptionStatus.NONE);
        given(subscriptionMapper.subscriptionToSubscribeResponseDto(Mockito.any(Subscription.class))).willReturn(subscribeResponseDto);
        // When & Then
        mockMvc.perform(post("/api/subscriptions/unsubscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscribeDto)))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.subscriptionStatus").value("NONE"))
                .andExpect(status().isOk())
                .andDo(document("subscription-unSubscribe",
                        requestFields(
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 전화번호"),
                                fieldWithPath("channelId").type(JsonFieldType.NUMBER).description("채널 ID"),
                                fieldWithPath("subscriptionStatus").type(JsonFieldType.STRING).description("구독 상태")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("구독 ID"),
                                fieldWithPath("data.subscriptionStatus").type(JsonFieldType.STRING).description("구독 상태")
                        )
                ));
    }
}
