# 빈의 스코프
> 스프링에서는 빈의 스코프를 싱글톤, 프로토 타입등으로 설정이 가능하며 그에 따라 속해있는 빈들이 상태가 변경될 수 있다.

### **싱글톤**

[##_ImageGrid|kage@t5pB2/btqAnY15PQC/z51y2zilW476uK0X56kxF0/img.png,kage@b0cgM0/btqApHLYfjX/p00wHqwJyI84Pj0eETzAd1/img.png|data-origin-width="0" data-origin-height="0" style="width: 47.0396%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 51.7976%;"|_##]

- 빈을 등록하면 기본값이 싱글톤이므로 싱글톤으로 등록이 된다.
- Single, Proto를 우선 @Component를 통해 빈으로 등록하였다.

[##_Image|kage@v1gXX/btqApkDpPQb/dLVus59qUc4YZv9S6Rxpak/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@0sDze/btqAqg1B59C/pMWy3QXEbi9fOKxPPYgRV1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 아무 설정을 하지 않았으므로 Proto는 현제 싱글톤 객체이다.
- 출력을 확인해보면 Single에서 가져온 Proto와 일반 Proto와 같은 인스턴스인것을 알 수 있다.
- 싱글톤 객체를 사용할 때는 프로퍼티가 공유되므로 **Multi Thread 환경에서 Thread-safe 하지 않다.**

---

### 프로토타입

[##_Image|kage@bAflew/btqApH6d7rW/19Y7JBIoZMLVpclAzBA8c0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_ImageGrid|kage@9z1GZ/btqAowK32oG/S1tMPodl13O1RJfC6VkJ7K/img.png,kage@eC9j3C/btqAovFpTDn/4koMvrEtSLwZ4LB2zczo9K/img.png|data-origin-width="0" data-origin-height="0" style="width: 68.0217%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 30.8155%;"|_##]

- Proto를 @Scope를 통해 prototype으로 지정한다.
- Bean을 여러 개 출력해보면 싱글톤은 같은 인스턴스가 출력되나 프로토타입은 다른 인스턴스가 출력되는 것을 알 수 있다.

### 프로토타입 사용시 주의할 점
#### 싱글톤 빈이 프로토타입 빈을 가지고 있을 때
- 현재 Single은 싱글톤이고 Proto는 프로토타입이다.
- 그렇다면 Single이 가지고 있는 Proto는 뭐가 될 까?


[##_Image|kage@xYmO4/btqAqgtNkBE/ADvxnPYURgALZITtNYhC61/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@qDf9d/btqAq2vin9E/TR06Htw0BP5Vg725EcWHK0/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]
- 출력을 확인해보면 프로토타입 임에도 불구하고 같은 인스턴스가 출력된다.
- **즉 프로토타입의 빈이 변경되지 않게 된 것이다.**

#### 해결 방법

[##_Image|kage@cWfXlN/btqAo7xy1GV/pNtNTIfcdCVOzAB6z77SeK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bB64dK/btqApjR3DvO/xTNGvTNrv4Z9EVIYz4SPG1/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- proxyMode를 TARGET_CLASS로 설정하면 제대로 동작한다.
- Single이 Proto를 직접 참조하는게 아닌 프록시를 통해 참조하게 되기 때문이다.
- 그러므로 프로토타입의 빈은 제 역할을 할 수 있게 된다.


> 원래 JAVA 기반의 프록시는 인터페이스 기반의 프록시만 만들 수 있다.
>스프링이 제고아는 TARGET\_CLASS를 통해 클래스 기반의 프록시를 만들어 준 것이다

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
