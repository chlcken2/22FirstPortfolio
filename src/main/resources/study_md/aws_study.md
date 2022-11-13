AWS 배포를 위한 설정 및 명령어 정리
--
1. 인스턴스를 사용할 때 탄력 호스트를 이용한다.
2. 탄력IP 생성 즉시 미등록시 금액 발생
3. pem키를 받은 후 ssh 파일에 생성

# 기본 명령어 
1. ssh 디렉토리로 이동:  cd ~/.ssh/
2. pem키 권한 변경: chmod 600 ~/.ssh/pem 키 이름
3. config 설정: vim ~/.ssh/config 
4. HOSTNAME 설정: HOSTNAME = 탄력적IP 주소 할당 
5. HOST 이름 변경으로 손쉽게 접근 가능
6. Config파일 권한 설정: chmod 700 ~/.ssh/config 

# ec2 세팅 후 필수설정
1. java version 일치화

- java8버전 설치

    - sudo yum install -y java-1.8.0-openjdk-devel.x86_64
- java 버전 변경 
    - sudo /usr/sbin/alternatives --config java
- 버전 선택 및 확인
    - java -version
2. 타임존 설정
- 기존 시간 삭제
  - sudo rm /etc/localtime
- 서울 시간으로 변경
  - sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
5. 호스트 네임 변경
- 접속
  - sudo vim /etc/sysconfig/network
- HOSTNAME 추가완료
