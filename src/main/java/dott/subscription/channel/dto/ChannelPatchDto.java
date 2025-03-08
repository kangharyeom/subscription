package dott.subscription.channel.dto;

import dott.subscription.constant.ChannelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ChannelPatchDto {
    private long id;
    @NotBlank(message = "채널 이름은 공백이 아니어야 합니다.")
    private String name;
    @NonNull
    private ChannelType channelType;
}
