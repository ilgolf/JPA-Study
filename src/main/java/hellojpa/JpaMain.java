package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

// JPA의 모든 데이터 변경은 transaction 안에서 실행
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory hello = Persistence.createEntityManagerFactory("hello");

        EntityManager entityManager = hello.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            // DB Create
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloDB");

            member.setId(1L);
            member.setName("HelloA");

            entityManager.persist(member);

            // DB Select & Update
            Member findMember = entityManager.find(Member.class, 1L);
            findMember.setName("HelloJPA");

            // DB Delete
            entityManager.remove(findMember);

            //JPQL 전체 조회
            List<Member> result = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(10) // 페이징 5 ~ 10 페이지
                    .getResultList();

            for (Member member1 : result) {
                System.out.println("member1.getName() = " + member1.getName());
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        hello.close();
    }
}
