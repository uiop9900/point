# 목차

1. 스펙
2. 패키징 구조
2. DB 구조
3. 구현한 API
4. 사용해본 기술
5. 테스트


## 1. 기술 스펙
- 프로그래밍 언어: `Java 17`
- 프레임워크: `Spring Boot 3.1.4`
- 빌드: `Gradle`
- 컨테이너: `Docker`


## 2. 패키징 구조
[interface] -> [facade]-> [domain] <- [infrastructure]

```
src/main/java
└── com
    └── jia
        └── point
            ├── [common]
            │   ├── annotation
            │   ├── config
            │   ├── converter
            │   └── handler
            ├── [domain]
            │   ├── dtos
            │   ├── entity
            │   ├── enums
            │   └── exceptions
            ├── [facade]
            ├── [infrastructure]
            └── [interfaces]
                ├── dtos
                └── enums

```

## 3. DB 구조
| 테이블   | MEMBER                                    | POINT                                                                                                                 | POINT_HISTORY                                     | POINT_HST_RECORD                                  |
|-------|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|---------------------------------------------------|
| 정의    | 사용자                                       | 최초 적립시, 포인트                                                                                                           | 포인트 `적립`, `만료`, `사용`, `취소` 시 다 쌓이는 히스토리 테이블       | `POINT`와 `POINT_HISTORY`의 중간테이블 (포인트취소를 위해 생성)    |
| 주요 컬럼 | - name: 회원 이름  <br/> - phoneNumber: 핸드폰번호 | - originValue : 최초 적립시 포인트<br/> - remainValue: 사용가능한 포인트<br/>- UseStatus: 포인트 사용 상태 (`미사용`, `사용중`, `취소`, `사용완료`, `만료`) | - PointUseType: 포인트 사용 타입(`적립`, `사용`, `만료`, `취소`) | - Point<br/> - PointHistory - useVale: 실사용 포인트 저장 |


## 4. 구현한 API
- <b>회원가입</b>
  - 사용자를 등록하는 API
- <b>포인트 적립</b>
  - 포인트를 쌓는 API
  - 최초로 쌓을때 등록된다. 해당 point를 전체사용, 부분사용이 가능하다.
- <b>포인트 사용</b>
  - 포인트를 사용하는 API
  - 포인트 사용시에 남은 포인트 금액에 따라 해당 포인트의 상태가 결정된다.(`사용중`, `사용완료`)
  - 해당 포인트의 사용 가능한 금액은 point_remainAmt에 저장된다.
- <b>포인트 취소(롤백)</b>
  - 포인트를 취소한다 는 건 사용목록을 확인하고 취소한다. -> 포인트를 사용한대로 db에 쌓는 point_hst를 request로 받는다.
  - 해당 포인트를 찾아서 만료시일을 확인해 해당 포인트를 원복(`취소`) 혹은 만료시킨다.
- <b>포인트 만료</b>
  - 매일 자정에 만료시일이 된 포인트들을 만료시킨다.
- <b>사용자별 포인트 조회(페이징)</b>
  - 사용자별로 포인트를 얼마나 사용하고 적립했는지 목록을 페이징해 보여준다.

## 5. 사용해본 기술
- 레디스
  - 동시성 이슈(Redis Lock)
  - 캐싱
- ExceptionHandler
- 공통 Response
- 페이징
- jmeter


## 6. 테스트
- 동시성 제어 테스트 (w. jmeter)
  - 100명의 사용자가 동시에 `적립`과 `사용`을 하는 경우
  - 정상적으로 락 발동 -> 하나씩 잘 쌓인다.
    - ![스크린샷 2023-12-07 오후 9.19.47.png](..%2F..%2F..%2F..%2Fvar%2Ffolders%2F_z%2Fr2vft7pd27d37djvf7wsr3dr0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_G8y0Yn%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202023-12-07%20%EC%98%A4%ED%9B%84%209.19.47.png)
