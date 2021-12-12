import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

/**
 * @author atsutsuiev
 */
class EventRPMStatisticTest {
    private var clock = object : Clock() {
        private var now: Instant = Instant.now()

        override fun instant(): Instant = now

        fun addTime(amountToAdd: Long, unit: TemporalUnit?) { this.now = now.plus(amountToAdd, unit) }

        fun reset() { this.now = Instant.now() }

        override fun getZone(): ZoneId = TODO("Not yet implemented")

        override fun withZone(zone: ZoneId?): Clock = TODO("Not yet implemented")
    }
    private var eventStatistic: EventStatistic? = null

    @Before
    fun setUp() { clock.reset(); eventStatistic = EventRPMStatistic(clock) }

    @Test
    fun `increment test`() {
        eventStatistic!!.incEvent("Event")
        eventStatistic!!.incEvent("Event")
        eventStatistic!!.incEvent("AnotherEvent")
        assertThat(eventStatistic!!.getEventStatisticByName("Event")).isEqualTo(1.0 / 30)
    }

    @Test
    fun `mo' time test`() {
        eventStatistic!!.incEvent("Event")
        clock.addTime(1, ChronoUnit.MINUTES)
        assertThat(eventStatistic!!.getEventStatisticByName("Event")).isEqualTo(1.0 / 60)
        clock.addTime(1, ChronoUnit.HOURS)
        assertThat(eventStatistic!!.getEventStatisticByName("Event")).isZero
    }

    @Test
    fun `different events test`() {
        eventStatistic!!.incEvent("Stump pullin'")
        eventStatistic!!.incEvent("Stump pullin'")
        clock.addTime(30, ChronoUnit.MINUTES)
        eventStatistic!!.incEvent("Towin'")
        eventStatistic!!.incEvent("Towin'")
        clock.addTime(30, ChronoUnit.MINUTES)
        eventStatistic!!.incEvent("Muddin'")
        assertThat(eventStatistic!!.allEventStatistic).containsOnlyKeys("Towin'", "Muddin'")
        assertThat(eventStatistic!!.allEventStatistic).containsEntry("Towin'", 1.0 / 30)
        assertThat(eventStatistic!!.allEventStatistic).containsEntry("Muddin'", 1.0 / 60)
    }
}
