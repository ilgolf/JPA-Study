package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

// JPA의 모든 데이터 변경은 transaction 안에서 실행
public class persistMain {

    public static void main(String[] args) {
        EntityManagerFactory hello = Persistence.createEntityManagerFactory("hello");

        EntityManager entityManager = hello.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloDB"); // 비 영속 상태

            // 영속 상태가 된다.
            System.out.println("==== BEFORE ====");
            entityManager.persist(member);
            System.out.println("==== AFTER ====");

            Member findMember1 = entityManager.find(Member.class, 101L);

            // 첫 번째 조회시 캐시에 저장되므로 두 번째 조회 시 select문이 발생하지 않음음
            Member findMember2 = entityManager.find(Member.class, 101L);

            System.out.println("result = " + (findMember1 == findMember2)); // 2는 캐시에서 가져오므로 동일성이 보장된다.

            System.out.println("findMember.getId() = " + findMember1.getId());
            System.out.println("findMember.getName() = " + findMember1.getName());

            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            entityManager.persist(member1);
            entityManager.persist(member2);

            // Buffer가 걸려 모아서 쿼리가 나가므로 이 출력문이 실행 된 후에 쿼리문이 나간다.
            System.out.println("===================");

            member = entityManager.find(Member.class, 150L);
            member.setName("zzzz"); // 영속 시킬 필요가 없다.

            member = new Member(200L, "member200");
            entityManager.persist(member);

            entityManager.flush(); // 강제 쿼리문을 호출하여 DB에 커밋하기 전에 반영된다.
            System.out.println("==================");

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close(); // 꼭 닫아 줘야 한다.
        }

        hello.close(); // 필수적으로 닫아 줘야함
    }
}
