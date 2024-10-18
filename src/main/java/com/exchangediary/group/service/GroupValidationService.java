package com.exchangediary.group.service;

import com.exchangediary.global.exception.ErrorCode;
import com.exchangediary.global.exception.serviceexception.ConfilctException;
import com.exchangediary.global.exception.serviceexception.DuplicateException;
import com.exchangediary.member.domain.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupValidationService {
    public void checkNumberOfMembers(int numberOfMembers) {
        if (numberOfMembers >= 7) {
            throw new ConfilctException(
                    ErrorCode.FULL_MEMBERS_OF_GROUP,
                    "",
                    String.valueOf(numberOfMembers)
            );
        }
    }

    public void checkNicknameDuplicate(List<Member> members, String nickname) {
        if (members.stream()
                .anyMatch(member -> member.getNickname().equals(nickname))) {
            throw new DuplicateException(
                    ErrorCode.NICKNAME_DUPLICATED,
                    "",
                    nickname
            );
        }
    }

    public void checkProfileDuplicate(List<Member> members, String profileImage) {
        if (members.stream()
                .anyMatch(member -> member.getProfileImage().equals(profileImage))) {
            throw new DuplicateException(
                    ErrorCode.PROFILE_DUPLICATED,
                    "",
                    profileImage
            );
        }
    }
}
