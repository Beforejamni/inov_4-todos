package study.todos.TodoTest.study.ClockTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

public class ClockTest {

    @Test
    void clcok() throws InterruptedException {
        Clock clock = Clock.systemDefaultZone();

        LocalDateTime firstTime = LocalDateTime.now(clock);

        Thread.sleep(10);

        LocalDateTime secondTime = LocalDateTime.now(clock);

        Assertions.assertThat(secondTime).isAfter(firstTime);

    }

    @Test
    void fixedClock() throws InterruptedException {
        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());

        LocalDateTime firstTime = LocalDateTime.now(fixed);

        Thread.sleep(10);

        LocalDateTime secondTime = LocalDateTime.now(fixed);

        Assertions.assertThat(firstTime).isEqualTo(secondTime);
    }
}
