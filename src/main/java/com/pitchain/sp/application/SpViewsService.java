package com.pitchain.sp.application;

import com.pitchain.common.redis.RedisHashRepository;
import com.pitchain.sp.infrastucture.SpRepositoryCustom;
import com.pitchain.sp.infrastucture.dto.SpViewsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpViewsService {

    private final SpRepositoryCustom spRepositoryCustom;
    private final RedisHashRepository redisHashRepository;

    private static final String SP_VIEW_REDIS_KEY = "spView";

    public void updateSpView(Long spId) {
        redisHashRepository.increment(SP_VIEW_REDIS_KEY, String.valueOf(spId), 1L);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void runUpdateSpViews() {
        updateSpViews();
    }

    @Transactional
    public void updateSpViews() {
        List<String> list = redisHashRepository.getAndDeleteAll(SP_VIEW_REDIS_KEY);
        List<SpViewsDto> spViewsDtoList = parseResult(list);

        for (SpViewsDto spViewsDto : spViewsDtoList) {
            spRepositoryCustom.updateSpView(spViewsDto.spId(), spViewsDto.views());
        }
    }

    private List<SpViewsDto> parseResult(List<String> list) {
        if (list.size() % 2 != 0){
            log.error("조회수 개수가 올바르지 않습니다.");
            throw new IllegalArgumentException("list 개수가 올바르지 않습니다.");
        }

        List<SpViewsDto> spViewsDtoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            Long spId = Long.parseLong(list.get(i));
            Long views = Long.parseLong(list.get(i + 1));

            SpViewsDto spViewsDto = new SpViewsDto(spId, views);
            spViewsDtoList.add(spViewsDto);
        }

        return spViewsDtoList;
    }
}
