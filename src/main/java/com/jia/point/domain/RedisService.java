package com.jia.point.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate; // 키값은 String만 가능.

    public void saveValue(Long key, BigDecimal value) {
        redisTemplate.opsForValue().set(String.valueOf(key), value);
    }

    public BigDecimal getValue(Long key) {
        BigDecimal bigDecimal = (BigDecimal) redisTemplate.opsForValue().get(String.valueOf(key));

        if (ObjectUtils.isEmpty(bigDecimal)) {
            return BigDecimal.ZERO;
        }
        return bigDecimal;
    }
}
