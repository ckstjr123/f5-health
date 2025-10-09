package f5.health.app;

import f5.health.app.common.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class HealthCareApplicationTests {

    @Test
    void contextLoads() {
// 테스트 데이터 준비: 크기 차이가 매우 큰 두 개의 Set
        final int LARGE_SIZE = 1; // 천만 개
        final int SMALL_SIZE = 5;        // 100개WW

        System.out.println("테스트 데이터 생성 중...");
        Set<Integer> largeSet = IntStream.range(0, LARGE_SIZE).boxed().collect(Collectors.toSet());
        Set<Integer> smallSet = IntStream.range(LARGE_SIZE, LARGE_SIZE + SMALL_SIZE).boxed().collect(Collectors.toSet());
        System.out.println("데이터 생성 완료. (Large: " + largeSet.size() + ", Small: " + smallSet.size() + ")");
        System.out.println("--------------------------------------------------");
        System.out.println("성능 테스트 시작...");
        System.out.println();




//
//        // 참고: 최적화 방식은 순서가 바뀌어도 성능이 거의 동일함
//        startTime = System.nanoTime();
//        Set<Integer> result3 = Sets.union(largeSet, smallSet);
//        endTime = System.nanoTime();
//        durationMs = (endTime - startTime) / 1_000_000.0;
//        System.out.printf("[최적화]   union(큰 Set, 작은 Set) 실행 시간: %.2f ms%n", durationMs);
//        System.out.println("결과 Set 크기: " + result3.size());
//        System.out.println("==================================================");

    }


    public static <T> Set<T> simple(Set<T> s1, Set<T> s2) {
        Set<T> result = new HashSet<>(s1);
        result.removeAll(s2);
        return result;
    }

    // 1. 기존 방식 (최적화되지 않음)
    public static <T> Set<T> union_unoptimized(Set<T> s1, Set<T> s2) {
        Set<T> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    // 2. 사이즈를 비교하는 최적화된 방식
    public static <T> Set<T> union_optimized(Set<T> s1, Set<T> s2) {
        // 더 큰 Set을 기준으로 삼는다.
        Set<T> biggerSet = s1.size() > s2.size() ? s1 : s2;
        Set<T> smallerSet = s1.size() > s2.size() ? s2 : s1;

        Set<T> result = new HashSet<>(biggerSet);
        result.addAll(smallerSet);
        return result;
    }

}
