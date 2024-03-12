# f1-Orury-Backend
당신만의 클라이밍 커뮤니티 [Orury](https://orury.com/)

## 👥 Contributors

<table>
  <tbody>
    <tr>
    <td align="center">
        <a href="https://github.com/kkkapuq">
          <br>
          <img src="https://avatars.githubusercontent.com/u/44130863?v=4" width="130px;" alt=""/>
          <br /> <sub><b>조형준</b><br></sub>
        </a>
    </td>
    <td align="center">
        <a href="https://github.com/oxix97">
          <br>
          <img src="https://avatars.githubusercontent.com/u/72330632?v=4" width="130px;" alt=""/>
          <br /><sub><b>이종찬</b></sub>
        </a>
        <br />
    </td>
    <td align="center">
        <a href="https://github.com/GBGreenBravo">
          <br>
          <img src="https://avatars.githubusercontent.com/u/147565215?v=4" width="130px;" alt=""/>
          <br /><sub><b>김민협</b><br></sub>
        </a>
    </td>
    <td align="center">
        <a href="https://github.com/yejincode">
          <br>
          <img src="https://avatars.githubusercontent.com/u/69861207?v=4" width="130px;" alt=""/>
          <br /><sub><b>송예진</b></sub>
        </a>
        <br />
    </td>
    </tr>
  </tbody>
</table>


## 프로젝트 소개
클라이밍 암장 정보와 리뷰를 한눈에!

클라이머들과 소통하며 함께 성장하세요!

| 로그인 페이지 | 메인 페이지 |
| --- | --- |
| ![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/db9fab54-1d90-451d-a64a-1fcc4ddccbe7) | ![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/6ac61341-0b04-4a20-ad4e-a4c61dd438ea) |
| OAuth 기반(Kakao, Apple) 회원가입, 로그인을 제공합니다.  | 암장 정보, 커뮤니티 게시판, 크루 등의 서비스에 접근합니다. |

| 지도 페이지 | 리뷰 페이지 |
| --- | --- |
| ![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/cd12a52d-f347-45db-a575-3ceb23a01742) | ![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/87fea6eb-7332-4616-ae04-0629596ec733) |
| 지도 기반으로 암장 정보를 제공합니다. 암장 세부 정보(운영 시간, 세팅 요일 등)를 확인할 수 있습니다.  | 암장에 대한 평균 별점과 리뷰를 확인할 수 있습니다. 리뷰 작성 및 반응 남기기 기능이 제공됩니다.  |

| 검색 페이지  | 커뮤니티 페이지  |
| --- | --- |
| ![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/9d7ce3dd-645f-4034-bec7-0f66c8b84c58) | ![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/da15d6c3-ee45-459b-94d5-6e957c787b31) |
| 암장명, 주소 등으로 암장을 검색합니다.  | 인기게시판, 자유게시판 , QnA 게시판에서 게시글, 댓글, 좋아요 남기기 등의 커뮤니티 활동을 제공합니다. |


## 아키텍처
- 멀티모듈 
![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/12d3bd85-0784-4448-abf1-01a03b2aabb4)

- 인프라
![image](https://github.com/Kernel360/f1-Orury-Backend/assets/69861207/000dc3c8-3f29-46a4-ab81-0dd01a9a7004)


## 개발 기록들
- CI/CD 배포방식
- JWT 토큰 인증 & OAuth 인증 로그인/회원가입
- [DB replication 및 라우팅](https://yejin-code.tistory.com/17)
- DDD 아키텍처 리팩토링
- BDD 기반 테스트코드 작성
- Spring Batch를 활용한 Kakao API 호출
- NginX를 활용한 로드밸런싱 및 리다이렉팅
- Prometheus, Grafana를 활용한 모니터링 시스템 구축

## 팀 문화
- 스크럼 , 회고
- 브랜치 전략
- 코드 리뷰
- API Docs (노션, swagger)



