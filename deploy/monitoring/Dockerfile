# 1. 공식 프로메테우스 이미지를 베이스로 사용
FROM prom/prometheus:latest

# 2. 로컬의 prometheus.yml 파일을 컨테이너 내부의 기본 설정 경로로 복사
# 이 과정이 수행되어야 우리가 작성한 설정이 반영됩니다.
COPY prometheus.yml /etc/prometheus/prometheus.yml

# (선택) 프로메테우스가 데이터를 저장할 경로를 지정 (기본값 사용)
VOLUME [ "/prometheus" ]

# 3. 9090 포트를 외부로 노출하겠다고 명시
#EXPOSE 9090