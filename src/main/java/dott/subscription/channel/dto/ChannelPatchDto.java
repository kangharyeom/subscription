package dott.subscription.channel.dto;

import dott.subscription.constant.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChannelPatchDto {
    private long id;
    @NonNull
    private ChannelType channelType;
}
