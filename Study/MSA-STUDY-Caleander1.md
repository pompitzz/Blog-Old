> [Vuetify Calendar](https://vuetifyjs.com/en/components/calendars#calendars)를 이용하여 캘린더를 구현해보고 캘린더에 Evnet를 등록하여 Event가 캘린더에 보일 수 있도록 구현해볼 것이다.



### v-calendar
```html
<div class="pa-5">
          <v-row>
              <v-col cols="12" md="6" class="mb-4">
                  <v-sheet height="500">
                      <v-calendar
                              ref="calendar"
                              dark
                      ></v-calendar>
                  </v-sheet>
              </v-col>
          </v-row>
  </div>
```
- v-calendar를 사용하면 간단하게 캘린더 틀을 구현할 수 있다.
- 필요한 부분을 하나씩 커스텀 해나가면 된다.

[##_Image|kage@dqx5Nq/btqBfrocOP6/IFDTwApMoh2yMQh6Lx2HQk/img.png|floatLeft|data-origin-width="0" data-origin-height="0" width="476"|||_##]

- v-calendar를 사용하면 이러한 형태로 캘린더가 보인다.
- 현재 테마를 dark 모드로 진행하고 있기 때문에 캘린더 테마도 dark로 하였다.
- v-calendar에는start와 type이라는props가 존재한다.
- start의 값이 2020-01-01라면 캘린더는 1월의 캘린더를 보여주게 된다.
- type은 여러 가지 존재하지만 대표적으로 month, week. day가 존재하는데 type에 따라 캘린더가 변형된다. 

> 예를 들어 start가 2020-01-13이라면 month type에서는 1월을 보여주고 week type에서는 01-13이 존재하는 week을 보여줄 것이며 day type에서는 01-13을 보여주게 된다.

### StartDate와 Type 설정하기
```js
<template>
<div class="pa-5">
    <v-row>
        <v-col cols="12" md="6" class="mb-4">
            <v-row>
                <v-col cols="6">
                    <v-menu
                            ref="dateOpen"
                            v-model="dateOpen"
                            :close-on-content-click="false"
                            :return-value.sync="start"
                            offset-y
                    >
                        <template v-slot:activator="{ on }">
                            <v-text-field
                                    v-model="start"
                                    dark
                                    label="Start Date"
                                    prepend-icon="mdi-calendar"
                                    dense
                                    readonly
                                    outlined
                                    hide-details
                                    v-on="on"
                            ></v-text-field>
                        </template>

                        <v-date-picker dark
                                       v-model="start"
                                       no-title
                        >
                            <v-spacer/>
                            <v-btn text dark color="primary" @click="dateOpen = false">Cancel</v-btn>
                            <v-btn text dark color="primary" @click="$refs.dateOpen.save(start)">OK</v-btn>
                        </v-date-picker>
                    </v-menu>
                </v-col>
                <v-col cols="6">
                    <v-select
                            v-model="type"
                            :items="typeOptions"
                            label="Type"
                            class="my-auto"
                            dark
                            hide-details
                            outlined
                            dense
                    ></v-select>
                </v-col>
            </v-row>
            <v-sheet height="500">
                <v-calendar
                        ref="calendar"
                        :start="start"
                        :type="type"
                        dark
                ></v-calendar>
            </v-sheet>
        </v-col>
    </v-row>
</div>
</template>

<script>
export default {
    data() {
        return {
            dateOpen: false,
            start: '',
            type: 'month',
            typeOptions: [
                {text: 'Day', value: 'day'},
                {text: 'Week', value: 'week'},
                {text: 'Month', value: 'month'},
            ],
        }
    },
}
</script>
```
- 캘린더 위에 날짜를 선택할 수 있는 v-date-picker와 type을 정할 수 있는 v-select를 구현한다.
- 해당 컴포넌트의 각 props나 slots 등은 [vuetify document](https://vuetifyjs.com/en/components/date-pickers#date-month-pickers)에 상세히 나와있다.


[##_ImageGrid|kage@bdFebP/btqBeTZxXan/9mcqD1iv4sKq6kZBH2xLRk/img.png,kage@cSfV7r/btqBey2G5n5/1UswY2j7yWDEpWQMgEm8Wk/img.png|data-origin-width="0" data-origin-height="0" style="width: 45.3654%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 53.4719%;"|_##]

- Start Date에 설정한 날짜와 Type에 지정된 type에 따라 캘린더가 변화하는 것을 확인할 수 있다.


```html
<div class="text-center mb-3 display-1">
	{{start | moment('YYYY MMMM')}}
</div>
```
- moment.js를 Vue에서 간편하게 사용하게 해주는 [Vue-Moment](https://www.npmjs.com/package/vue-moment)를 이용하여 start의 값을 유연하게 바꿀 수 있다. 
- start를 year, month만 보이도록 설정하였다.

[##_Image|kage@c3MwGL/btqBg8nHadZ/LLZYTDDbXhSHXV0lmLbkDK/img.png|floatLeft|data-origin-width="0" data-origin-height="0" width="462"|||_##]

- 화면을 확인해보면 어느정도 깔끔한 캘린더의 틀이 잡힌 것을 알 수 있다.
- @click:date를 통해 캘린더의 날짜들을 눌렀을 때의 이벤트를 실행할 수 있다.
- 그 이벤트는 {date: "2020-01-07", time: "", …} 이런 식으로 클릭한 날짜에 대한 정보를 파라미터로 넘겨준다.
- 이 파라미터를 이용하여 날짜를 눌렀을 때 이벤트를 추가할 Dialog를 만들 수 있다.

### Evnet 추가 Dialog
```js
<template>
    <v-dialog max-width="600px" persistent v-model="dialog">
        <v-card>
            <v-card-title>
                <h3>일정 추가</h3>
            </v-card-title>
            <v-card-text>
                <v-form class="px-3" ref="form">
                    <v-text-field label="일정" v-model="calendar.title" prepend-icon="mdi-folder-marker"
                                  :rules="inputRules"></v-text-field>
                    <v-textarea label="상세설명" v-model="calendar.content" prepend-icon="mdi-pencil"
                                :rules="inputRules"></v-textarea>
                    <v-row>
                        <v-col cols="6" class="pb-0">
                            <v-menu>
                                <template v-slot:activator="{on}">

                                    <v-text-field slot="activator" label="시작일"
                                                  readonly
                                                  prepend-icon="mdi-calendar-month"
                                                  v-on="on" :value="calendar.startDate"
                                                  class=""></v-text-field>
                                </template>
                                <v-date-picker v-model="calendar.startDate"></v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-col cols="6" class="pb-0">
                            <v-menu
                                    :close-on-content-click="false"
                                    v-model="startTimer"
                                    offset-y
                            >
                                <template v-slot:activator="{ on }">
                                    <v-text-field
                                            label="시작 시간"
                                            readonly
                                            :value="calendar.startTime"
                                            prepend-icon="mdi-timer"
                                            v-on="on"
                                    ></v-text-field>
                                </template>
                                <v-time-picker
                                        v-if="startTimer"
                                        v-model="calendar.startTime"
                                >
                                    <v-btn class="mx-auto"
                                           @click="selectTime"
                                    >선택
                                    </v-btn>
                                </v-time-picker>
                            </v-menu>
                        </v-col>
                    </v-row>

                    <v-row>
                        <v-col cols="6" class="pt-0">
                            <v-menu>
                                <template v-slot:activator="{on}">

                                    <v-text-field slot="activator" label="종료일"
                                                  readonly
                                                  prepend-icon="mdi-calendar-month"
                                                  v-on="on" :value="calendar.endDate"
                                                  class=""></v-text-field>
                                </template>
                                <v-date-picker v-model="calendar.endDate"
                                               :allowed-dates="allowedDates"></v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-col cols="6" class="pt-0">
                            <v-menu
                                    :close-on-content-click="false"
                                    v-model="endTimer"
                                    offset-y
                            >
                                <template v-slot:activator="{ on }">
                                    <v-text-field
                                            label="종료 시간"
                                            readonly
                                            :value="calendar.endTime"
                                            prepend-icon="mdi-timer"
                                            v-on="on"
                                    ></v-text-field>
                                </template>
                                <v-time-picker
                                        v-if="endTimer"
                                        v-model="calendar.endTime"
                                >
                                    <v-btn class="mx-auto"
                                           @click="selectTime"
                                    >선택
                                    </v-btn>
                                </v-time-picker>
                            </v-menu>
                        </v-col>
                    </v-row>

                    <div class="text-center">
                        <v-btn text class="primary white--text mx-2 mt-3" @click="submit">
                            추가
                        </v-btn>
                        <v-btn text class="primary white--text mx-2 mt-3" @click="close">
                            닫기
                        </v-btn>
                    </div>
                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        data() {
            return {
                startTimer: false,
                endTimer: false,
            }
        },
        computed: {
            dialog() {
                return this.$store.state.calendar.dialog;
            },
            calendar() {
                return this.$store.state.calendar.calendar;
            },
        },
        methods: {
            submit() {
                if (this.$refs.form.validate()) {
                    this.$store.dispatch('REQUEST_ADD_EVENT',this.calendar);
                }
            },
            close() {
                this.$store.commit('CLOSE_CALENDAR_DIALOG');
            },
            selectTime() {
                this.endTimer = false;
                this.startTimer = false;
            },
            allowedDates(val) {
                let endDate = val.split('-').reduce((a, b) => a + b);
                let startDate = this.calendar.startDate.split('-').reduce((a, b) => a + b);
                return endDate >= startDate;
            }
        },

    }
</script>
```
- 캘린더에서 날짜를 눌렀을 때 vuex를 이용하여 dialog를 띄울 수 있다.
- 그리고 날짜를 눌렀을 때의 날짜와 시간에 따라 시작일과 시작 시간이 변경되게 구현하였다.
- 캘린더가 month type일때 날짜를 눌러 Dialog를 띄우면 시작 시간은 없고 날짜만 존재한다.
- 그외에 week, day type에서는 해당 날짜의 시간을 통해 Dialog를 띄울 수 있다.
- 종료일 v-date-pricker를 보면 allowedDates라는 props가 사용된 것을 알 수 있는데 이것을 통해 종료일에 날짜 범위를 지정할 수 있다.
- allowdDates method를 보면 val을 넘겨주는 것을 알 수 있는데 이 값을 통해 종료일의 범위를 지정할 수 있다.
- 종료일은 시작일보다 빠를 수 없으므로 시작일 이후의 날짜만 선택 가능하게 하였다.


### Dialog 띄우기
```js
<v-calendar
        ref="calendar"
        :start="start"
        @click:date="open"
        :type="type"
        dark
></v-calendar>

methods: {
	open(date) {
	    this.$store.commit('OPEN_CALENDAR_DIALOG', date)
	}
}
```
- calendar에 @click:date를 추가해주고 클릭 시 dialog를 open 하도록 commit을 한다.

```js
const mutations = {
    OPEN_CALENDAR_DIALOG(state, payload) {
        state.calendar.startDate = payload.date;
        state.calendar.startTime = payload.time;
        state.calendar.hasTime = payload.hasTime;
        state.dialog = true;
    },
    CLOSE_CALENDAR_DIALOG(state) {
        state.dialog = false;
    },
    ADD_EVENTS(state, evnet) {
        state.events[0] = evnet;
        state.events[1] = evnet;
        state.events[2] = evnet;
        state.dialog = false;
    }
};
```

- mutations를 보면 dialog 뿐만 아니라 startDate, startTime, hasTime도 함께 state에 넘겨준 것을 알 수 있다.

[##_ImageGrid|kage@cGoJnE/btqBfsN9Kuy/ZLwIfzCi86ekPvAUsg1Uuk/img.png,kage@YaGy6/btqBc6esh4G/pK7eUNWRMHMv29IaoI3Znk/img.png|data-origin-width="0" data-origin-height="0" width="424" height="354" style="width: 59.9374%; margin-right: 10px;",data-origin-width="0" data-origin-height="0" style="width: 38.8998%;"|_##]

- 캘린더의 날짜를 누르면 OPEN\_CALENDAR\_DIALOG의 mutation이 실행되고 정상적으로 dialog가 나타난다.
- 시작일은 자동으로 값이 바인딩된 것을 알 수 있고 종료일은 오른쪽 date-picker와 시작일인 24일부터 선택이 가능한 것을 알 수 있다.
- 이렇게 캘린더와 Event 추가 Dialog구현이 완료되었다.
- 다음에는 이 Dialog를 통해 실제 Event를 추가해 Calendar와 연동하는 것을 해볼 것이다.
