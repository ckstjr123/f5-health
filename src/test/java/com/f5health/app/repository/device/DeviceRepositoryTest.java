package com.f5health.app.repository.device;

import f5.health.app.HealthApplication;
import f5.health.app.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest(classes = HealthApplication.class)
public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

}
