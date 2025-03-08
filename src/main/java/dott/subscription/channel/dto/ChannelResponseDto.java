package dott.subscription.channel.dto;

import dott.subscription.constant.ChannelType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ChannelResponseDto {
    private long id;
    private String name;
    private ChannelType channelType;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
