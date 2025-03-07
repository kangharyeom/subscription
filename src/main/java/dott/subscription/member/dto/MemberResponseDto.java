package dott.subscription.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponseDto {
    private long id;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
