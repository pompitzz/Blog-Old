# MessageSource
> Message를 다국화할 수 있는 기능을 제공하는 Interface이다.

- ApplicationContext는 MessageSource 인터페이스를 상속받고 있기 때문에 ApplicationContext으로 사용할 수 있다.

### 예제

[##_ImageGrid|kage@ca1Kf4/btqAuwXllqd/gaAN2b2KuXaWPh4DSLSF8K/img.png,kage@bG2hxH/btqAt6xMB07/1662RzAFCbDtZGsEpMFE5k/img.png,kage@U9FPz/btqAt74xlqx/MRj6yIaBLUfQwxsNKJdrTk/img.png|data-origin-width="0" data-origin-height="0" style="width: 32.5558%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 34.0492%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 31.0695%;"|_##]
- resources폴더 안에 위와 같이 messages.properties를 만든다. ko\_KR에는 한국어, en에는 영어로 hello의 값을 입력한다.
- properties를 만들면 따로 폴더로 묶을 필요 없이 위와 같이 알아서 번들링이 된다.

[##_Image|kage@b6e0Iq/btqAs4AHWYZ/2vqBlKiQdSxIZ1moyPB18k/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@boS92l/btqAt7XNzCy/yBMGdpKZnofa9mSeikf2xk/img.png|floatLeft|data-origin-width="0" data-origin-height="0"|||_##]

- Locale 정보를 통해 hello의 message를 받아오면 Locale에 맞는 값이 출력되는 것을 알 수 있다.

[##_Image|kage@bgKA6f/btqArUewlKA/z2tID04AoOKgWNhSx9v1hK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 한글이 깨지는 경우 setting의 File Encodings에 들어가 위와 같이 UTF-8로 설정해주면 정상적으로 동작된다.

### MessageSource를 직접 설정하기

[##_Image|kage@ylqIq/btqAriNrfYz/PfP5BMUKFafDyHIgrkrko1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

-  위의 예제에서는 스프링 부트가 자동으로 설정해준 MessageSource를 사용하였지만 이렇게 직접 빈으로 등록할 수 있다.
- 기본적인 경로와 엔코딩을 설정하고 캐시시간도 설정이 가능하다.

<br>

[##_Image|kage@Xok07/btqAsSgeiXK/fmCFET109rkoKzlwbEsEA0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]
- Thread.sleep()을 통해 1초마다 meesage를 출력하게 한다.

<br>

[##_ImageGrid|kage@b4ZdnZ/btqAuxhEcmd/jqi7i6HO5BIXB4R0TPrCKK/img.png,kage@dmuorF/btqAsRVSAZL/KJ17XZOKmgB05kfJnyurW0/img.png|data-origin-width="0" data-origin-height="0" style="width: 51.4142%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 47.423%;"|_##]
- 앱을 구동시켜보면 1초마다 메시지가 출력된다.
- 앱이 구동중인 도중에 message의 값을 변경하였다.
- 그리고 프로젝트를 빌드하면 앱이 구동중에도 변경내용이 적용되는것을 알 수 있다.

---

[참고 자료](https://www.inflearn.com/course/spring-framework_core)
