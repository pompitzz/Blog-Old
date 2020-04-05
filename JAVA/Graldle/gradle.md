# 그래들 가이드 공부
### Creating New Gradle Builds
```java
gradle init // 프로젝트 생성

// 이런식으로 build.gradle에 task 설정후 ./gradlew copy로 실행하면 해당 설정이 실행됨.
task copy(type: Copy, group: "Custom", description: "Copies sources to the dest directory"){
	from "src"
	into "dest"
}

// 수많은 플러그인들이 존재하는데 플러그인들을 추가할 수 있음
plugins{
	id "base"
}

// 추가된 플러그인이 제공해주는 기능으로 task를 설정할 수 있다.
task copy(type: Copy, group: "Custom", description: "Copies sources to the dest directory"){
	from "src"
	into "dest"
}

./gradlew tasks // task 목록확인 가능
```
### Building Java Application
```Java
gralde init // 간단하게 Java Application 생성가능

// 빌드 시 먼저 ~/.gradle/cache에 이미 라이브러리들이 캐시되어 있는지 확인하고 이미 있다면 캐시를 이용하여 빌드한다.

open build/reports/tests/test/index.html // 빌드 내역을 얘로 쉽게 확인 가능
```
