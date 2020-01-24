# 이벤트 추가하기

- 지난 게시글에서 이벤트 추가 Dialog를 생성하였다.
- 오늘은 Dialog를 통해 이벤트를 추가하면 해당 유저의 Event Table에 데이터가 삽입되고 캘린더에 추가된 이벤트가 보여지도록 해볼것이다.


## Spring Boot
### Event Entity
```JAVA
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private String content;

    // 연관관계 편의 메서드
    public void setMember(Member member){
        this.member = member;
        member.getEvents().add(this);
    }

}
```
- 우선 이벤트 엔티티를 추가해준다.
- member와 다대일 연관관계를 맺고 일정에 필요한 필드들을 가지고 있다.
- Date와 Time을 분리한 이유는 일정에서 날짜만 정해지고 시간이 정해지지 않는 경우가 있기 때문이다.

### Event Repository, Service
```JAVA
public interface EventRepository extends JpaRepository<Event, Long> {

}

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public EventResponseDto save(EventSaveRequestDto dto, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Event event = dto.toEntity(member);

        eventRepository.save(event);

        return new EventResponseDto(event);
    }

}
```
- EventRepository는 기본 Spring Data JPA가 지원해주는 메서드들을 사용할 것이다.
- EventService의 save에서 EventSaveRequestDto, email 매개변수를 통해 event를 생성 한 후 event를 저장한다.
- 그 후 EventResponseDto로 변환하여 return 해준다.

```JAVA
@NoArgsConstructor
@Getter
public class EventSaveRequestDto {


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String title;
    private String content;

    public Event toEntity(Member member){
        Event event = Event.builder()
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .content(content)
                .build();

        event.setMember(member);
        return event;
    }

}
```
- EventSaveRequestDto를 보면 @DateTimeFormat으로 직렬화된 JSON 데이터들을 LocalDate, LocalTime으로 변환 할 수 있다.

### EventApiController
```JAVA
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventApiController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity saveEvent(@RequestBody EventSaveRequestDto dto,
                                    @TokenMemberEmail String email){

        EventResponseDto saveDto = eventService.save(dto, email);

        return ResponseEntity.ok(saveDto);
    }

}
```
- EventApiController에서 클라이언트의 요청을 받아 이벤트를 저장해준다.
- @ToeknMemberEmail은 HandlerMethodArgumentResolver를 이용하여 클라이언트에서 보낸 Token을 디코딩 하여 회원의 email을 반환해준다.
- 클라이언트단에서 로그인 회원을 검증할 테지만 여기서 한번 더 검증을 통해 토큰이 없다면 이벤트는 추가되지 않을 것이다.

---

## Vue.js
- 지난 게시글에서 구현한 일정 추가 Dialog 이후를 구현할 것이다.

### Calendar Store
#### **State**
```js
const state = {
    event: initEvent(),
    events: [],
    dialog: false,
};

function initEvent(){
    return {
        startDate: '',
        startTime: '',
        endDate: '',
        endTime: '',
        content: '',
        title: '',
    }
}
```
- Calendar의 state이다.
- event는 일정 추가 Dialog의 각 속성의 값들이다.
- events는 추가된 이벤트들을 받아오고 그 값을 캘린터에 보여주기 위한 state이다.

![img](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FcGoJnE%2FbtqBfsN9Kuy%2FZLwIfzCi86ekPvAUsg1Uuk%2Fimg.png)

```js
methods: {
    submit() {
        if (this.event.title === '' || this.event.endDate === '') {
            store.commit('SET_SNACKBAR',
             setSnackBarInfo('제목과 종료일자를 작성해주세요.', 'error', 'top'));
        } else {
            this.$store.dispatch('REQUEST_ADD_EVENT', this.event);
        }
    },
  }
```
- 일정 추가 모달에서 추가 버튼을 누르게 되면 호출되는 submit() 메서드이다.
- 일정 제목과 종료일은 필수이므로 검증을 한 후 dispatch를 통해 actions를 호출한다.

#### **Actions**
```js
const actions = {
    async REQUEST_ADD_EVENT(context, calendar) {
        try {
            const response = await requestAddEvent(calendar);

            const addedEvent = makeEvent(response.data);
            context.commit('ADD_EVENT', addedEvent);
            store.commit('SET_SNACKBAR', setSnackBarInfo('일정이 추가 되었습니다.', 'info', 'top'))
        } catch (e) {
            console.log('일정 추가 에러' + e);
        }
    },
  }

const colors = ['blue', 'indigo', 'deep-purple', 'green', 'orange', 'red'];


const makeEvent = (event) => {
    return {
        name: event.title,
        start: event.startDate + getTime(event.startTime),
        end: event.endDate + getTime(event.endTime),
        color: colors[Math.floor(Math.random() * 6)]
    }
};
```
- 캘린더의 actions 부분이다.
- 우선 비동기통신으로 requestAddEvent를 통해 서버에게 저장을 요청한다.
- 저장된 이벤트를 서버는 응답을 해줄 것이다.
- 그 후 makeEvent를 통해 이벤트를 따로 생성해줘야 한다.
- name, start, end, color 프로퍼티를 가지는 event여야 vuetify 캘린더가 인식하여 캘린더에 보여주기 때문이다.
- name은 일정 제목이 되고 start는 startDate와 Time을 합친 것이다.
- color는 현재는 랜덤으로 배정하였다.
- 그렇게 만든 이벤트를 mutations에 넘겨준다.

#### **Mutations**
```js
const mutations = {
    ADD_EVENT(state, getEvent) {
        state.events.push(getEvent);
        state.dialog = false;
        state.event = initEvent();
    }
};
```
- actions에서 받은 이벤트를 events state에 추가해준다.
- 이렇게 추사된 이벤트를 캘린더에서 받아 화면에 보여지게 될 것이다.

### Calendar.vue
```html
<v-calendar
        :event-color="getEventColor"
        :events="events"
        :start="start"
        :type="type"
        @click:date="open"
        @click:event="showEvent"
        @click:more="moreEvent"
        @click:time="open"
        dark
        ref="calendar"

        v-model="start"
></v-calendar>
```
- 캘린더 컴포넌트 이다.
- 저번 게시글 이후에 추가된 속성들이 조금 있다.
- 순서대로 event-color는 말그대로 event의 color에 맞게 캘린더에 일정이 표시된다.
- events는 데이터 배열로 실제 일정들을 가지고 있다.
- @click.date는 캘린더의 날짜를 클릭했을 때 발생하는 것으로 Event추가 Dialog를 띄워준다.
- @click.event는 추후에 선택된 이벤트를 자세히 보여줄 때사용한다.
- @click.more은 같을날에 이벤트가 많을 때 more 표시가 잇는데 그것을 누르면 캘린더의 type을 day로 변경하여 자세시 볼수 있게 해준다.
- @click.time은 @click.date와 동일하게 시간을 클리하면 이벤트 추가 Dialog를 띄어준다.

#### **events computed**
```js
computed: {
    events() {
        return this.$store.state.calendar.events;
    }
},
```
- mutations에서 추가한 state의 events 받아와 위에서 설명한 events 넣어주었다.
