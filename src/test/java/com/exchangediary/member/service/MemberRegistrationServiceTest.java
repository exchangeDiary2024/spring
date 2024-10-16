package com.exchangediary.member.service;

import com.exchangediary.member.domain.MemberRepository;
import com.exchangediary.member.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberRegistrationServiceTest {
    @InjectMocks
    private MemberRegistrationService memberRegistrationService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    void 새로운_회원_생성() {
        Long kakaoId = 1L;
        Member newMember = Member.builder()
                .id(1L)
                .kakaoId(kakaoId)
                .orderInGroup(0)
                .build();

        when(memberRepository.findBykakaoId(kakaoId)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(newMember);

        Long result = memberRegistrationService.getOrCreateMember(kakaoId).memberId();

        assertThat(result).isEqualTo(newMember.getId());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void 기존_회원_가져오기() {
        Long kakaoId = 1L;
        Member member = Member.builder()
                .id(1L)
                .kakaoId(kakaoId)
                .orderInGroup(0)
                .build();

        when(memberRepository.findBykakaoId(kakaoId)).thenReturn(Optional.ofNullable(member));

        Long result = memberRegistrationService.getOrCreateMember(kakaoId).memberId();

        assertThat(result).isEqualTo(member.getId());
        verify(memberRepository, times(0)).save(any(Member.class));
    }
}
