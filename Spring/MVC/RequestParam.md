> @PathVariable, @MatrixVariable, @RequestParam 에 대해 알아본다.

### @PathVariable
- 요청 URI 패턴의 일부를 핸들러 메서드 파라미터로 받을 수 있다.

[##_ImageGrid|kage@bFbD5v/btqAGX8H3Wx/GulkYezMwcorvp0uVLk3dK/img.png,kage@cdzM5S/btqAE6eu228/sCRQGh4RmYLSEwQywwI5g1/img.png|data-lazy-src="" data-width="325" data-height="201" data-origin-width="0" data-origin-height="0" style="width: 41.1217%; margin-right: 10px;",data-lazy-src="" data-width="556" data-height="245" data-origin-width="0" data-origin-height="0" style="width: 57.7155%;"|_##]
- @GetMapping에서 {id}부분을 메서드의 파라미터에 매핑시켜준다.
- 해당 파라미터에 @PathVariable을 붙이면 된다.
- 만약 /hello/23 이라는 요청이 들어오면 id에 23이 매핑될 것이다.

<br>

[##_Image|kage@c0SE0i/btqADHflqa2/630rrnhYvbzS5JUEKsguB0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="194" data-origin-width="0" data-origin-height="0"|||_##]

- 원래라면 @PathVariable에 url {id}와 같이 명시된 name을 위와 같이 적어 주어야 한다.
- 허나 타입 변환을 지원해주므로 주황색 박스의 변수명이 같으면 빨간색 줄을 생략할 수 있다.

#### 테스트

[##_Image|kage@bsfL1q/btqAHJoBhDO/fJNlCsOKsQWyU94Knh0RwK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="315" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@GkqWc/btqAHoydllu/TcrvvsOAWlYS3eYMs60Dd0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="160" data-origin-width="0" data-origin-height="0"|||_##]

- /hello/1로 요청을 보냈을 때 id에 1이 들어 간것을 알 수 있다.
- 만약 /hello 와 같이 값을 넣지 않으면 BadRequest 응답이 발생한다.
- @PathVariable(reque = false)로 설정하거나, 파라미터를 Optional로 설정하면 BadRequest가 발생하지 않는다.

---

### @MatrixVariable

- 요청 URI 패턴에서 **키/값 쌍의 데이터를 메서드 파라미터로 받는 방법**

[##_Image|kage@pEwZy/btqAGDQa4dS/zg9hpTHHjHILzDBEkLJbGK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="127" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@byRy9i/btqAFnG2Wba/2Ny5wyhkPNpTITZWPhKwK0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="143" data-origin-width="0" data-origin-height="0"|||_##]
- 이렇게 요청 url끝에 name=james로 보내면 String name = james가 된다.

[##_Image|kage@bDL5rB/btqADIyBVnk/XGr6bxXIsdrcgG5ANhqH91/img.png|widthContent|data-lazy-src="" data-width="693" data-height="157" data-origin-width="0" data-origin-height="0"|||_##]
- @PathVariable과 마찬가지로 타입변환을 지원해주므로 따로 값을 명시 하지 않아도 된다.
- 그리고 데이터가 없다면 BadRequest가 발생할 것이며 @PathVariable과 똑같은 설정을통해 해결이 가능하다.

​
#### 참고
[##_Image|kage@BRzhr/btqAGDvP6u2/aAlrWv7K39kJFiqNUJ2ZkK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="190" data-origin-width="0" data-origin-height="0"|||_##]
- 위의 Test 코드를 보면 세미콜론으로 url이 구문된 것을알 수 있다.
- 스프링에서는 기본으로 urlPath에 세미콜론은 제거하기 때문이 이렇게 따로 추가 설정이 필요하다.

---

### @RequestParam
- 요청 에 들어있는 **단순 타입** 데이터를 메서드 파라미터로 받아올 수 있다.

[##_ImageGrid|kage@52eTH/btqAHpqm1bp/vrlI5VBI9ecv6I2LqgvWHK/img.png,kage@pxwqr/btqAHHYCe4z/QSN4KGbcZ6mxoozn0SSEb0/img.png|data-lazy-src="" data-width="326" data-height="206" data-origin-width="0" data-origin-height="0" style="width: 38.6178%; margin-right: 10px;",data-lazy-src="" data-width="612" data-height="248" data-origin-width="0" data-origin-height="0" style="width: 60.2194%;"|_##]
- @RequestParam을 이용하면 단순 타입들을 받아올 수 있다.
- @RequestParam을 생략하여도 알아서 매핑을 해주긴 하지만 명시적으로 적는게 좋다.
- 메서드 파라미터 변수명이 Request Param의 name 속성과 일치하면 매칭을 해준다.


#### Test
[##_Image|kage@VzFvQ/btqAE7doAtY/kH98sP4GdUULaO8vTrrc5K/img.png|widthContent|data-lazy-src="" data-width="693" data-height="303" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@bEqCTM/btqAEsIOsUf/48mM7zdGIh3KSHDLXPBhsK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="158" data-origin-width="0" data-origin-height="0"|||_##]

- 위와 같이 param 설정을통해 파라미터로 전달이 가능하다.
- 테스트 결과를 보면 name에 james가 들어간 것을 알 수 있다.
- 만약 @RequestParam이 있는데 값이 없다면 BadRequest가 발생한다.
- 아래와 같이 required와 defalutValue를 설정할수도 있고 Optional로 받아올 수도 있다.

[##_Image|kage@dQlbS6/btqAGCKsVI0/MSLe7ozoTLI1JbLBmFjnz0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="92" data-origin-width="0" data-origin-height="0"|||_##]

<br>

[##_Image|kage@zoce8/btqADYunEKi/FyxiUEI9sUvBRhcYJ93N61/img.png|widthContent|data-lazy-src="" data-width="693" data-height="109" data-origin-width="0" data-origin-height="0"|||_##]

-  Map<String, String> 또는 MultiValueMap<String, String>를 통해 모든 요청 파라미터를 받아올 수 있다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc)
