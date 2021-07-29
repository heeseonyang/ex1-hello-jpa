package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();/*트렌젝션 생성*/
        tx.begin(); /*트렌젝션 시작*/

        try {
            //code
           //Member findMember = em.find(Member.class, 1L);
           List<Member> result = em.createQuery("select m from Member as m", Member.class)
                   .getResultList();

           for (Member member : result) {
               System.out.println("member.name = " + member.getName());
           }

            tx.commit(); /*커밋*/
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
