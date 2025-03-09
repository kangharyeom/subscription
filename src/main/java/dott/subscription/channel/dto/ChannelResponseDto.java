package dott.subscription.channel.dto;

import dott.subscription.constant.ChannelType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ChannelResponseDto {
    private long id;
    private String name;
    private ChannelType channelType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
