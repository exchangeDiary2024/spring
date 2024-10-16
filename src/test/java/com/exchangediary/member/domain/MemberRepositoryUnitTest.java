package com.exchangediary.member.domain;

import com.exchangediary.group.domain.entity.Group;
import com.exchangediary.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryUnitTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 일기_작성_순서_조회() {
        Group group = Group.builder()
                .name("버니즈")
                .currentOrder(0)
                .code("code")
                .build();
        entityManager.persist(group);
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Collections.shuffle(nums);
        for (int num : nums) {
            Member member = Member.builder()
                    .profileLocation("/images/" + num)
                    .kakaoId(num * 10L)
                    .nickname(String.valueOf(num))
                    .orderInGroup(num)
                    .group(group)
                    .build();
            entityManager.persist(member);
        }

        List<Member> members = memberRepository.findAllByGroupOrderByOrderInGroup(group);

        assertThat(members).hasSize(7);
        for (int idx = 0; idx < 7; idx++) {
            assertThat(members.get(idx).getOrderInGroup()).isEqualTo(idx + 1);
        }
    }
}
