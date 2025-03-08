package dott.subscription.channel.dto;

import dott.subscription.constant.ChannelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ChannelPostDto {
    @NotBlank(message = "채널 이름은 공백이 아니어야 합니다.")
    private String name;
    @NonNull
    private ChannelType channelType;
}
