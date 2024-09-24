```mermaid
graph TD
    subgraph "프론트엔드 (React)"
        A[Login 컴포넌트] --> B[App 컴포넌트]
        B --> C[NewsList 컴포넌트]
        B --> P[결제 컴포넌트]
    end

    subgraph "백엔드 서비스"
        D[API 게이트웨이 :8070]
        E[Auth 서비스 :8080]
        F[News 서비스 :8090]
        Q[결제 서비스 :8100]
        R[User 서비스 :8110]
    end

    G[사용자] -->|모든 요청| D
    B -->|API 요청| D
    D -->|인증 요청| E
    D -->|뉴스 데이터 요청| F
    D -->|결제 요청| Q
    D -->|사용자 정보 요청| R

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
        S[Toss Payments]
    end

    E -.->|인증| H
    Q -.->|결제 요청 및 처리| S

    subgraph "데이터베이스"
        I[(News DB)]
        J[(User DB)]
        T[(Payment Log DB)]
    end

    E --> J
    R --> J
    Q --> T

    subgraph "결제 플로우"
        P -->|결제 요청| Q
        Q -->|결제 성공 시| U{결제 완료 이벤트}
        U -->|결제 완료 메시지| K
        K -->|결제 완료 메시지 전달| R
        R -->|멤버십 혜택 부여| J
        Q -->|결제 실패 시| V[결제 실패 처리]
        V -->|주문 상태 업데이트| T
        V -->|결제 실패 로그 저장| T
    end
