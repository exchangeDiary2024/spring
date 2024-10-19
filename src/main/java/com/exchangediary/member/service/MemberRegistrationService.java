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
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(jwtService.generateRefreshToken())
                    .member(member)
                    .build();
            member.updateRefreshToken(refreshToken);
        }
        return MemberIdResponse.builder()
                .memberId(member.getId())
                .build();
    }

    private Member signUp(Long kakaoId) {
        Member newMember = Member.builder()
                .kakaoId(kakaoId)
                .orderInGroup(0)
                .build();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtService.generateRefreshToken())
                .member(newMember)
                .build();
        newMember.updateRefreshToken(refreshToken);
        return memberRepository.save(newMember);
    }
}
