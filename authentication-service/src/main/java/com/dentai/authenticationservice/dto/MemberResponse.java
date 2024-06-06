package com.dentai.authenticationservice.dto;

import com.dentai.authenticationservice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberResponse {

    private Long id;
    private String email;

    public static MemberResponse of(final Member member) {
        Logger logger = org.slf4j.LoggerFactory.getLogger(MemberResponse.class);
        logger.info("MemberResponse: of started with member | {}", member);
        return new MemberResponse(member.getId(), member.getEmail());
    }



}
