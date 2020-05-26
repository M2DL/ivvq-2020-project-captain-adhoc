package captainadhoc.captainadhocbackend.integrationServices;

import captainadhoc.captainadhocbackend.domain.Member;
import captainadhoc.captainadhocbackend.services.interfaces.IMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired
    IMemberService memberService;

    @BeforeEach
    public void setupEach() {

        Member member = Member.builder()
                .lastName("Kevin")
                .firstName("Marchand")
                .userName("marchand1")
                .password("mdp")
                .isAdmin(true)
                .build();

        memberService.saveMember(member);
    }

    @Test
    public void saveMemberTest() {

        // given: un Member non persisté
        Member member = Member.builder()
                .lastName("Test")
                .firstName("Utilisateur")
                .userName("UtilisateurTest")
                .password("mdp")
                .isAdmin(true)
                .build();

        assertNull(member.getIdMember());

        // when: le member est persisté
        memberService.saveMember(member);

        // then: le member a un id
        assertNotNull(member.getIdMember());

    }
}
