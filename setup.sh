#!/bin/bash

# Kafka 모니터링 환경 설정 스크립트

echo "Kafka 모니터링 환경을 설정합니다..."

# 필요한 디렉토리 생성
mkdir -p grafana/provisioning/datasources
mkdir -p grafana/provisioning/dashboards
mkdir -p grafana/dashboards

# Prometheus 설정 파일 생성
cat > prometheus.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'kafka-exporter'
    static_configs:
      - targets: ['kafka-exporter:9308']
    scrape_interval: 10s
    metrics_path: /metrics
EOF

# JMX Exporter 설정 파일 생성
cat > jmx-config.yml << 'EOF'
startDelaySeconds: 0
ssl: false
lowercaseOutputName: false
lowercaseOutputLabelNames: false

rules:
  - pattern: kafka.server<type=(.+), name=(.+)><>Value
    name: kafka_server_$1_$2
    type: GAUGE

  - pattern: kafka.log<type=LogSize, name=Size, topic=(.+), partition=(.+)><>Value
    name: kafka_log_size
    type: GAUGE
    labels:
      topic: "$1"
      partition: "$2"

  - pattern: kafka.consumer<type=(.+), name=(.+), client-id=(.+)><>Value
    name: kafka_consumer_$1_$2
    type: GAUGE
    labels:
      client_id: "$3"

  - pattern: kafka.producer<type=(.+), name=(.+), client-id=(.+)><>Value
    name: kafka_producer_$1_$2
    type: GAUGE
    labels:
      client_id: "$3"

  - pattern: java.lang<type=Memory><HeapMemoryUsage>used
    name: jvm_memory_heap_used
    type: GAUGE

  - pattern: java.lang<type=Memory><NonHeapMemoryUsage>used
    name: jvm_memory_nonheap_used
    type: GAUGE
EOF

# Grafana 데이터소스 설정
cat > grafana/provisioning/datasources/datasource.yml << 'EOF'
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090
    isDefault: true
    editable: true
EOF

# Grafana 대시보드 프로비저닝 설정
cat > grafana/provisioning/dashboards/dashboard.yml << 'EOF'
apiVersion: 1

providers:
  - name: 'kafka-dashboards'
    orgId: 1
    folder: 'Kafka Monitoring'
    type: file
    disableDeletion: false
    updateIntervalSeconds: 10
    allowUiUpdates: true
    options:
      path: /var/lib/grafana/dashboards
EOF

# 간단한 Kafka 대시보드 생성
cat > grafana/dashboards/kafka-overview.json << 'EOF'
{
  "dashboard": {
    "id": null,
    "title": "Kafka Overview",
    "tags": ["kafka"],
    "style": "dark",
    "timezone": "browser",
    "panels": [
      {
        "id": 1,
        "title": "Kafka Topics",
        "type": "stat",
        "targets": [
          {
            "expr": "kafka_topics",
            "refId": "A"
          }
        ],
        "fieldConfig": {
          "defaults": {
            "mappings": [],
            "thresholds": {
              "steps": [
                {"color": "green", "value": null}
              ]
            },
            "color": {"mode": "palette-classic"},
            "custom": {"displayMode": "list", "orientation": "horizontal"}
          }
        },
        "gridPos": {"h": 8, "w": 12, "x": 0, "y": 0}
      },
      {
        "id": 2,
        "title": "Message Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(kafka_topic_partition_current_offset[5m])",
            "refId": "A"
          }
        ],
        "gridPos": {"h": 8, "w": 12, "x": 12, "y": 0}
      }
    ],
    "time": {"from": "now-1h", "to": "now"},
    "refresh": "30s"
  }
}
EOF

echo "설정 파일이 생성되었습니다!"
echo ""
echo "사용법:"
echo "1. docker-compose up -d 로 서비스 시작"
echo "2. Grafana: http://localhost:3000 (admin/admin)"
echo "3. Prometheus: http://localhost:9090"
echo "4. Kafka: localhost:9092"
echo ""
echo "토픽 생성/확인:"
echo "docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list"
echo "docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic --partitions 3 --replication-factor 1"
echo ""
echo "메시지 생산/소비 테스트:"
echo "docker exec -it kafka kafka-console-producer --bootstrap-server localhost:9092 --topic test-topic"
echo "docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning"