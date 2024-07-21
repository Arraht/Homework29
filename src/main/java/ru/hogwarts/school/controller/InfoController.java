package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/get")
public class InfoController {
    @Value("${server.port}")
    private Integer port;

    @GetMapping("/port")
    public Integer getPort() {
        return port;
    }

    @GetMapping("/stream")
    public Long getStream() {
        return LongStream.range(1L, 1_000_000L)
                .reduce(0, Long::sum);
    }
}