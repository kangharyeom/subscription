package dott.subscription.channel.mapper;

import dott.subscription.channel.dto.ChannelPatchDto;
import dott.subscription.channel.dto.ChannelPostDto;
import dott.subscription.channel.dto.ChannelResponseDto;
import dott.subscription.channel.entity.Channel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    Channel channelPostDtoToChannel(ChannelPostDto channelPostDto);
    Channel channelPatchDtoToChannel(ChannelPatchDto channelPatchDto);
    ChannelResponseDto channelToChannelResponseDto(Channel channel);
}
