package com.nhnacademy.accountapi.scheduler;

import com.nhnacademy.accountapi.entity.Member;
import com.nhnacademy.accountapi.entity.Status;
import com.nhnacademy.accountapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class MemberSleepScheduler {

    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateMemberStatusToSleep() {

        log.info("휴면 전환");

        LocalDateTime time = LocalDateTime.now().minusDays(30);

        List<Member> targets = memberRepository.findMembersByLastLoginAtBeforeAndStatus(time, Status.ACTIVE);

        for (Member member : targets) {
            member.updateStatus(Status.SLEEP);
        }
    }
}
