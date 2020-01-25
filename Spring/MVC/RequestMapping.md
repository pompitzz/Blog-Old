# 요청 매핑하기
> 미디어 타입과 헤더, 매개변수에 맞게 요청을 매핑할 수 있다.

### **원하는 타입의 요청만 처리하기**
#### JSON 요청만 처리하고 싶을 경우

[##_Image|kage@cBrhwR/btqACPKxQdM/V5BAn5nzq4bO9fkIE2f1p0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="160" data-origin-width="0" data-origin-height="0"|||_##]

- Annotaiton에 consumes 설정으로 원하는 타입을 설정할 수 있다. VALUE는 String을 리턴한다는 의미이다.

[##_Image|kage@ec2tO5/btqACZ7gjen/Clm1360EHIqzFD1soX2FW0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="296" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@dQ5by1/btqACPqfVNB/xFB4swTE7Gxl1rUnCx8kn0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="148" data-origin-width="0" data-origin-height="0"|||_##]

- 테스트 코드에서 contentType을 JSON으로 설정하고 테스트하면 테스트가 성공한다.

- APPLICATION\_JSON, APPLICATION\_JSON\_VALUE 등 JSON으로만 설정하면 모두 가능하다.

#### **Content Type을 주지 않을 경우**

[##_Image|kage@bW0K68/btqADYzL869/76Tcz5nqxqNB9QoXlNa1o1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="282" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@8AlX9/btqAEsgkq1E/KC6g1KcfAHjuDIeawTagYK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="144" data-origin-width="0" data-origin-height="0"|||_##]

- Content-Type 헤더를 설정하지 않고 테스트 시 415(UnsupportedMediaType) 상태 응답이 발생한다.

---

### **원하는 타입으로 응답하기**

#### 응답을 TEXT로 할 경우

[##_Image|kage@ebQdW3/btqABRPPVAl/lFCKnOl0kmllncZOHSbKB1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="237" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@ba0rLZ/btqADZFqIwj/BssJ9PREwS1kyMxPIVBLC0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="319" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@brlpXs/btqACGtt6vI/bxedZFtr74Lh1kMzIpjbU0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="140" data-origin-width="0" data-origin-height="0"|||_##]

- 테스트에서 Accept 헤더를 TEXT로 설정하면 테스트가 정상 작동한다.
- Accept 헤더를 설정하지 않으면 아무거나 받는다는 뜻이므로 이 또한 테스트가 성공할 것이다.

#### **다른 Accept 헤더를 설정했을 때**

[##_Image|kage@C5wwi/btqADHLM8WG/TC15guRyUUMvwfQsrgY6aK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="313" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@c3ytX2/btqADjK8Zwy/Wp9SGyWdgLshOL3wskwQf0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="108" data-origin-width="0" data-origin-height="0"|||_##]

- Accept 헤더를 JSON으로 설정 후 요청을 보낼 시 406(Not Acceptable) 상태 응답이 발생한다.

### **Annotation 우선순위**

[##_Image|kage@dvkRRW/btqAB2Q6tRH/dayRBbM4TmAUKDVbT8V93k/img.png|widthContent|data-lazy-src="" data-width="693" data-height="270" data-origin-width="0" data-origin-height="0"|||_##]

- 클래스 단과 메서드단에 동시에 설정을 하게 되면 둘 다 적용되는 게 아닌 메서드에서 오버라이드 하기 때문에 JSON만 적용된다.

---

### **특정한 헤더 요청 처리**

#### **특정한 헤더 요청을 처리하고 싶을 때**

[##_Image|kage@bJxzlI/btqADiZKL72/XJNIcREysCF6TyLVcQvOFk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="170" data-origin-width="0" data-origin-height="0"|||_##]

- HOST 헤더 요청만 처리한다고 설정할 수 있다.

[##_Image|kage@0txu8/btqAEsm6Xog/2OzeL8PXNGPOCsJbpB1WZk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="317" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@4dcQW/btqADiS0KdA/qn7YLScvS02kDPf2AsF7z1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="113" data-origin-width="0" data-origin-height="0"|||_##]

- 테스트에 FROM 헤더 요청으로 보내면 테스트가 실패하게 된다.

#### **특정한 헤더 요청만 처리를 하고 싶지 않을 때**

[##_Image|kage@ckM3rW/btqAE7iHUhJ/ujoCG5tjT6MJnKQAmHS4bk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="175" data-origin-width="0" data-origin-height="0"|||_##]

- !를 이용하여 FROM 요청은 처리하지 않도록 하였다.

[##_Image|kage@dNKvUl/btqADjEoEU1/sKioLjRYWrQ2h3mbrHjVPK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="160" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@bYu2gQ/btqAE6xkES6/oJrhrBnuU6KEyUygWKkpe1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="83" data-origin-width="0" data-origin-height="0"|||_##]

- 테스트에서 헤더에 FROM을 넣고 요청하였더니 테스트가 실패하였다.​

#### **특정한 헤더 키/값이 있는 요청을 처리하고 싶은 경우**

[##_Image|kage@bhmpNJ/btqAErobFzN/9eBCDXkqYt8cMdny02JpyK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="157" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@bdWIQ1/btqACOLDJOV/USQcZj9JI4JgUHe96pMKPK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="170" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@do0Koe/btqAE7342yv/rWClp6neTWkAMqpKBLIay0/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 특정한 헤더 키값에 대한 요청 처리도 가능하다.

---

### **특정한 매개변수 요청 처리**

[##_Image|kage@nuLN3/btqABRvzIUT/GkfMLsrKtRZJOKz6GQm4zK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="170" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@xLedv/btqACZMTe2z/Qwuc0HbLo5NmfeiJvBKKVk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="171" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@0ccgO/btqADkXzD3K/uOeN9qaJW2NyFO0yZpchJk/img.png|alignCenter|data-origin-width="0" data-origin-height="0"|||_##]

- 헤더에서 설정한 것처럼 매개변수에도 똑같이 적용이 된다.

---

[참고자료](https://www.inflearn.com/course/%EC%9B%B9-mvc#)
