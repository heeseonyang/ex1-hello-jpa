- Hello JPA - 프로젝트 생성

- Hello JPA - 애플리케이션 개발



## H2데이터 베이스 설치와 실행

- http://www.h2database.com/
- 가볍다. (1.5M)
- 웹용 쿼리툴 제공
- MySQL, Oracle 데이터베이스 시뮬레이션 기능
- 시퀀스, Auto Increment 기능 지원



![image-20210728194153815](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210728194153815.png)

![image-20210728194217895](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210728194217895.png)



## 메이븐 소개

- http://maven.apache.org/
- 자바 라이브러리, 빌드 관리
- 라이브러리 자동 다운로드 및 의존성 관리
- 최근에는 그래들(Gradle)이 점점 유명



## 프로젝트 생성

- 자바 8 이상 권장

- 메이븐 설정

    - groupID : jpa-basic
    - artifactId: ex1-hello-jpa
    - version: 1.0.0

- pom.xml에 라이브러리를 추가해준다.

  ```
  <dependencies>
      <!-- JPA 하이버네이트 -->
      <dependency>
          <groupId>org.hibernate</groupId>
          <artifactId>hibernate-entitymanager</artifactId>
          <version>5.3.10.Final</version>
      </dependency>
      <!-- H2 데이터베이스 -->
      <dependency>
          <groupId>com.h2database</groupId>
          <artifactId>h2</artifactId>
          <version>1.4.199</version>
      </dependency>
  </dependencies>
  ```



## JPA 설정하기

resources -> META-INF디렉토리 생성 -> persistence.xml파일 생성

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello"> <!--jpa를 만들건데 이름은 hello로 할거야-->
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/> <!--무슨 드라이버 쓰는지-->
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>
</persistence>
```

위 코드 넣기.





### 데이터베이스 방언

- JPA는 특정 데이터베이스에 종속되지 않는다
- 각각의 데이터베이스가 제공하는 SQL 문법과 함수는 조금씩 다르다
    - 가변문자: MySQL은 VARCHAR ,Oracle은 VARCHAR2
    - 문자열을 자르는 함수 : SQL표준은 SUBSTRING(), Oracle은 SUBSTR()
    - 페이징 : MySQL은 LIMIT, Oracle은 ROWNUM
- 방언 : SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능

*********************************************************************************



## JPA애플리케이션 개발

#### 

![image-20210729214255405](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729214255405.png)

Persistence에서 시작.. 설정정보를 읽어서 팩토리를 생성한다

팩토리에서 필요할때마다 엔티티매니저를 찍어내서 구동시킨다.



1) JPAMain클래스 생성

   java >hellojpa패키지 생성 > JpaMain클래스생성

   ![image-20210729214700903](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729214700903.png)





![image-20210729215101729](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729215101729.png)

1. Persistence에서부터 시작!

   Persistence.createEntityManagerFactory()해주는데 ()안에 persistence-unit name 을 넘기라고 한다.







![image-20210729214857824](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729214857824.png)

persistence-unit name은

>  resource-> META-INF > persistence.xml에 "persistence-unit name =   ? " 적는 곳이 있다.

우리는 여기에 "hello"라고 적었으니 그대로 넣어주면 된다.

![image-20210729220810169](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729220810169.png)

EntityManagerFactory를 만드는 순간 데이터베이스와 연결이 된 것이다.



![image-20210729221502042](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729221502042.png)

EntityManagerFactory에서 createEntityManager를 꺼낸다.

꺼내고 여기서 우리가 코드를 작성하고

실제 애플리케이션이 완전히 끝나면 끝나면 EntityManagerFactory를 닫아준다.



## 객체와 테이블을 생성하고 매핑하기

- @Entity : JPA가 관리할 객체
- @Id : 데이터베이스 PK와 매핑



1) 우선 Member테이블을 생성한다.

   테이블을 만들고 매핑을 해봐야 JPA동작을 확인할 수 있기 때문이다.

   h2에 접속하는데 여기서 중요한 것이

   ![image-20210729221812152](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729221812152.png)

   ![image-20210729222012573](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729222012573.png)

   h2에 접속할 때 쓰이는 JDBC URL과 설정파일(persistence.xml)에 적은 jdbc.url이 동일해야한다.

   그리고 사용자명과 비밀번호도 똑같이 맞춰야 한다.



![image-20210729222251718](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729222251718.png)



접속 후 Member테이블 생성

이제 테이블을 만들었으니 이 테이블과 매핑이 되는 Member라는 클래스를 만들 것이다.

테이블을 생성하면 먼저 @Entity 어노테이션을 반드시 걸어준다.

그래야 이게 JPA로 돌아간다는 걸 알기 때문이다.

db에 있는 id와 name을 만들어주는데 jpa에게 pk값이 어떤건지 알려주어야한다.

![image-20210729222900386](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729222900386.png)

그리고 겟터 셋터도 만들어준다.



##### 실제로 돌아가는지 보기 위해 멤버를 저장해보겠다!

어떻게?

EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만들어놔야한다.

그리고 실제로 디비에 저장하는 것은 매 트렌젝션마다 entityManager를 만들어줘야한다.

![image-20210729223355835](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729223355835.png)

이렇게 멤버를 넣어주고 돌려보았지만 에러가 난다.



왜냐하면?

JPA는 트렌젝션이라는 것이 매우 중요하다.

모든것은 트렌젝션 단위 안에서 작업해야한다.



그래서 우리는 트렌젝션을 만들어 준다.

![image-20210729223720915](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729223720915.png)

트렌젝션을 생성하고 -> 시작하고 -> 로직을 만들어서 -> jpa에 저장하고 -> 트렌젝션 커밋하고-> 닫아준다.



run해보면?

![image-20210729223811434](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729223811434.png)

콘솔창에 이렇게 쿼리문이 뜬다.

![image-20210729223846835](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729223846835.png)

왜냐하면 우리가 설정파일에 이렇게 쿼리문이 보이고, 포멧팅을 해주고, 코멘트까지 나오도록 설정해놨기 때문이다.



![image-20210729223926379](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729223926379.png)

그러고 디비에 와보면 이렇게 값이 들어와있다.



![image-20210729224019111](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729224019111.png)

다시 이렇게 아이디와 이름을 바꿔서 런하고 디비에 와보면

![image-20210729224043211](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729224043211.png)

이렇게 두번째로 넣어줬던 값도 들어와있다.



이게바로 JPA가 맵핑 정보를 보고 직접 값을 넣어주는 것이다.

우리가 쿼리문을 작성한 것은 하나도 없다.





![image-20210729224207012](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729224207012.png)

만약 테이블명과 클래스명이 다르다면 이런식으로 써놓으면

디비에 이름이 "USER"인 테이블과 맵핑된다.

![image-20210729224305126](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729224305126.png)

컬럼명이 다른경우엔 이런식으로 써줌.



일단 에러없이 잘 동작했지만 위 코드는 안 좋은 코드다.

왜냐하면 tx.commit에서 에러가 났을 때 em.close()와 emf.close()모두 동작하지 못하기 때문이다.



'''

```
package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();/*트렌젝션 생성*/
        tx.begin(); /*트렌젝션 시작*/

        try {
            //code
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB"); /*로직을 만들고*/

            em.persist(member);/*jpa에 저장하고*/

            tx.commit(); /*커밋*/
        }catch (Exception e) {
            tx.rollback();
        }finally {
            em.close(); 
        }
        emf.close();
    }
}
```

그래서 우리는 try~catch안에다가

문제가 생기면 tx.rollback()해주고

작업이 끝나면 엔티티매니저가 닫아줄 수 있도록 코드를 수정했다.

그리고 전체 애플리케이션이 끝나면 엔티티매니저팩토리도 닫아버렸다.



![image-20210729233348378](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729233348378.png)

##### 조회

![image-20210729233435010](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729233435010.png)

정확히 조회됨



![image-20210729233546635](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729233546635.png)

##### 삭제

삭제는 이렇게 remove라고 쓰고 괄호 안에 찾은 애를 넣어주면

delete쿼리가 작성되면서 삭제된다.



![image-20210729233806084](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729233806084.png)

##### 수정

em.persist()안해줘도 알아서 저장해줌.

![image-20210729233941057](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729233941057.png)

##### 콘솔창

![image-20210729234005254](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210729234005254.png)

##### DB

디비 이름이 HelloA -> HelloJPA로 바뀌어있다.



#### 어떻게 값만 바꿨는데 수정되고 저장되는걸까?

JPA를 통해 엔티티를 가져오면 얘는 제이피에이가 관리를한다.

그리고 JPA가 변경 됫는지 안됏는지 트렌젝션이 커밋하는 시점에 다 체크를한다.

그래서 무언가 바뀐게 잇으면 update쿼리를 만들어서 날리고 커밋한다.



#### 주의점!

• 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에 서 공유

• 엔티티 매니저는 쓰레드간에 공유X (사용하고 버려야 한다).

• JPA의 모든 데이터 변경은 트랜잭션 안에서 실행





## JPQL 소개

- 가장 단순한 조회방법이다.
- 나이가 18살 이상인 회원을 모두 검색하고 싶다면? 이럴때 JPQL을 사용한다..

  수십-수백개의 테이블에서 내가 원하는 데이터를 쉽게 가져오기 위해 JPA에서 JPQL이라는 것으로 도와준다.

- 객체를 대상으로 하는 객체지향 쿼리
- 방언에 맞춰서 각 디비에 맞게 쿼리문을 번역해준다.

- JPA는 SQL을 추상화한 JPQL이라는 객체지향쿼리언어를 제공한다
- JPQL은 엔티티 객체를 대상으로 쿼리, SQL은 테이블을 대상으로 쿼리!

# 영속성 컨텍스트 1

### JPA에서 가장 중요한 2가지

- 객체와 관계형 데이터베이스 매핑하기 (정적)
- 영속성 컨텍스트 (실제 JPA가 내부에서 도대체 어떻게 동작하나?)



### 엔티티 매니저 팩토리와 엔티티 매니저

![image-20210802182351715](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802182351715.png)



엔티티매니저팩토리를 통해서 고객의 요청이 올 때마다 엔티티매니저를 생성한다.

이 엔티티 매니저는 내부적으로 데이터베이스 커넥션을 사용하여 디비를 사용하게 된다.



그러면 영속성 컨텍스트는 도대체 뭘까?

- "엔티티를 영구 저장하는 환경" 이라는 뜻

- EntityManager.persist(entity); -> 영속성컨텍스트를 통해서 entity를 영구저장한다.





영속성 컨텍스트는 논리적인 개념으로 눈에 보이지 않는다.

엔티티 매니저를 통해서 영속성 컨텍스트에 접근한다.

엔티티매니저를 생성하면 1:1로 영속성 컨텍스트가 생성된다.





### 엔티티의 생명주기

• 비영속 (new/transient) 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태

• 영속 (managed) 영속성 컨텍스트에 관리되는 상태

• 준영속 (detached) 영속성 컨텍스트에 저장되었다가 분리된 상태

• 삭제 (removed) 삭제된 상태





### 비영속 상태란?

:JPA와 전혀 관련이 없는 상태

![image-20210802183057985](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802183057985.png)





### 영속성 상태?

: 객체를 생성한 후에 EntityManager를 얻어와서 그 안에 persist로 객체를 넣는다.

![image-20210802183124948](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802183124948.png)





![image-20210802183158336](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802183158336.png)



이렇게 영속상태가 된다고만 해서  디비에 저장되는 것은 아니다.

디비는 이후에 커밋을 해야 저장된다.



### 준영속 , 삭제 상태란?

![image-20210802183401221](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802183401221.png)



### 영속성 컨텍스트의 이점

• 1차 캐시

: 맵에 저장하면 먼저 1차캐시에 가서 값을 찾는다.

엔티티매니저는 데이터베이스 트랜젝션 단위로 만들고 끝나면 종료한다.

고객의 요청이 들어와 비지니스가 끝나버리면 캐시가 다 지워진다.

데이터베이스에서 조회를 하면 데이터베이스트랜젝션 단위로 ..

데이터베이스 트랜잭션 안에서만 효과가 있다.



'''

```
 //비영속
  Member member = new Member();
  member.setId(101L);
  member.setName("HelloJPA");

  //영속
  System.out.println("=== BEFORE ===");
  em.persist(member);
  System.out.println("=== AFTER ===");

Member findMember = em.find(Member.class, 101L);

System.out.println("findMember.id =" + findMember.getId());
  System.out.println("findMember.id =" + findMember.getName());
```

console

![image-20210802185311917](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802185311917.png)

조회를 했는데 select쿼리가 나오지 않았다.

왜냐하면 member값이 1차쿠키에 저장되니까

값을 DB에서 가져오는 것이 아니라 1차쿠키에서 먼저 찾아서 가지고 오는 것.



이번엔 새로 만들어 볼게





똑같은걸 두번 조회할 땐 두번째 부터는 1차캐시에서 가지고 온다









• 동일성(identity) 보장

영속엔티티의 동일성을 보장해준다.

'''

```java
Member a = em.find(Member.class, member1");
Member b = em.find(Member.class, member1");
                   
System.out.println(a == b); //동일성 비교 true
```

1차 캐시로 반복 가능한 읽기(REPEATABLE READ)등급의 트랜잭션 격리수준을

데이터베이스가 아닌 애플리케이션 차원에서 제공







• 트랜잭션을 지원하는 쓰기 지연 (transactional write-behind)

영속성컨텍스 안에는 1차캐시도 있지만 쓰기지연 SQL저장소라는 곳도 있다.

JPA가 A를 1차캐시에 저장하고 쿼리를 생성해서 쓰기지연 저장소에 쌓아둔다.



B를 넣으면 이때도 1차캐시에 저장해서 쓰기지연 SQL저장소에 쌓아둔다,



커밋하면 쓰기지연 SQL에 있던 애들이 Flush되면서 날라가고 실제 DB저장소에 저장된다

'''

```
EntityTransaction tx = em.getTransaction();/*트렌젝션 생성 */
        //엔티티 매니저는 데이터 변경시 트랜젝션을 시작해야 한다.
        tx.begin(); /*트렌젝션 시작*/


            //영속

            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");

            em.persist(member1);
            em.persist(member2);
            //여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
            System.out.println("=================");

            //커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
            tx.commit(); /*커밋*/

```



console

![image-20210802190838570](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802190838570.png)

콘솔창을 보면 "====" 이후에 쿼리문이 나오는 것을 알 수 있다. -> 커밋 후 SQL생성



• 변경 감지(Dirty Checking)

:

- 엔티티 수정

```java
Member member = em.find(Member.class, 150L); //찾아와서
member.setName("ZZZZZ"); //값만 변경
```

jpa는 변경감지라는 기능으로 엔티티를 변경할 수 있는 기능이 제공된다

마치 우리가 생각하기에는 엔티티의 값을 바꾸려면 값을 바꾸고 update해달라는 코드를 써야할 것 같지만 우리는 값만 바꿔주고 저장하라는 코드를 작성하지 않아도 된다.



커밋을 하면 엔티티와 스냅샷을 비교한다.

1차캐시 안에는 pk,entity,스냅샷(내가 값을 읽어온 최초 시점을 스냅샷으로 떠놓음)이 있다.

내가 값을 변경해서 커밋되는 시점에 jpa가 1차캐시안의 값을 다 비교한다

비교해보고 값이 바껴있으면 sql저장소에 저장된 업데이트 쿼리를 데이터베이스저장소에 반영한다.



- - 엔티티 삭제

- ```java
    //삭제 대상 엔티티 조회 
    
    Member memberA = em.find(Member.class, “memberA"); 
    em.remove(memberA); //엔티티 삭제
    ```

• 지연 로딩(Lazy Loading)


## 플러시

영속성 컨텍스트의 변경내용을 데이터베이스에 반영하는 것.

영속성 컨텍스트의 쿼리를 디비에 날려주는 것.

데이터베이스가 커밋되면 플러시가 자동으로 발생한다.



플러시가 발생하면?

- 변경이 감지됨
- 수정된 엔티티를 쓰기지연 SQL저장소에 등록한다
- 쓰기지연 Sql저장소의 쿼리를 데이터베이스에 전송한다.



영속성 컨텍스트를 어떻게 플러시하는가?

- em.flush()라고 직접 호출하기
- 트랜잭션 커밋 - 자동 호출
- JPQL 쿼리 호출 - 자동 호출



'''

```
Member member = new Member(200L, "member200");
em.persist(member);

em.flush();//강제로 호출.

System.out.println("=================");

tx.commit(); /*커밋*/
```

![image-20210802221450979](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802221450979.png)

플러시를 해버리니까 커밋도 하기 전에 디비에 저장되어버린다.





### JPQL쿼리 실행 시 플러시가 자동으로 호출되는 이유

```java
    em.persist(memberA);
em.persist(memberB);
em.persist(memberC);
//중간에 JPQL 실행
    query = em.createQuery("select m from Member m", Member.class);
    List<Member> members= query.getResultList()
```

memberA,B,C를 em.persist로 저장을 했다.

실제 데이터베이스에 이 쿼리가 날라가지 않는다.



넣은 다음 바로 아래 코드로 조회를 해온다.

그럼 조회가 될까 안될까?

조회가 안된다.

왜? 디비에 쿼리라도 날라가야 디비에서 가져올텐데

지금은 쿼리조차 실행되지 않았기 때문이다.

그래서 jpa는 이런걸 방지하기 위해 무조건 플러시를 날려버린다.



## 플러시란?

• 영속성 컨텍스트를 비우지 않음

• 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화

• 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화  하면 됨

• 영속 -> 준영속
em.persist ->영속상태가 된다.

em.find 등을 해서 jpa를 사용하여 조회했는데 얘가 영속상태에 없다? 그러면

디비에서 가져와서 올려 -> 그럼 이걸 영속상태라고 한다.

• 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)

• 영속성 컨텍스트가 제공하는 기능을 사용 못함







준영속 상태로 만들기 예시 1)

'''

```java
Member member = em.find(Member.class, 150L);
member.setName("AAAAA");

em.detach(member); //이제 영속성에서 떼어낸다

System.out.println("=================");

tx.commit(); /*그럼 이제 쟤를 jpa에서 관리하지 않기때문에 커밋을 해도 아무일도 일어나지 않아*/
```

![image-20210802222550084](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802222550084.png)

select만 나오고 update쿼리는 나오지 않는다.



준영속 상태로 만들기 예시 2)

'''

```java
Member member = em.find(Member.class, 150L);
member.setName("AAAAA");

em.clear(); //통으로 다 날려버린다.

System.out.println("=================");

tx.commit(); /*완전 초기화 되기 때문에 커밋을 해도 아무일도 일어나지 않아*/
```

![image-20210802222807065](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802222807065.png)



준영속 상태로 만들기 예시 3)

em.close(); : 영속성 컨텍스트를 종료


