# 베이스는 pgvector
FROM pgvector/pgvector:pg16

# PostGIS 설치
RUN apt-get update && apt-get install -y \
    postgis postgresql-16-postgis-3 postgresql-16-postgis-3-scripts \
 && rm -rf /var/lib/apt/lists/*
