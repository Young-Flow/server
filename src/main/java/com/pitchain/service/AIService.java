package com.pitchain.service;

import com.pitchain.dto.PreferenceInfoDto;
import com.pitchain.dto.res.MemberPreferenceInfoRes;
import com.pitchain.dto.res.PreferenceInfoRes;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AIService {

    @Value("${ai.model-url}")
    private String modelUrl;

    private final MemberRepository memberRepository;

    // 매주 월요일 새벽 4시
    @Scheduled(cron = "0 0 4 * * MON", zone = "Asia/Seoul")
    public void sendMemberPreferenceInfos2AIServer() {
        List<PreferenceInfoDto> preferenceInfoDtos = memberRepository.getMemberPreferenceInfos();

        Map<Long, List<PreferenceInfoDto>> preferenceInfoGroupedByMemberId = preferenceInfoDtos.stream().collect(Collectors.groupingBy(PreferenceInfoDto::memberId));

        List<MemberPreferenceInfoRes> memberPreferenceInfoResList = preferenceInfoGroupedByMemberId.entrySet().stream()
                .map(entry -> {
                    Long memberId = entry.getKey();
                    List<PreferenceInfoRes> preferenceInfoResList = entry.getValue().stream()
                            .map(PreferenceInfoRes::createRes)
                            .toList();
                    return new MemberPreferenceInfoRes(memberId, preferenceInfoResList);
                })
                .toList();

        send2AIServer(memberPreferenceInfoResList);
    }

    private static void send2AIServer(List<MemberPreferenceInfoRes> memberPreferenceInfoResList) {
        new RestTemplate().postForObject("modelUrl", memberPreferenceInfoResList, Void.class);
    }
}
