package dott.subscription.channel.controller;

import dott.subscription.channel.dto.ChannelPatchDto;
import dott.subscription.channel.dto.ChannelPostDto;
import dott.subscription.channel.dto.ChannelResponseDto;
import dott.subscription.channel.entity.Channel;
import dott.subscription.channel.mapper.ChannelMapper;
import dott.subscription.channel.service.ChannelService;
import dott.subscription.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final ChannelMapper channelMapper;

    /**
     * 채널 생성
     */
    @PostMapping("/create")
    public ResponseEntity createChannel(@RequestBody ChannelPostDto channelPostDto) {
        log.info("CHANNEL CREATE START");

        // Dto to Entity 세팅
        Channel channel = channelMapper.channelPostDtoToChannel(channelPostDto);
        log.debug("[channel - createChannel] : {}", channel.toString());

        // 채널 생성
        ChannelResponseDto channelResponseDto = channelMapper.channelToChannelResponseDto(channelService.createChannel(channel));
        log.debug("[ChannelResponseDto - createChannel] : {}", channelResponseDto.toString());
        log.info("CHANNEL CREATE END");
        return new ResponseEntity<>(new SingleResponseDto<>(channelResponseDto), HttpStatus.CREATED);
    }

    /**
     * 채널 단건 조회
     */
    @GetMapping("/{channelId}")
    public ResponseEntity readChannel(@PathVariable long channelId) {
        log.info("CHANNEL READ START");
        log.debug("[channelId - readChannel] : {}", channelId);

        // 채널 단건 조회
        ChannelResponseDto channelResponseDto = channelMapper.channelToChannelResponseDto(channelService.findChannelByChannelId(channelId));
        log.debug("[ChannelResponseDto - readChannel] : {}", channelResponseDto.toString());
        log.info("CHANNEL READ END");
        return new ResponseEntity<>(new SingleResponseDto<>(channelResponseDto), HttpStatus.OK);
    }

    /**
     * 채널 타입 변경
     */
    @PatchMapping("/update/type")
    public ResponseEntity updateChannelType(@RequestBody ChannelPatchDto channelPatchDto) {
        log.info("CHANNEL UPDATE START");

        // Dto to Entity 세팅
        Channel channel = channelMapper.channelPatchDtoToChannel(channelPatchDto);
        log.debug("[channel - updateChannel] : {}", channel.toString());

        // 채널 타입 변경
        ChannelResponseDto channelResponseDto = channelMapper.channelToChannelResponseDto(channelService.updateChannelType(channel));
        log.debug("[ChannelResponseDto - updateChannel] : {}", channelResponseDto.toString());

        log.info("CHANNEL UPDATE END");
        return new ResponseEntity<>(new SingleResponseDto<>(channelResponseDto), HttpStatus.OK);
    }

    /**
     * 채널 삭제
     */
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity deleteChannel(@PathVariable long channelId) {
        log.info("CHANNEL UPDATE START");

        log.debug("[channelId - deleteChannel] : {}", channelId);

        ChannelResponseDto channelResponseDto = channelMapper.channelToChannelResponseDto(channelService.deleteChannel(channelId));
        log.debug("[ChannelResponseDto - deleteChannel] : {}", channelResponseDto.toString());
        log.info("CHANNEL UPDATE END");
        return new ResponseEntity<>(new SingleResponseDto<>(channelResponseDto), HttpStatus.NO_CONTENT);
    }
}
