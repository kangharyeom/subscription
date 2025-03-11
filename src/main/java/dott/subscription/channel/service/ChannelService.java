package dott.subscription.channel.service;

import dott.subscription.channel.entity.Channel;
import dott.subscription.channel.repository.ChannelRepository;
import dott.subscription.exception.BusinessLogicException;
import dott.subscription.exception.Exceptions;
import dott.subscription.subscription.repository.SubscriptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EntityManager entityManager;

    public Channel createChannel(Channel channel) {
        // 채널 이름 중복 확인
        isChannelNameDuplicated(channel.getName());

        log.info("CHANNEL CREATE SUCCESS : {}", channel.toString());
        return channelRepository.save(channel);
    }

    @Transactional
    public Channel updateChannelType(Channel channel) {
        // 등록된 채널인지 확인 및 비관적락 적용
        Channel findChannel = entityManager.find(Channel.class, channel.getId(), LockModeType.PESSIMISTIC_WRITE);

        if (findChannel == null) {
            throw new BusinessLogicException(Exceptions.CHANNEL_NOT_FOUND);
        }

        Optional.ofNullable(channel.getChannelType())
                .ifPresent(findChannel::setChannelType);

        channelRepository.save(findChannel);
        log.info("CHANNEL TYPE UPDATE SUCCESS : {}", findChannel.toString());
        return findChannel;
    }

    @Transactional
    public Channel deleteChannel(long channelId) {
        // 등록된 채널인지 확인 및 비관적락 적용
        Channel channel = entityManager.find(Channel.class, channelId, LockModeType.PESSIMISTIC_WRITE);

        if (channel == null) {
            throw new BusinessLogicException(Exceptions.CHANNEL_NOT_FOUND);
        }

        //채널과 연관된 구독정보 삭제
        subscriptionRepository.deleteByChannel(channel);

        channelRepository.delete(channel);
        log.info("CHANNEL DELETE SUCCESS");
        return channel;
    }

    // 채널 Id로 채널 조회
    public Channel findChannelByChannelId(long channelId) {
        Optional<Channel> optionalChannel = channelRepository.findById(channelId);

        if (optionalChannel.isPresent()) {
            // 채널이 존재할 경우
            return optionalChannel.get();
        } else {
            // 채널이 존재하지 않을 경우
            throw new BusinessLogicException(Exceptions.CHANNEL_NOT_FOUND);
        }
    }

    // 채널 이름 중복 확인
    public void isChannelNameDuplicated(String name) {
        Optional<Channel> optionalChannel = channelRepository.findByName(name);

        if (optionalChannel.isPresent()) {
            // 채널이 존재할 경우
            throw new BusinessLogicException(Exceptions.CHANNEL_EXIST);
        }
    }
}
