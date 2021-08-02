package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public JpaMain() {
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();/*트렌젝션 생성 */
        //엔티티 매니저는 데이터 변경시 트랜젝션을 시작해야 한다.
        tx.begin(); /*트렌젝션 시작*/

        try {


            //영속

            Member member = em.find(Member.class, 150L);
            member.setName("AAAAA");

            em.clear(); //통으로 다 날려버린다.

            System.out.println("=================");

            tx.commit(); /*완전 초기화 되기 때문에 커밋을 해도 아무일도 일어나지 않아*/
        }catch (Exception e) {

            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }


}

