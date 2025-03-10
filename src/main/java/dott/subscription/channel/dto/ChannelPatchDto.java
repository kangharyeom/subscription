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
    @NonNull
    private ChannelType channelType;
}
