```mermaid
graph TD
subgraph "프론트엔드 (React)"
A[Login 컴포넌트] --> B[App 컴포넌트]
B --> C[NewsList 컴포넌트]
end

    subgraph "백엔드 서비스"
        D[API 게이트웨이 :8070]
        E[Auth 서비스 :8080]
        F[News 서비스 :8090]
    end

    G[사용자] -->|모든 요청| D
    B -->|API 요청| D

    D -->|인증 요청| E
    D -->|뉴스 데이터 요청| F

    subgraph "메시지 브로커"
        K[RabbitMQ]
    end

    subgraph "작업 스케줄러"
        L[Spring Scheduler]
    end

    L -->|갱신 요청| K
    K -->|뉴스 갱신 메시지| F

    subgraph "뉴스 서비스 상세"
        F -->|1. 뉴스 검색| M[Bing 검색 API]
        M -->|검색 결과| F
        F -->|2. 뉴스 요약 및 태그 생성| N[OpenAI GPT API]
        N -->|요약 및 태그| F
        F -->|3. 처리된 뉴스 저장| I[(News DB)]
    end

    subgraph "외부 서비스"
        H[Google OAuth]
    end

    E -.->|인증| H

    subgraph "데이터베이스"
        I[(News DB)]
        J[(User DB)]
    end

    E --> J
