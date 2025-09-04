package com.claude;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClaudeMcpOriginApplicationTests {

    @Test
    @DisplayName("Spring Context 로딩 테스트")
    void contextLoads() {
        // 스프링 컨텍스트가 정상적으로 로딩되는지 확인
        // 별도의 assertion은 없지만, 컨텍스트 로딩에 실패하면 테스트가 실패함
    }

}
