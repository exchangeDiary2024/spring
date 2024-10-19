package com.exchangediary.member.service;

import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.domain.entity.RefreshToken;
import com.exchangediary.member.ui.dto.response.MemberIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRegistrationService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public MemberIdResponse getOrCreateMember(Long kakaoId) {
        Member member = memberRepository.findBykakaoId(kakaoId)
                .orElseGet(() -> signUp(kakaoId));

        if (member.getRefreshToken() == null) {
            RefreshToken refreshToken = RefreshToken.of(jwtService.generateRefreshToken(), member);
            member.updateRefreshToken(refreshToken);
        }
        return MemberIdResponse.from(member.getId());
    }

    private Member signUp(Long kakaoId) {
        Member newMember = Member.from(kakaoId);
        RefreshToken refreshToken = RefreshToken.of(jwtService.generateRefreshToken(), newMember);
        newMember.updateRefreshToken(refreshToken);
        return memberRepository.save(newMember);
    }
}
