package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("홍길동");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member=2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("AAA", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<Member> aaa1 = memberRepository.findByUsername("AAA");
        Assertions.assertThat(aaa1.get(0).getUsername()).isEqualTo("AAA");

    }


    @Test
    public void testQuery() {
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<Member> aaa1 = memberRepository.findUser("AAA", 20);
        Assertions.assertThat(aaa1.get(0).getUsername()).isEqualTo("AAA");

    }

    @Test
    public void findUsernameList() {
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<String> aaa1 = memberRepository.findUsernameList();
        Assertions.assertThat(aaa1.get(0)).isEqualTo("AAA");
        Assertions.assertThat(aaa1.get(1)).isEqualTo("BBB");
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member aaa = new Member("AAA", 10);
        aaa.setTeam(team);
        memberRepository.save(aaa);

        List<MemberDto> result = memberRepository.findMemberDto();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void findByNames() {
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);

        List<Member> aaa1 = memberRepository.findByNames(Arrays.asList("AAA", "BBB", "CCC"));

        for (Member member : aaa1) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void findOptinalByUsername() {
        Member aaa = new Member("AAA", 10);
        Member bbb = new Member("BBB", 20);
        memberRepository.save(aaa);
        memberRepository.save(bbb);

        Optional<Member> result = memberRepository.findOptinalByUsername("AAA");
        System.out.println("result = " + result);

    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> result = memberRepository.findByAge(age, pageRequest);

        List<Member> content = result.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(8);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(3);
        Assertions.assertThat(result.isFirst()).isTrue();
        Assertions.assertThat(result.hasNext()).isTrue();


    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 40));

        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear();

        //then
        Assertions.assertThat(resultCount).isEqualTo(3);

        List<Member> result = memberRepository.findByUsername("member6");
        Member member = result.get(0);

        System.out.println("member = " + member);
    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }

    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void queryLock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> member11 = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic() {
        //given

        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member member1 = new Member("member1", 10, teamA);
        memberRepository.save(member1);

        em.flush();
        em.clear();

        //when
        Specification<Member> spec =
                MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        //then
        Assertions.assertThat(result.size()).isEqualTo(1);

    }
}