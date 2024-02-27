package org.orury.domain.post.db.repository;

//@DisplayName("Post Repository Test")
//@RepositoryTest
//class PostRepositoryTest extends TestRepositoryUtils {
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public PostRepositoryTest(PostRepository postRepository, UserRepository userRepository) {
//        this.postRepository = postRepository;
//        this.userRepository = userRepository;
//    }
//
//    @BeforeEach
//    void setUp() {
//        userRepository.save(createUser(1L));
//        postRepository.save(createPost(1L, createUser(1L)));
//    }
//
//    @DisplayName("게시글 아이디로 게시글 조회 - 성공")
//    @Test
//    void test() {
//        //given
//        User user = createUser(1L);
//        Post expect = createPost(1L, user);
//        Post actual = postRepository.getReferenceById(1L);
//
//        //then
//        assertThat(expect)
//                .isNotNull()
//                .isEqualTo(actual);
//    }
//}