package dott.subscription.channel.service;

import dott.subscription.channel.entity.Channel;
import dott.subscription.channel.repository.ChannelRepository;
import dott.subscription.exception.BusinessLogicException;
import dott.subscription.exception.Exceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    public Channel createChannel(Channel channel) {
        // 채널 이름 중복 확인
        isChannelNameDuplicated(channel.getName());

        log.info("CHANNEL CREATE SUCCESS : {}", channel.toString());
        return channelRepository.save(channel);
    }

    public Channel updateChannel(Channel channel) {
        // 등록된 채널인지 확인
        isChannelExistByChannelId(channel.getId());

        // 채널 이름 중복 확인
        isChannelNameDuplicated(channel.getName());

        Optional.ofNullable(channel.getChannelType())
                .ifPresent(channel::setChannelType);
        Optional.ofNullable(channel.getName())
                .ifPresent(channel::setName);

        log.info("CHANNEL UPDATE SUCCESS : {}", channel.toString());
        return channelRepository.save(channel);
    }

    public Channel deleteChannel(long channelId) {
        // 등록된 채널인지 확인
        Channel channel = isChannelExistByChannelId(channelId);

        log.info("CHANNEL DELETE SUCCESS : {}", channel.toString());
        channelRepository.delete(channel);
        return channel;
    }

    // 채널 Id로 채널 조회
    public Channel isChannelExistByChannelId(long channelId) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);

        if (optionalChannel.isPresent()) {
            // 채널이 존재할 경우
            return optionalChannel.get();
        } else {
            // 채널이 존재하지 않을 경우
            throw new BusinessLogicException(Exceptions.CHANNEL_NOT_FOUND);
        }
    }

    // 채널 이름으로 채널 조회
    public Channel isChannelNameDuplicated(String name) {
        Optional<Channel> optionalChannel = channelRepository.findByName(name);

        if (optionalChannel.isPresent()) {
            // 채널이 존재할 경우
            throw new BusinessLogicException(Exceptions.CHANNEL_EXIST);
        } else {
            return optionalChannel.get();
        }
    }
}
