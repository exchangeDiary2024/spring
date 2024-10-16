package com.exchangediary.member.service;

import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import com.exchangediary.member.ui.dto.response.MemberIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRegistrationService {
    private final MemberRepository memberRepository;

    public MemberIdResponse getOrCreateMember(Long kakaoId) {
        Member member = memberRepository.findBykakaoId(kakaoId)
                .orElseGet(() -> saveUser(kakaoId));
        return MemberIdResponse.builder()
                .memberId(member.getId())
                .build();
    }

    private Member saveUser(Long kakaoId) {
        Member newMember = Member.builder()
                .kakaoId(kakaoId)
                .orderInGroup(0)
                .build();
        return memberRepository.save(newMember);
    }
}
