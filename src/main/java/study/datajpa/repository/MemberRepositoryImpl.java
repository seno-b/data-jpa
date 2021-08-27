package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.internal.predicate.BooleanExpressionPredicate;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    private Predicate usernameEq(String username) {
        if (Objects.isNull(username)) {
            return null;
        }

        return null;
    }
}
