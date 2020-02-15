# @InitBinder
> 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용한다.

### 바인딩 설정
####  event의 id값은 데이터 바인더를 통해 받아오지 않게 하기

[##_Image|kage@61PrZ/btqAm4nUeKv/2Qdww2MKxBf8UZle9WZPGk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@zsQXO/btqAnnU2cR8/zmTitKah3nNIVhSRGpQWh1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 이벤트 클래스와 resources/templates/events/ 에 form.html을 이렇게 작성하였다.

<br>

[##_Image|kage@bgCQ4r/btqAnYHfrif/8HpOOcso6Ij5TJ59Tki7K1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 모델을 통해 뷰에게 넘겨주고 그것을 Postmapping으로 받아오는 컨트롤러를 작성하였다.

<br>

[##_Image|kage@cEmKYQ/btqAm4g512s/8jwSl7hJ7zNX2C9Yd4IAy1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@LwPBy/btqAovEONr4/g2q69LGEHOeMkkLOlKNnwK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 디버그 모드로 웹을 띄우고 event값을 확인하면 모든 값이 제대로 전달된 것을 알 수 있다.

#### 데이터 바인더 적용

[##_Image|kage@bN0wVK/btqAm3P13hm/ZhO2q2WO77rsGS01NKkKkK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- @InitBinder를 이용해 데이터 바인더를 적용한 후 id를 setDisallowedFields에 추가한다.

<br>

[##_Image|kage@ciYlql/btqAlX3QTOO/odL1BwSwFrKr56Rwzzqhw0/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 그리고 동일하게 디버그 모드로 웹을 띄워서 데이터를 전송해보면 id값은 걸러진 것을 알 수 있다.
- 아래와 같이 InitBinder("event")라고 적으면 event의 Attribute에만 데이터 바인더를 적용할 수 있다.

[##_Image|kage@qruYi/btqAo65TsLA/u7byi83ZOKEvCnocvX9GOk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

---

### 포매터 설정

- 원하는 포매터를 만들고 webDataBinder.addCustomFormatter()를 통해 추가할 수 있다.
- 기본적으로 포매터가 추가되어 있는 LocalDate로 확인할 수 있다.

<br>

[##_Image|kage@bkrUEj/btqAlYhnHO8/Q8d2zdSkP6k2N0QxVUnbm1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@cB8LXz/btqAm4VIRXX/3quqnrkXmk8F7XOe0WBec0/img.png|widthContent|data-origin-width="0" data-origin-height="0"|||_##]

- 주석처럼 pattern으로 할 수 있지만 ISO를 이용해 Type-Safe 하게 사용할 수 있다. 

<br>

[##_Image|kage@b3rjse/btqApIXLE3g/djLsrZSjk3K2chwtX2eS2K/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bZqtR5/btqApIcpZdT/qQaWg9NvcklNJLZGlPHvUk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- form.html에 startDate를 추가하고 디버그 모드로 실행시켜 데이터를 전송해보면 이렇게 String이 아닌 LocalDate 타입으로 값이 변한 된 것을 알 수 있다.
- 기본적으로 추가되어 있는 포매터가 이렇게 해주는 것이다.

---

### Validator 설정

- 원하는 Validator를 설정해보자.

[##_Image|kage@BmT85/btqAowjsD9b/4VYvQMIxXhaRqCSnvUnUx1/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- Validator를 구현하는 EventValidator를 만든다. validate()에서 직접 검증을 할 수 있다. event의 name이 aaa일 경우 에러를 추가하였다.

<br>

[##_Image|kage@boxwyp/btqApHEyRFc/SFd9B0Y9ZGpD5AIb9xl55K/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bwZ4ZE/btqAlZAyDui/8u8lujf60l7NTvcc4C8fTK/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- InitBider에 Validaor를 추가하고 디버그 모드로 돌려본다.

<br>

[##_Image|kage@kEG14/btqAnxpDnqk/JQFMu6bGYPtVYAbCLa9Wz0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@b1i8gZ/btqAmumOK7u/2GKFhmp2IFMTDLptkBNXK0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- name에 aaa를 넣고 데이터를 전송하면 이렇게 검증이 이루어지는 것을 알 수 있다.

#### Validator를 빈으로 등록하여 사용하기

[##_Image|kage@83Dl2/btqAnomakTD/wTKZBMrusvGR8qk8QmEH50/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- validator를 구현하지 않고 그냥 이렇게 빈으로 등록 후 검증 메서드를 만든다.

<br>

[##_Image|kage@lH0P6/btqApkplxhP/EFH3cNrRCyjSwWLaHklux0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@bnm2Mb/btqAlY2KjmL/zy2hBQfiqi8Ok0nLZnyExk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- Controller에서 Bean으로 등록한 EventValidator를 통해 따로 InitBinder에 추가하지 않고도 사용이 가능하다.

---

[참고자료](https://www.inflearn.com/course/%EC%9B%B9-mvc)
