package dott.subscription.channel.dto;

import dott.subscription.constant.ChannelType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChannelResponseDto {
    private long id;
    private String name;
    private ChannelType channelType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
