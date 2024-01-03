package org.fastcampus.oruryclient.domain.user.db.repository;

//@SpringBootTest
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @DisplayName("생성된 유저를 저장할 때 id, createdAt, updatedAt을 자동으로 생성해주는지 테스트")
//    @Test
//    @Transactional
//    void saveAutoTest() {
//        //given
//        User user = User.of(null,"abc@gmail.com","testnick", "1234", 1,2, LocalDate.of(2023, 12, 21),"orury.png",null, null);
//
//        //when
//        User saveUser = userRepository.save(user);
//
//        //then
//        assertThat(saveUser.getId()).isInstanceOf(Long.class);
//        assertThat(saveUser.getCreatedAt()).isInstanceOf(LocalDateTime.class);
//        assertThat(saveUser.getUpdatedAt()).isInstanceOf(LocalDateTime.class);
//    }
//}