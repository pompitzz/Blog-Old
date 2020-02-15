# 추가된 이벤트 조회 및 상세보기
> [지난 게시글](https://sun-22.tistory.com/74?category=364993)에서 구현한 이벤트 추가하기에 이어 이벤트 조회 및 상세보기를 구현해 볼 것이다.

- Event 조회는 Event의 시작일 혹은 종료일이 해당 캘린더의 Month와 같은 것들만 조회할 것이다.
- 캘린더에 보여지는 이벤트를 클릭했을 때 이벤트의 세부정보를 볼 수 있는 Dialog를 띄울 것이다.

# 이벤트 조회

## Spring Boot
- Event의 시작일의 년도, 개월과 종료일의 년도, 개월을 각각 비교해야 하고 이벤트를 조회하는 회원도 고려 해야한다.
- 쿼리가 조금씩 난잡해질 수록 QueryDSL를 사용하는게 매우 효율적이다.
- 그러므로 Event를 조회하는 쿼리는 QueryDSL로 작성해 볼 것이다.

#### QueryDSL 의존성 추가

```shell
plugins {
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

dependencies {
    implementation 'com.querydsl:querydsl-jpa'
}

def querydslDir = "$buildDir/generated/querydsl"
querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}
sourceSets {
    main.java.srcDir querydslDir
}
configurations {
    querydsl.extendsFrom compileClasspath
}
compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}
```
- QueryDSL의존성을 추가하고 queryDSL의 Q타입 객체들은 빌드 폴더에 넣었다.

```bash
./gradlew compileQuerydsl
```
- 의존성 추가후 compileQuerydsl을 통해 Q타입 객체들을 생성한다.

```java
@Configuration
public class QueryDslConfig {
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
```
- 마지막으로 JpaQueryFactory를 빈으로 등록하면 QueryDSL을 사용할 준비가 끝났다.

### QueryDSL로 이벤트 조회하기
- 우선 Spring Data JPA의 Repository와 QueryDSL을 함께 쓰기 위해서는 몇가지 절차가 필요하다.

```java
public interface EventRepositoryCustom { // (1)
}

// (2)
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
}

@RequiredArgsConstructor // (3)
public class EventRepositoryImpl implements EventRepositoryCustom {
}
```
- 처음으로 1번과 같이 interface 타입으로 EventRepositoryCustom을 생성한다.
- 그 후 Spring Data JPA를 사용하는 EventRepository에서 1번을 상속받는다.
- 마지막으로 EventRepositoryImpl를 생성하여 EventRepositoryCusom(1번)을 구현한다.
- Custom을 구현하는 Impl에서 QueryDSl를 작성하면 된다.
- Repository뒤 접미사인 Impl, Custom의 이름을 변경하면 Spring Data JPA가 인식하지 못하는 것으로 알고있으므로 그대로 사용하는게 좋을 거 같다.

<br>

```java
public interface EventRepositoryCustom {
    List<Event> findByMonthAndMemberId(LocalDate date, Long memberId);
}

@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Event> findByMonthAndMemberId(LocalDate date, Long memberId) {
        return queryFactory
                .selectFrom(event)
                .join(event.member, member)
                .where(
                        member.id.eq(memberId),
                        startDateEq(date).or(endDateEq(date))
                )
                .fetch();
    }

    private BooleanExpression startDateEq(LocalDate date) {
        return event.startDate.year().eq(date.getYear())
                .and(event.startDate.month().eq(date.getMonthValue()));
    }

    private BooleanExpression endDateEq(LocalDate date) {
        return event.endDate.year().eq(date.getYear())
                .and(event.endDate.month().eq(date.getMonthValue()));
    }

}
```
- EventRepositoryCusom에 기능을 정의하고 EventRepositoryImpl에서 기능을 구현한다.
- 이전에 등록한 jpaQueryFactory를 사용하여 쿼리를 작성한다.
- 작성된 쿼리를 보면 queryDSL을 사용하면 자바 코드로 매우 직관적으로 짤 수 있는 것을 알 수 있다.
- event를 membet와 join하여 조회한다. 조회할때 where 조건문을 보면 쉼표로 구분되어있다.
- 쉼표는 and와 똑같은 의미로 memberId가 같으면서 startDate, endDate 조건이 맞는 이벤트를 조회하는 것이다.
- startDateEq, endDateEq를 통해 각 시작일, 종료일의 년도와 개월이 파라미터의 년도와 개월과 일치하는지 검증한다.

### TEST
```java
@Test
void findEventsByDate() throws Exception {
    //given
    Member member = Member.builder()
            .email("asd@asd.com")
            .password("password")
            .name("name")
            .role(MemberRole.USER)
            .build();
    em.persist(member);

    // 년도는 모두 20년이다.
    // content이후 첫번째 인자가 시작 Month, 두번째 인자가 종료 Month이다.
    Event event1 = getEvent(member, "title1", "content", 1, 1);
    Event event2 = getEvent(member, "title2", "content", 12, 1);
    Event event3 = getEvent(member, "title3", "content", 1, 2);
    Event event4 = getEvent(member, "title4", "content", 2, 2);
    Event event5 = getEvent(member, "title5", "content", 12, 12);
    em.persist(event1);
    em.persist(event2);
    em.persist(event3);
    em.persist(event4);
    em.persist(event5);

    em.flush();
    em.clear();

    //when
    List<Event> findEvent = eventRepository.findByMonthAndMemberId(of(2020, 1, 20), member.getId());

    //then
    assertThat(findEvent).extracting("title").containsExactly("title1", "title2", "title3");
}
```
- 테스트로 기능이 제대로 동작하는지 확인해본다.
- 아규먼트가 20년 1월이므로 event1, 2, 3이 조회되어야 한다.
- 아래와 같이 테스트가 통과되는것을 알 수 있다.

### Service, Controller
```java
// Service
public List<EventResponseDto> findByMonthAndMember(LocalDate date, String email) {

    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

    List<Event> findEvents = eventRepository.findByMonthAndMemberId(date, member.getId());

    return findEvents.stream().map(EventResponseDto::new).collect(Collectors.toList());
}

// Controller
@GetMapping
public ResponseEntity queryEvents(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam  LocalDate date,
                                  @TokenMemberEmail String email){

    List<EventResponseDto> findDtos = eventService.findByMonthAndMember(date, email);

    return ResponseEntity.ok(findDtos);
}
```
Service
- 서비스에서는 파라미터로 받은 email을 통해 회원을 조회하고 그 회원의 아이디와 날짜에 맞게 이벤트들을 조회한 후 Dto로 변환하여 반환해준다.

Controller
- 컨트롤러에서는 Client가 Param으로 보내는 Date String을 @DateTimeFormat을 통해 LocalDate로 변환해준다.
- @TokenMemberEmail은 이벤트 조회시 해당 유저의 JWT 토큰을 통해 email 정보를 받아온다.
- 두개의 파라미터를 통해 service에게 조회를 요청하고 조회된 값을 반환해준다.

### Vue.js
- vue.js는 지난 게시글들에서 디자인은 다 구현하였으므로 로직만 작성하면 된다.

```js
// actions
async REQEUST_QUERY_EVENTS_BY_DATE(context, date) {
    try {
        const response = await requestQueryEvents(date);
        context.commit('ADD_EVENTS', response.data);
    } catch (e) {
        store.commit('SET_SNACKBAR', setSnackBarInfo('이벤트 전체 조회를 실패하였습니다.', 'error', 'top'))
    }
},

// api
function requestQueryEvents(date) {
    return axios.get(`${process.env.VUE_APP_BASEURL}/api/events`, {
        params: {date: date}
    });
}

// mutations
ADD_EVENTS(state, events) {
   state.events = [];
   events.forEach(e => {
       state.events.push(makeEvent(e));
   })
},

// Calendar.vue
computed: {
     events() {
         return this.$store.state.calendar.events;
     }
 },
```
- Vuex를 통해 이벤트를 조회하는 방법으로 구현하였다.
- actions는 파라미터의 date를 api통신을 담당하는 requestQueryEvents에게 전달인자로 넘겨준다.
- requestQueryEvents는 get요청을 통해 date를 파라미터로 넘겨주고 이벤트를 조회한 후 그 결과를 리턴한다.
- 리턴값을 받은 actions는 mutations인 ADD_EVENTS에게 그 결과를 전달한다.
- ADD_EVENTS는 전달받은 이벤트를 state.events에 채워넣는다.
- 그 값은 Calendar.vue에서 computed로 받아 캘린더에 담아준다.

<br>

```js
watch:{
    start(newDate, oldDate) {
        let newDateMonth = this.$moment(newDate).format('MM');
        let oldDateMonth = this.$moment(oldDate).format('MM');
        if(newDateMonth !== oldDateMonth){
            this.$store.dispatch('REQEUST_QUERY_EVENTS_BY_DATE', newDate);
        }
    }
}
```
- Calendar.vue에서는 캘린더의 날짜인 start를 watch를 통해 관찰한다.
- 값이 바뀔 때 이전 값과 바뀐 값의 개월수가 다르면 actions에게 해당 날짜에 맞는 이벤트 조회를 요청한다.

### 결과 확인

```java
@RequiredArgsConstructor
@Component
@Profile("local")
@Transactional
public class AppRunner implements ApplicationRunner {


    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Long mem = memberService.save(MemberJoinRequestDto.builder()
                .email("123@123.com")
                .password("123")
                .name("John")
                .role(MemberRole.ADMIN)
                .build());

        Member member = memberRepository.findById(mem).get();

        saveEvents("블로그 작성", of(2020, 1, 28), of(2020, 1, 30), member);
        saveEvents("회의", of(2019, 12, 20), of(2019, 12, 25), member);
        saveEvents("2월달 면접준비", of(2020, 2, 2), of(2020, 2, 11), member);
        saveEvents("면접 공부", of(2020, 1, 20), of(2020, 1, 30), member);
        saveEvents("책 읽기", of(2020, 12, 30), of(2020, 1, 3), member);

    }

    private void saveEvents(String title, LocalDate start, LocalDate end, Member member){
        Event event = Event.builder()
                .startDate(start)
                .endDate(end)
                .title(title)
                .content("content")
                .build();
        event.setMember(member);
        eventRepository.save(event);
    }
}
```
- 우선 서버단에서 초기 데이터를 미리 넣어준다.

<br>

- Vue 프로젝트를 구동시켜 캘린더를 확인해보면 제대로 값들이 들어오는 것을 볼 수 있다.
- 일정이 1 ~ 2월일 경우에도 1월 2월 모두 조회하여 정상적으로 보여지는 것을 알 수 있다.

# 이벤트 상세보기
- 이벤트를 클릭하면 이벤트에 대한 세부사항을 보여줄 모달을 구현해본다.
- 서버단은 매우 단순한 조회이므로 따로 생략한다.

### EventDetail.vue
```js
<template>
    <v-row justify="center">
        <v-dialog v-model="dialog" persistent max-width="400">
            <v-card>
                <v-card-title class="headline">{{event.title}}</v-card-title>
                <v-card-text style="font-size: 1rem" class="font-weight-bold">{{event.content}}</v-card-text>

                <div class="ml-5 font-weight-light">
                    <div>시작일: {{getEventStart()}}</div>
                    <div>종료일: {{getEventEnd()}}</div>
                </div>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green darken-1" text @click="close()">닫기</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-row>
</template>

<script>
    export default {
        name: "EventDetail",
        computed: {
            dialog() {
                return this.$store.state.calendar.eventDetailDialog;
            },
            event() {
                return this.$store.state.calendar.event;
            }
        },
        methods: {
            getEventStart() {
                return this.event.startDate + getTime(this.event.startTime);
            },
            getEventEnd() {
                return this.event.endDate + getTime(this.event.endTime);
            },
            close(){
                return this.$store.commit('CLOSE_EVENT_DETAIL');
            }
        }
    }

    const getTime = (time) => {
        return time === null ? '' : ` ${time}`;
    };
</script>
```

- 이벤트 세부사항을 보여주는 모달이다.
- dialog, event는 vuex state를 통해 받아온다.
- 시작일과 종료일은 날짜와 시간을 합쳐서 보여줄 것이다.


<br>

### 구현 로직
```js
// Calendar.vue
showEvent({event}) {
    this.$store.dispatch('REQUEST_DETAIL_EVENT', event.id);
},

// actions
async REQUEST_DETAIL_EVENT(context, eventId) {
    try {
        const respone = await requestEventDetail(eventId);
        context.commit('SHOW_EVENT_DETAIL', respone.data);
    } catch (e) {
        store.commit('SET_SNACKBAR', setSnackBarInfo('이벤트 상세 조회를 실패하였습니다.', 'error', 'top'))
    }
},

// api
function requestEventDetail(eventId) {
    return axios.get(`${process.env.VUE_APP_BASEURL}/api/events/${eventId}`);
}

// mutations
SHOW_EVENT_DETAIL(state, event) {
    state.event = event;
    state.eventDetailDialog = true;
},
```
- showEvent는 캘린더에서 이벤트를 클릭했을 때 동작하는 메서드이다.
- 해당 이벤트의 아이디를 아규먼트로 넘겨 actions를 요청한다.
- api 요청 결과를 mutations을 통해 event에 넣고 eventDetailDialog를 오픈하게 된다.
