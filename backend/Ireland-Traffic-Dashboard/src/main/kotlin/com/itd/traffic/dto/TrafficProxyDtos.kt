package com.itd.traffic.dto

data class RoadPointRequest(
    val name: String,
    val lat: Double,
    val lng: Double,
)

data class FlowPointDto(
    val lat: Double,
    val lng: Double,
)

data class FlowLinkDto(
    val points: List<FlowPointDto>,
)

data class TrafficLocationDto(
    val description: String,
    val shape: TrafficShapeDto,
)

data class TrafficShapeDto(
    val links: List<FlowLinkDto>,
)

data class TrafficCurrentFlowDto(
    val speed: Int,
    val freeFlow: Int,
    val jamFactor: Double,
    val traversability: String,
)

data class TrafficSegmentDto(
    val location: TrafficLocationDto,
    val currentFlow: TrafficCurrentFlowDto,
)

data class TrafficFlowResponseDto(
    val results: List<TrafficSegmentDto>,
)

data class IncidentDetailsDto(
    val id: String,
    val type: String,
    val criticality: String,
    val description: String,
    val startTime: String,
    val endTime: String,
)

data class IncidentLocationDto(
    val shape: TrafficShapeDto,
    val description: String,
)

data class IncidentImpactDto(
    val delayInSeconds: Int,
    val affectedRoads: List<String>,
)

data class IncidentDto(
    val incidentDetails: IncidentDetailsDto,
    val location: IncidentLocationDto,
    val impact: IncidentImpactDto,
    val icon: String,
)

data class IncidentResponseDto(
    val results: List<IncidentDto>,
)

data class TrafficFlowRequest(
    val roads: List<RoadPointRequest>,
)

