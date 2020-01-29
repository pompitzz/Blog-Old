> [지난 게시글](https://sun-22.tistory.com/46)에서 설정한 Authorization Server에 이어 Resource Server를 설정하는 게시글 입니다.

---

- 저번 게시글에서 Authorization Server 설정을 통해 JWT 토큰을 발급하였다.
- 이번엔 Resource Server를 설정할 것이다.
- GET 요청에 대해서는 모든 사용자를 허가할 것이다.
- 나머지 요청에 대해서는 **인증된 사용자만** 허가를 할 것이다.


> 게시판을 생성하고 조회하는 예제를 통해 Resource Server가 정상적으로 동작하는지 확인한다.

### Board 엔티티 생성
```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Board(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
```
- 제목, 내용, 작성자를 가지고 있는 Board를 생성한다.


#### DTO
```java
@NoArgsConstructor
@Getter
public class BoardResponseDto {

    Long id;
    String title;
    String content;
    String author;

    public BoardResponseDto(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}

@NoArgsConstructor
@Getter
public class BoardSaveRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String author;

    @Builder
    public BoardSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Board toEntity(){
        return Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

}
```
- 게시판을 생성하고 조회할 때 필요한 BoardSaveRequestDto, BoardResponseDto를 생성한다.

### BoardRepository, BoardService

```java
public interface BoardRepository  extends JpaRepository<Board, Long> {
}

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardSaveRequestDto dto){
       return boardRepository.save(dto.toEntity()).getId();
    }

    public BoardResponseDto findById(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> findAll(){
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }
}
```
- BoardRepository를 생성하고 BoardService에 기본적인 게시판 저장, 조회 로직을 작성한다.

### BoardController

#### HATEOAS 의존성 추가
[##_Image|kage@DqEiX/btqAJhAs9j5/M7JaejtlQ4MQf2NGkNdMPK/img.png|alignCenter|data-origin-width="0.0" data-origin-height="0.0"|||_##]
- 우선 게시판 생성 시 201 응답에 담아줄 uri을 생성하기 위해 spring hateoas 의존성을 추가한다.


#### BoardApiController
```java
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid BoardSaveRequestDto dto,
                               Errors errors){

        if (errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        Long savedId = boardService.save(dto);
        URI uri = WebMvcLinkBuilder.linkTo(BoardApiController.class).slash(savedId).toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        BoardResponseDto responseDto = boardService.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity findAll(){
        List<BoardResponseDto> boardList = boardService.findAll();
        return ResponseEntity.ok(boardList);
    }
}
```
- 게시판 생성은 PostMapping으로 하고 게시판 조회는 GetMapping으로 설정한다.
- 게시판 생성이 정상적으로 이루어지면 created 응답에 게시판을 조회할 수 있는 uri를 담아 응답해주고 나머지는 body에 responseDto를 담아 ok응답으로 보낸다.


> ResourceServer가 설정되면 POST요청인 게시판 생성은 AccessToken이 필요하고 GET요청인 게시판 조회는 따로 인증을 필요로 하지 않을 것이다.

### ResourceServerConfig

```java
@RequiredArgsConstructor
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("SpringBoot");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/members/join").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
```
- Resource Server 설정은 Authorization Server에 비해 간단하다.
- 왜냐하면 현재는 한 프로젝트 내에서 Authorization  Server와 Resource Server를 같이 설정하였다.
- 그러므로 Authorization Server에 빈으로 등록된 JwtTokenStore와 JwtTokenConverter를 사용할 수 있다.
- 즉 따로 Jwt에 대한 설정을 ResouceServerSecurityConfigurer에 하지 않아도된다.
- ResouceServerSecurityConfigurer에 resourceId만 설정하고 다른 건 기본 설정으로 놔둔다.
- http configure에서 회원가입을 위한 /api/memebers/join POST 요청과 /api이하의 모든 GET 요청은 permitAll 한다.
- 나머지 요청은 승인된 사용자만 가능하게 설정하였으며 exceptionHandling은 spring security oauth2에서 제공해주는 핸들러를 사용하였다.

### 테스트 코드 작성
#### 게시판 생성 테스트
```java
@Test // (1)
void saveBoardWithoutAccessToken() throws Exception {
    //given
    BoardSaveRequestDto dto = BoardSaveRequestDto.builder()
            .title("타이틀")
            .content("내용")
            .author("작성자")
            .build();
    // when && then
    this.mockMvc.perform(post("/api/boards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
            .andDo(print())
            .andExpect(status().isUnauthorized());
}
```
- 첫 번째 테스트는 인증되지 않은 사용자가 게시판을 생성하는 경우이다.
- 헤더에 토큰 정보를 넣지 않고 POST 요청을 보내기 때문에 401(Unauthorized) 응답이 발생한다.

<br>

```java
@Test // (2)
void saveBoardWithAccessToken() throws Exception {
    //given
    BoardSaveRequestDto dto = BoardSaveRequestDto.builder()
            .title("타이틀")
            .content("내용")
            .author("작성자")
            .build();
    // when && then
    this.mockMvc.perform(post("/api/boards")
            .header(HttpHeaders.AUTHORIZATION, getJwtToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(redirectedUrlPattern("http://localhost/api/boards/{id}"));
}

String getJwtToken() throws Exception {
    //given
    String password = "qwe123";
    String email = "email@gmail.com";
    MemberJoinRequestDto joinDto = MemberJoinRequestDto.builder()
            .email(email)
            .password(password)
            .name("John")
            .role(MemberRole.USER)
            .build();
    memberService.save(joinDto);

    //when & then
    ResultActions result = this.mockMvc.perform(post("/oauth/token")
            .with(httpBasic(clientId, clientSecret))
            .param("username", email)
            .param("password", password)
            .param("grant_type", "password"));

    String responseBody = result.andReturn().getResponse().getContentAsString();
    Jackson2JsonParser parser = new Jackson2JsonParser();
    String access_token = parser.parseMap(responseBody).get("access_token").toString();
    return "Bearer " + access_token;
}
```
- 두 번째 테스트는 JWT 토큰을 발급받고 Header에 Authorization: Bearer \[JWT TOKEN\]을 넣어 보내는 경우이다.
- 정상적으로 게시판이 생성되고 201(Created) 응답에 생성된 게시판을 조회할 수 있는 RedirectUrl이 존재하는 것을 확인할 수 있다.
- getJwtToken()을 통해 멤버를 등록 한후 등록된 멤버의 아이디로 토큰을 요청하였다.
- 그 요청의 결과에서 accees_token 값을 추출하여 반환해준다.

<br>

```java
@Test // (3)
void saveBoardWithAccessTokenButInvalidToken() throws Exception {
    //given
    BoardSaveRequestDto dto = BoardSaveRequestDto.builder()
            .title("타이틀")
            .content("내용")
            .author("작성자")
            .build();
    // when && then
    this.mockMvc.perform(post("/api/boards")
            .header(HttpHeaders.AUTHORIZATION, getJwtToken() + "ASD")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
            .andDo(print())
            .andExpect(status().isUnauthorized());
}


```
- 마지막 테스트는 JWT 토큰을 Header에 넣어 보내 긴 하지만 잘못된 토큰을(여기선 임의의 문자를 추가했음) 넣어 보내는 경우이다.
- 이 또한 첫번 째 테스트와 똑같이 401(Unauthorized) 응답이 발생한다.

#### 게시판 조회 테스트
```java
@Test
void findBoardAll() throws Exception {
    //given
    IntStream.range(0, 10).forEach(this::saveBoard);

    //when && then
    this.mockMvc.perform(get("/api/boards"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].id").exists())
            .andExpect(jsonPath("[0].title").exists())
            .andExpect(jsonPath("[0].content").exists())
            .andExpect(jsonPath("[0].author").exists());
}


void saveBoard(int i) {
    BoardSaveRequestDto dto = BoardSaveRequestDto.builder()
            .title("Title" + i)
            .content("Content" + i)
            .author("Author" + i)
            .build();

    boardService.save(dto);
}
```
- 모든 게시판을 조회하는 테스트이다.
- 우선 임의적으로 10개의 게시판을 Board 테이블에 저장하고 별도의 토큰 없이 바로 게시판을 조회한다.
- 게시판을 조회하면 200(Ok) 응답과 게시판의 Data들이 Json형태로 오는 것을 테스트하였다.

### 테스트 결과
[##_Image|kage@bNyB2R/btqAKv5DhX7/pdrCFBxT15eKuzXWfd9yYK/img.png|widthContent|data-origin-width="0.0" data-origin-height="0.0"|||_##]
- 위의 모든 테스트가 통과된 것을 알 수 있다.
- 이로서 Resource Server의 설정이 제대로 적용된 것을 확인할 수 있다.
