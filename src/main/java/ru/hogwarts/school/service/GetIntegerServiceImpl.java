package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class GetIntegerServiceImpl {

    public Integer getIntegerParallel() {
        // получаем сумму 1_000_000 чисел параллельно
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }
}
