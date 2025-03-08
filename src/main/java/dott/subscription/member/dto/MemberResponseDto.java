package dott.subscription.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MemberResponseDto {
    private long id;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
