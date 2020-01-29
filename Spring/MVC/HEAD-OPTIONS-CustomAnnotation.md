> 스프링 웹 MVC에서 기본으로 지원해주는는 HEAD, OPTIONS HTTP Method를 기본으로
지원해준다.

- HEAD, OPTIONS Method에 대한 설명은 [이전 게시글](https://sun-22.tistory.com/41)에서 작성하였다.

### HEAD
- HEAD 메서드는 GET과 동일한 요청을 하지만 응답에 본문이 없고 헤더만 존재한다.

[##_Image|kage@nIiiW/btqAE7C0QjA/86y1LWYS2bDiQ1WSVpukp0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="167" data-origin-width="0" data-origin-height="0"|||_##]

[##_Image|kage@et5ssX/btqACOrjByc/Scyjxjwga77EZQBYrPQcyK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="134" data-origin-width="0" data-origin-height="0"|||_##]

- 결과를 확인해보면 Body에 아무 정보가 없는것을 알 수 있다.

---

### OPTIONS
- OPTIONS로 요청은 여러 가지 종류의 지원 범위를 응답해준다.

[##_Image|kage@bvFRpq/btqADkpLaUS/k8i9sSaLtaEbkKowguy0y1/img.png|widthContent|data-lazy-src="" data-width="693" data-height="177" data-origin-width="0" data-origin-height="0"|||_##]

- @GetMapping을 통해 GET 메서드만 허용하도록 설정하였다.
- 만약 여기에 OPTIONS 요청을 보내면 Allow Method에는 GET만 포함될 것이다.

[##_Image|kage@GadMR/btqACOkxdC6/MuU5ijTCSmBPB97rgZXndK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="144" data-origin-width="0" data-origin-height="0"|||_##]
[##_Image|kage@bPqB8X/btqAC0kNaNa/HcA7sdxVyai2q9HBBd2ysk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="148" data-origin-width="0" data-origin-height="0"|||_##]
- 결과를 확인 해보면 GET이 들어가 있는것을 알 수 있다.
- HEAD, OPTIONS는 Spring에서 기본으로 제공하여 들어 있는 것 이다.

#### 모든 HTTP Method 허용시

[##_Image|kage@K2rOK/btqACQio7tA/kXSanJWVQIbjfQC7ULzBjK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="164" data-origin-width="0" data-origin-height="0"|||_##]
- @RequestMapping을 사용하면 모든 메서드를 허용하는것을 [이전 게시글](https://sun-22.tistory.com/41)에서 알아 보았다.
- 그러므로 OPTIONS 요청을 보내면 모든 메서드가 응답될 것이다.

[##_Image|kage@bdpLCx/btqAE7Xjkc6/xDAs3GkQ4oSIGOU012Kwj0/img.png|widthContent|data-lazy-src="" data-width="693" data-height="230" data-origin-width="0" data-origin-height="0"|||_##][##_Image|kage@crzAv0/btqAB2ji3eK/Ak7YQSH0XZvpwok4wUPokK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="179" data-origin-width="0" data-origin-height="0"|||_##]
- 헤더의 Allow에 모든 Method들이 들어있는 것을 확인할 수 있다.

---

[참고 자료](https://www.inflearn.com/course/%EC%9B%B9-mvc#)

# **Custom Annotation 만들기**

Custom Annotation을 만들기 위해 Annotation에 대해 알아보자.

#### **Meta Annotation**

 - **Annotation에 사용할 수 있는 Annotation이다.**

 - 스프링이 제공하는 대부분의 Annotation은 메타 Annotation으로 사용할 수 있다.

#### **Composed Annotation**

 - **한 개 혹은 여러 Meta Annotation을 조합해서 만든 Annotation**

 - 코드를 간결하게 줄일 수 있다.

 - 보다 구체적인 의미를 부여할 수 있다.

[##_Image|kage@biLCos/btqACZ7gPFG/BSvFVkWfn79zzxHpKESjsk/img.png|widthContent|data-lazy-src="" data-width="693" data-height="106" data-origin-width="0" data-origin-height="0"|||_##]

GetMapping Annotation을 살펴보자.

주황 박스 부분이 GetMapping Annotation 위에 사용되는 **Meta Annotation 들이다.**

그리고 GetMapping이 여러 Meta Annotaiton으로 만든 **Composed Annotation**이다.

### **기본 Meta Annotaion들**

#### **  1. @Retention**

   - 해당 Annotation 정보를 언제까지 유지할 것인가.

     \* **Source**: 소스 코드까지만 유지. 즉, **클래스 파일에는 존재하지 않음.**

     \***Class**: 컴파일 한. class 파일에도 유지. **런타임 시, 클래스를 메모리로 읽어오면 해당 정보는 사라짐.**

     \***Runtime**: 클래스를 **메모리에 읽어왔을 때까지 유지!** 코드에서 이 정보를 바탕으로 특정 로직을 실행

#### **  2. @Target**

- 해당 Annotation을 어디에 사용할 수 있는지 결정한다.

#### **  3. @Documented**

- 해당 Annotation을 사용한 코드의 문서에 그 Annotation에 대한 정보를 표기할지 결정한다.

---

### **Custom Annotation 만들기**

[##_Image|kage@FtGvW/btqADkce7P8/JbaIXfUisR0aMgTVzOt2WK/img.png|widthContent|data-lazy-src="" data-width="693" data-height="106" data-origin-width="0" data-origin-height="0"|||_##]

@GetMapping은 Meta Annotation이 아니므로 **RequestMapping을 사용**한다.

그리고 Retention은 RUNTIME 시까지 유지시켜줘야 하며 METHOD 단에 붙일 거기 때문에 타깃을 설정하였다.

​

[##_Image|kage@BFnz7/btqACOSq1B6/KTyEOJqFHAvbTjXOxHx280/img.png|widthContent|data-lazy-src="" data-width="693" data-height="319" data-origin-width="0" data-origin-height="0"|||_##][##_ImageGrid|kage@ccuOYT/btqADYsZV5Z/AGg8xiR7t2HVK6bcKO0ZW0/img.png,kage@vFc3E/btqAC0Zo4So/CKhK78oWglYtxfsHofyac1/img.png|data-lazy-src="" data-width="445" data-height="205" data-origin-width="0" data-origin-height="0" style="width: 53.233%; margin-right: 10px;",data-lazy-src="" data-width="318" data-height="171" data-origin-width="0" data-origin-height="0" style="width: 45.6043%;"|_##]

Annotation을 직접 만들어서 사용하여도 잘 동작하는 것을 확인할 수 있다.

---

[https://www.inflearn.com/course/%EC%9B%B9-mvc#](https://www.inflearn.com/course/%EC%9B%B9-mvc#)
