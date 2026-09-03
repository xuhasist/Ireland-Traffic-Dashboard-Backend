# Ireland Traffic Dashboard 
Backend service for a full-stack traffic monitoring dashboard for Irish cities.

Built with Spring Boot 3.5.0, Kotlin 2.1.21, PostgreSQL 16, MongoDB 7, Redis 7, and Docker Compose, the service aggregates traffic and weather data into a normalized dashboard API consumed by a React frontend.

The dashboard provides traffic flow, incidents, weather conditions, summary metrics, charts, map visualizations, and dashboard snapshots.

## ✨ Backend Highlights
- Aggregates traffic, weather, incident, metric, and chart data through a unified dashboard API
- Integrates with TomTom Traffic and OpenWeather APIs
- Uses Redis to cache external API responses and reduce redundant requests
- Stores structured city configuration in PostgreSQL
- Stores nested dashboard snapshots in MongoDB
- Supports both mock and live data modes

## 📁 Project Structure

```
Ireland-Traffic-Dashboard-Backend
├── backend/Ireland-Traffic-Dashboard/src/main/kotlin/com/itd/
│   ├── common/          # Shared DTOs and common response models, such as API metadata
│   ├── config/          # Spring Boot configuration, including CORS, Redis cache, seed data, and HTTP clients
│   ├── dashboard/       # Aggregates weather, traffic, incidents, metrics, and chart data for the dashboard
│   ├── location/        # Manages cities and city configuration data stored in PostgreSQL
│   ├── snapshot/        # Manages dashboard snapshots stored in MongoDB
│   ├── traffic/         # Proxy layer for TomTom traffic flow and incident APIs
│   └── weather/         # Proxy layer for OpenWeather API
└── docker-compose.yml   # Defines local infrastructure services, including PostgreSQL, MongoDB, and Redis
```

Main data flow:
```
1. User selects a city and data mode in the React UI.
2. The frontend calls GET /api/dashboard?city={city}&mode={mock|live}.
3. The backend loads city configuration from PostgreSQL.
4. In live mode, the backend calls TomTom and OpenWeather APIs.
5. Redis caches external API responses to reduce repeated calls.
6. The backend calculates summary metrics and chart data.
7. The frontend receives a normalized dashboard response.
8. React state is updated and components re-render.
```

## 🗄️ Database and Storage

### PostgreSQL
PostgreSQL is used to store structured city and city configuration data, such as supported cities, map center coordinates, bounding boxes, and monitored road points.

### MongoDB
MongoDB is used to store dashboard snapshots because each snapshot contains nested and semi-structured dashboard data, such as weather data, traffic metrics, congestion breakdowns, and speed trend data.

### Redis
Redis is used as a cache layer for external third-party API responses, such as TomTom traffic data and OpenWeather weather data.
