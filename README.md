[![CI](https://github.com/Young-Flow/server/actions/workflows/CI.yml/badge.svg)](https://github.com/Young-Flow/server/actions/workflows/CI.yml)
[![CD](https://github.com/Young-Flow/server/actions/workflows/CD.yml/badge.svg)](https://github.com/Young-Flow/server/actions/workflows/CD.yml)

# 숏폼 중심 스타트업 크라우드펀딩 웹 Pitchain BE
![메인](https://github.com/user-attachments/assets/4f288f4f-4bea-4133-b8b9-a3e9395ed1d1)

## 프로젝트 소개
- Pitchain은 숏폼 형식의 스타트업 로켓 피치 영상을 제공하여 개인 사용자가 스타트업을 탐색하고, 크라우드펀딩 형식으로 투자할 수 있는 웹 플랫폼입니다.
- 개인 사용자는 관심 있는 스타트업에 좋아요 누르기, 스크랩, 댓글 달기, 투자하기 등 여러 활동을 할 수 있습니다.
- 기업 사용자는 숏폼 형식의 로켓 피치 영상, PPT 형식의 기업 소개 자료 등을 업로드하고 펀딩을 열 수 있습니다. 또한 현재 펀딩의 진행 상황을 대시보드를 통해 확인할 수 있습니다.

## 백엔드 팀원 소개 
|[이승헌](https://github.com/lsh2613)|[정윤우](https://github.com/jeongmallro)|
|------|---|

## 개발 환경
- 프로그래밍 언어: Java 17
- 웹 프레임워크: Spring Boot 3.3.7
- 데이터베이스: MySQL, Redis
- ORM: JPA, Querydsl
- 클라우드 서비스: AWS(EC2, S3, RDS, CloudFront, Lambda, CodeDeploy, ElastiCache)
- CI/CD 툴: GitHub Actions
- 오픈소스 라이브러리: FFmpeg
- 로깅 툴: Sentry

## 개발 기간
- 2024.12 ~ 현재 진행중

## 성과
- 2025.02.07 미래내일 일경험 프로젝트형 IT 직무 3기 대상 수상

## 주요 기술
- AWS Lambda와 FFmpeg을 이용한 숏폼 업로드 과정 설계 (https://github.com/Young-Flow/server/issues/65)
![스트리밍](https://github.com/user-attachments/assets/2cdf5800-c019-421a-bedb-3c6fa5424b85)
- Redis와 HashMap을 이용한 비영업일 고려한 일환율 업데이트 로직 (https://github.com/Young-Flow/server/issues/42)

## 주요 논의
- API 분리 (https://github.com/Young-Flow/server/issues/25)
- 영상 업로드 과정에서의 사용자 기능 (https://github.com/Young-Flow/server/issues/129)
- 조회수 중복 처리 로직 (https://github.com/Young-Flow/server/issues/125)
- 계층을 가진 댓글 조회 (https://github.com/Young-Flow/server/issues/55)

## ERD
![ERD](https://github.com/user-attachments/assets/bde9d6f4-7ea9-4111-b7f0-621ab8f12acf)
