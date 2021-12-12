import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit.HOURS


/**
 * @author atsutsuiev
 */
class EventRPMStatistic(private val clock: Clock) : EventStatistic {
    private val events = mutableMapOf<String, MutableList<Instant>>()

    override fun incEvent(name: String) {
        if (!events.containsKey(name)) events[name] = ArrayList()
        events[name]!!.add(clock.instant())
    }

    override fun printStatistic() { allEventStatistic.forEach {(name, rpm) -> println("$name: rpm $rpm") } }

    override fun getEventStatisticByName(name: String): Double {
        retainRecent()
        return if (events.containsKey(name) && events[name]!!.isNotEmpty()) events[name]!!.size / 60.0 else 0.0
    }

    override val allEventStatistic: Map<String, Double>
        get() {
            retainRecent()
            return events.mapValues { if (events.containsKey(it.key)) events[it.key]!!.size / 60.0 else 0.0 }
        }

    private fun retainRecent() {
        clock.instant().minus(1, HOURS).also { i ->
            events.values.forEach { it.removeIf { instant -> !instant.isAfter(i) } }
        }
        events.entries.removeIf { entry -> entry.value.isEmpty() }
    }
}
