package dott.subscription.channel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dott.subscription.channel.dto.ChannelPatchDto;
import dott.subscription.channel.dto.ChannelPostDto;
import dott.subscription.channel.dto.ChannelResponseDto;
import dott.subscription.channel.entity.Channel;
import dott.subscription.channel.mapper.ChannelMapper;
import dott.subscription.channel.service.ChannelService;
import dott.subscription.constant.ChannelType;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class ChannelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChannelService channelService;
    @MockitoBean
    private ChannelMapper channelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * 채널 생성
     */
    @Test
    @DisplayName("채널 생성 API 테스트")
    void createChannel() throws Exception {
        // Given
        ChannelPostDto channelPostDto = new ChannelPostDto("홈페이지", ChannelType.BOTH);
        LocalDateTime localDateTime = LocalDateTime.now();
        ChannelResponseDto channelResponseDto = new ChannelResponseDto(1L, "홈페이지", ChannelType.BOTH, localDateTime, localDateTime);

        Channel channel = new Channel();
        channel.setChannelType(ChannelType.BOTH);
        channel.setName("홈페이지");

        given(channelMapper.channelPostDtoToChannel(Mockito.any(ChannelPostDto.class))).willReturn(channel);

        Channel channel2 = new Channel();

        channel2.setChannelType(ChannelType.BOTH);
        channel2.setId(1L);
        channel2.setName("홈페이지");
        channel2.setCreatedAt(localDateTime);
        channel2.setModifiedAt(localDateTime);
        given(channelService.createChannel(Mockito.any(Channel.class))).willReturn(channel2);
        given(channelMapper.channelToChannelResponseDto(Mockito.any(Channel.class))).willReturn(channelResponseDto);
        // When & Then
        mockMvc.perform(post("/api/channels/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(channelPostDto)))
                .andExpect(jsonPath("$.data.name").value("홈페이지"))
                .andExpect(jsonPath("$.data.channelType").value("BOTH"))
                .andExpect(status().isCreated())
                .andDo(document("channel-create",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("채널명"),
                                fieldWithPath("channelType").type(JsonFieldType.STRING).description("구독 창구 타입")
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

    /**
     * 채널 단건 조회
     */
    @Test
    @DisplayName("채널 단건 조회 API 테스트")
    void readChannel() throws Exception {
        // Given
        long channelId = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();

        Channel channel = new Channel();
        channel.setId(channelId);
        ChannelResponseDto channelResponseDto = new ChannelResponseDto(1L, "홈페이지", ChannelType.BOTH, localDateTime, localDateTime);
        given(channelService.findChannelByChannelId(Mockito.anyLong())).willReturn(channel);
        given(channelMapper.channelToChannelResponseDto(Mockito.any(Channel.class))).willReturn(channelResponseDto);

        // When & Then
        mockMvc.perform(get("/api/channels/{id}",channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(channelId)))
                .andExpect(status().isOk())
                .andDo(document("channel-find-by-channelId",
                        pathParameters(
                                parameterWithName("id").description("조회할 채널 ID")
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

    /**
     * 채널 타입 변경
     */
    @Test
    @DisplayName("채널 타입 변경 API 테스트")
    void updateChannel() throws Exception {
        // Given
        ChannelPatchDto channelPatchDto =  new ChannelPatchDto(1L, ChannelType.SUBSCRIBE_ONLY);
        LocalDateTime localDateTime = LocalDateTime.now();
        ChannelResponseDto channelResponseDto = new ChannelResponseDto(1L, "홈페이지", ChannelType.SUBSCRIBE_ONLY, localDateTime, localDateTime);

        Channel channel = new Channel();
        channel.setId(1L);
        channel.setChannelType(ChannelType.SUBSCRIBE_ONLY);

        given(channelMapper.channelPatchDtoToChannel(Mockito.any(ChannelPatchDto.class))).willReturn(channel);

        Channel channel2 = new Channel();
        channel2.setId(1L);
        channel2.setChannelType(ChannelType.SUBSCRIBE_ONLY);
        channel2.setName("홈페이지");
        channel2.setCreatedAt(localDateTime);
        channel2.setModifiedAt(localDateTime);
        given(channelService.updateChannelType(Mockito.any(Channel.class))).willReturn(channel2);
        given(channelMapper.channelToChannelResponseDto(Mockito.any(Channel.class))).willReturn(channelResponseDto);

        // When & Then
        mockMvc.perform(patch("/api/channels/update/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(channelPatchDto)))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.channelType").value("SUBSCRIBE_ONLY"))
                .andExpect(status().isOk())
                .andDo(document("channel-update-channelType",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("채널 ID"),
                                fieldWithPath("channelType").type(JsonFieldType.STRING).description("구독 창구 타입")
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

    /**
     * 채널 삭제
     */
    @Test
    @DisplayName("채널 삭제 API 테스트")
    void deleteChannel() throws Exception {
        // Given
        Channel channel = new Channel();
        channel.setId(1L);
        given(channelService.deleteChannel(Mockito.anyLong())).willReturn(channel);

        // When & Then
        mockMvc.perform(delete("/api/channels/delete/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(1L)))
                .andExpect(status().isNoContent())
                .andDo(document("channel-delete",
                        pathParameters(
                                parameterWithName("id").description("삭제할 채널 ID")
                        )
                ));
    }
}