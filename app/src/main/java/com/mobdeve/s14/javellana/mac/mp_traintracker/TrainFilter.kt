package com.mobdeve.s14.javellana.mac.mp_traintracker

data class TrainFilter(
    var line: String? = null,
    var destination: String? = null,
    var status: String? = null
)

fun List<Train>.applyFilters(filter: TrainFilter): List<Train> {
    return this.filter { train ->
        (filter.line == null || train.line.equals(filter.line, ignoreCase = true)) &&
                (filter.destination == null || train.nextStation.equals(filter.destination, ignoreCase = true)) &&
                (filter.status == null || train.status.equals(filter.status, ignoreCase = true))
    }
}
