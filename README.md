## hellomyteam 소개
회사에 가면 매일 아침 하는 인사관리 시스템을 나의 애정있는 나의 팀에 적용해보자는 취지에서 만들어졌습니다.

<br>
<br>


## ⚡ 기술 스택
- **Web backend:** Java, Gradle, Spring boot, Spring Security, JPA, Spring Data JPA,  Querydsl, Mysql
- **Infra:** EC2, CodeDeploy RDS, Mardia DB
- **Web frontend:** React, JS, HTML5, CSS3
- **Versioning:** CI/CD, Git, Git Flow, Git Hub
<br>
<br>


## ✍️ 깃 사용 방법
브랜치 종류
- main : 운영 서버용 브런치
- release: 개발 서버용, 테스트용 브런치
- develop : 코드 병합 및 테스트용 브런치
- feature : 새로운 기능 개발 브런치
<br> 

- Feature 브랜치 네이밍 규칙
    - {브랜치 종류}/{이름}/{{기능이름}}
    - ex) feature/cahnghyeon/login


<br>


## 🌠 서버 / DB 구성

➡️ 운영용 EC2(API 서버, 웹 서버) + RDS(1) / 개발용 EC2(API 서버, 웹 서버) + RDS(2)  

<br>
<br>

## 💬 고민거리

**서버구축의 고민거리(feat. 돈만 있었다면…)**
    
노트북에서 스프링부트와 리액트를 구동할 사양이 안되는 팀원이 있어 백엔드 API용 **개발 서버**를 구축해야 했습니다. 
    
금액을 최소화하면서 운영/개발용 서버 구축하는 방법에 대해 고민을 했고 최종적으로 하나의 EC2 당 API 서버와 웹 서버를 띄우기로 결정했습니다. 
    

    
이렇게 구성할 경우의 문제점은 하나의 EC2가 상태 검사 에러가 나서 다운될 경우 API 서버와 웹 서버 둘 다 접속 불가능한 문제가 발생할 수 있지만 돈을 아껴야하는 현재 상황에선 최선의 방법이 아닐까 생각합니다
(해당 프로젝트로 수익성이 난다면 EC2 한 개 당 하나의 서버를 구축하고 싶습니다.)

