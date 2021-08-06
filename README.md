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



# 엔티티 매핑



### 목차

• 객체와 테이블 매핑

• 데이터베이스 스키마 자동 생성

• 필드와 컬럼 매핑

• 기본 키 매핑

• 실전 예제 - 1. 요구사항 분석과 기본 매핑



### 엔티티 매핑 소개

• 객체와 테이블 매핑: @Entity, @Table

• 필드와 컬럼 매핑: @Column

• 기본 키 매핑: @Id

• 연관관계 매핑: @ManyToOne,@JoinColumn





## 객체와 테이블 매핑

####  @Entity

• @Entity가 붙은 클래스는 JPA가 관리, 엔티티라 한다.

• JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수

• 주의

- 기본 생성자 필수(파라미터가 없는 public 또는 protected 생성자)

- final 클래스, enum, interface, inner 클래스 사용X

- 저장할 필드에 final 사용 X



#### @Entity 속성 정리

• 속성: name

• JPA에서 사용할 엔티티 이름을 지정한다.

• 기본값: 클래스 이름을 그대로 사용(예: Member)

• 같은 클래스 이름이 없으면 가급적 기본값을 사용한다



#### Table

• @Table은 엔티티와 매핑할 테이블 지정

![image-20210802225358525](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802225358525.png)

![image-20210802225440145](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802225440145.png)





## 데이터베이스 스키마 자동 생성

#### 데이터베이스 스키마 자동 생성

• DDL을 애플리케이션 실행 시점에 자동 생성

실행시점에 create문으로 db를 만들어놓고 시작할 수 있다.

• 테이블 중심 -> 객체 중심

• 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성

varchar , varchar2이런거 적절하게 맞게 생성해준다.

• 이렇게 생성된 DDL은 개발 장비에서만 사용

• 생성된 DDL은 운영서버에서는 사용하지 않거나, 적절히 다듬 은 후 사용



#### 데이터베이스 스키마 자동 생성 - 속성

##### hibernate.hbm2ddl.auto 라는 옵션을 걸어준다. (persistence.xml에 추가)

![image-20210802230921149](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802230921149.png)



![image-20210802231035154](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802231035154.png)

run해보면 기존에 있던 테이블을 지우고 새로 테이블을 생성해낸다.



![image-20210802231133438](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802231133438.png)

만약 age를 새로 추가해보면?

![image-20210802231234468](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802231234468.png)

원래 있던 테이블을 지우고 age컬럼을 추가해서 새 테이블을 만든 걸 볼 수 있다.

![image-20210802231311656](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802231311656.png)

DB에서 확인해보니 DB도 AGE컬럼이 추가되어 있는 것을 볼 수 있다.



보통같은 경우에는 age를 추가하고 alter를 써서 테이블 컬럼을 수정하거나 한다.

그러나 데이터베이스 스키마 자동생성기능으로 이렇게 간단하게 수정할 수 있다.



![image-20210802231352672](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210802231352672.png)

persistence.xml의 옵션에 위와 같은 명령어들을 넣어서 사용할 수 있다.



- create-drop: 보통 test를 돌릴 때 깔끔하게 데이터를 날리고싶을 때 사용한다.

- update: 만약 내가 age라는 컬럼을 추가하고 싶은데 테이블을 drop하고 싶지 않을 때 사용한다.

  하지만 지우는 건 안되고 추가하는 것만 된다!

- validate: 예를들어 새로운 컬럼을 추가하였을 때 테이블에 그 컬럼이 없을 시 에러가 난다.

- none: 나는 이 기능이 싫을 때 이 옵션 자체를 주석처리 하거나 옵션에 none이라고 적는다.



#### 데이터베이스 스키마 자동 생성 - 주의

• 운영 장비에는 절대 create, create-drop, update 사용하면 안된다.

• 개발 초기 단계는 create 또는 update

• 테스트 서버는 update 또는 validate

: ★ 테스트 서버에서는 create쓰면 데이터가 다 날라간다!!

• 스테이징과 운영 서버는 validate 또는 none



#### DDL 생성 기능

• 제약조건 추가: 회원 이름은 필수, 10자 초과X

• @Column(nullable = false, length = 10)

• 유니크 제약조건 추가

• @Table(uniqueConstraints = {@UniqueConstraint( name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"} )})

• DDL 생성 기능은 DDL을 자동 생성할 때만 사용되고 JPA의 실행 로직에는 영향을 주지 않는다.





## 필드와 컬럼 매핑

##### 요구사항 추가

1. 회원은 일반 회원과 관리자로 구분해야 한다.
2. 회원 가입일과 수정일이 있어야 한다.
3. 회원을 설명할 수 있는 필드가 있어야 한다. 이 필드는 길이 제 한이 없다



## 기본 키 매핑

##### 기본 키 매핑 어노테이션

• @Id

• @GeneratedValue

```java
@Id @GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
```



## 기본 키 매핑 방법

• 직접 할당: @Id만 사용

• 자동 생성(@GeneratedValue)

​    • IDENTITY: 데이터베이스에 위임, MYSQL

​    • SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE

​        • @SequenceGenerator 필요 • TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용

• @TableGenerator 필요

• AUTO: 방언에 따라 자동 지정, 기본값



### 1) 직접 할당

- @Id사용

  :그냥 Id를 어노테이션 해주기만 하면 된다.





### 2) IDENTITY 전략 - 특징

• 기본 키 생성을 데이터베이스에 위임

• 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용 (예: MySQL의 AUTO_ INCREMENT)

• JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행

• AUTO_ INCREMENT는 데이터베이스에 INSERT SQL을 실행 한 이후에 ID 값을 알 수 있음

• IDENTITY 전략은 em.persist() 시점에 즉시 INSERT SQL 실행 하고 DB에서 식별자를 조회



## IDENTITY 전략 - 매핑

![image-20210804213509618](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210804213509618.png)



## SEQUENCE 전략 - 특징

• 데이터베이스 시퀀스는 유일한 값을 순서대로 생성하는 특별한

데이터베이스 오브젝트(예: 오라클 시퀀스)

• 오라클, PostgreSQL, DB2, H2 데이터베이스에서 사용



## SEQUENCE 전략 - 매핑

![image-20210804213549443](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210804213549443.png)



## SEQUENCE - @SequenceGenerator

- 주의: allocationSize 기본값 = 50

![image-20210804213732321](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210804213732321.png)



## TABLE 전략

• 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉 내내는 전략

• 장점: 모든 데이터베이스에 적용 가능

• 단점: 성능



## TABLE 전략 - 매핑



![image-20210804213821945](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210804213821945.png)





## @TableGenerator - 속성

![image-20210804213838882](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210804213838882.png)



## 권장하는 식별자 전략

• 기본 키 제약 조건: null 아님, 유일, 변하면 안된다.

• 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대 체키)를 사용하자.

• 예를 들어 주민등록번호도 기본 키로 적절하기 않다.

• 권장: Long형 + 대체키 + 키 생성전략 사용

# 양방향 연관관계와 연관관계의 주인

객체-테이블

테이블-외래키

를 가지고 연관관계를 가지고 조인함



## 양방향 매핑

![image-20210805211320053](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805211320053.png)

테이블 연관관계는 단방향 연관관계의 테이블연관관계와 똑같다.

##### 왜? 테이블은 TEAM_ID(FK)로 조인하면 된다.

반대로 TEAM의 입장에서 우리 팀에 어떤 멤버가 있는지 알고싶으면

TEAM_ID(PK)로 TEAM_ID(FK)랑 조인하면 된다.

테이블의 연관관계에는 방향이란 개념없이 FK하나만 집어넣으면 양쪽의 관계를 다 알수 있다.



반면에 객체의 경우에는 반대로 왔다갔다 할 수가 없다.





## 양방향매핑 (Member 엔티티는 단방향과 동일)

![image-20210805212039463](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805212039463.png)



## 양방향 매핑 (Team 엔티티는 컬렉션 추가)

![image-20210805212056457](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805212056457.png)

```java
@OneToMany(mappedBy = "team") //(일대다에서 "team"과 매핑되어있다)
```





## 양방향 매핑 (반대 방향으로 객체 그래프 탐색)

![image-20210805212151452](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805212151452.png)





## 연관관계의 주인과 mappedBy

• mappedBy = JPA의 멘탈붕괴 난이도

• mappedBy는 처음에는 이해하기 어렵다.

• 객체와 테이블간에 연관관계를 맺는 차이를 이해해야 한다. ★



## 객체와 테이블이 관계를 맺는 차이

##### • 객체 연관관계 = 2개

- 회원 -> 팀 연관관계 1개(단방향)

-  팀 -> 회원 연관관계 1개(단방향)

##### • 테이블 연관관계 = 1개

- 회원 <-> 팀의 연관관계 1개(양방향)



![image-20210805212714815](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805212714815.png)

1) 객체에서는 참조가 양쪽에 있다.

   단방향이 두개가 있는 거나 다름 없음

2) 테이블은 양방향이라고는 하지만 사실상 이건 방향이 없는 것이다.

   그냥 키 하나로 왔다갔다 할 수 있기 때문에.



## 객체의 양방향 관계

• 객체의 양방향 관계는 사실 양방향 관계가 아니라 서로 다른 단 뱡향 관계 2개다.

• 객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어 야 한다.

![image-20210805212837171](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805212837171.png)



## 테이블의 양방향 연관관계

• 테이블은 외래 키 하나로 두 테이블의 연관관계를 관리

• MEMBER.TEAM_ID 외래 키 하나로 양방향 연관관계 가짐 (양쪽으로 조인할 수 있다.)

> SELECT *  FROM MEMBER M JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID

> SELECT *  FROM TEAM T JOIN MEMBER M ON T.TEAM_ID = M.TEAM_ID

-> 반대로도 가능하다. 이게 바로 조인키 하나로 다 된다는 것.



## 둘 중 하나로 외래 키를 관리해야 한다.

![image-20210805212951875](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805212951875.png)

그럼 우리는 값을 수정하려고 할때 어떤 값을 바꿔야 할까?

멤버에 있는 Team과 Team에 있는 List members 중 하나로 외래키를 잡아야한다,

이게 바로 연관관계 주인이다. ▽



## 연관관계의 주인(Owner)

#### 양방향 매핑 규칙

• 객체의 두 관계중 하나를 연관관계의 주인으로 지정

• 연관관계의 주인만이 외래 키를 관리(등록, 수정)

• 주인이 아닌쪽은 읽기만 가능

• 주인은 mappedBy 속성 사용X

• 주인이 아니면 mappedBy 속성으로 주인 지정



## 누구를 주인으로?

• 외래 키가 있는 있는 곳을 주인으로 정해라

• 여기서는 Member.team이 연관관계의 주인

![image-20210805221237404](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805221237404.png)

Member의 Team을 연관관계 주인으로 하는 것이 낫다.

왜? Team의 List members로 하면 Team객체에 업데이트 쿼리를 날렸는데 Member에 업데이트 쿼리가 적용된다.

디비 입장에서 보면 외래키가 있는 곳이 무조건 (다, manyToOne), 없는 곳이 무조건 (1,OneToMany)

그냥 외래키가 있는 곳을 주인으로 정하면 헷갈리지 않는다.



## 양방향 매핑시 가장 많이 하는 실수(연관관계의 주인에 값을 입력하지 않음)



![image-20210805221923948](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805221923948.png)



## 양방향 매핑시 연관관계의 주인에 값을 입력해야 한다. (순수한 객체 관계를 고려하면 항상 양쪽다 값을 입력해야 한다.

![image-20210805221952144](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805221952144.png)

양방향매핑을 할 때는 가급적이면 양쪽에 모두 값을 넣어주어라.



## 양방향 연관관계 주의 - 실습

• 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자

• 연관관계 편의 메소드를 생성하자

• 양방향 매핑시에 무한 루프를 조심하자

-  예: toString(), lombok, JSON 생성 라이브러리





## 양방향 매핑 정리

• 단방향 매핑만으로도 이미 연관관계 매핑은 완료

• 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추 가된 것 뿐

• JPQL에서 역방향으로 탐색할 일이 많음

• 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 됨 (테이블에 영향을 주지 않음)



## 연관관계의 주인을 정하는 기준

• 비즈니스 로직을 기준으로 연관관계의 주인을 선택하면 안됨

##### • 연관관계의 주인은 외래 키의 위치를 기준으로 정해야함



## 실전예제 2 -연관관계 매핑 시작

### 테이블 구조

- 테이블 구조는 이전과 같다.

![image-20210805230516111](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805230516111.png)

### 객체 구조

- 참조를 사용하도록 변경

![image-20210805230544148](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210805230544148.png)



# 다양한 연관관계 매핑



## 목차

• 연관관계 매핑시 고려사항 3가지

• 다대일 [N:1]

• 일대다 [1:N]

• 일대일 [1:1]

• 다대다 [N:M]



## 연관관계 매핑시 고려사항 3가지

• 다중성

• 단방향, 양방향

• 연관관계의 주인



## 다중성

• 다대일: @ManyToOne

• 일대다: @OneToMany

• 일대일: @OneToOne

• 다대다: @ManyToMany (실무에서 쓰면 안된다)



## 단방향, 양방향

• 테이블

- 외래 키 하나로 양쪽 조인 가능

- 사실 방향이라는 개념이 없음

• 객체

- 참조용 필드가 있는 쪽으로만 참조 가능

- 한쪽만 참조하면 단방향

- 양쪽이 서로 참조하면 양방향 (객체는 양방향이라는게 없다. 사실 단방향이 두개. 양방향처럼 보이는 것 뿐)



## 연관관계의 주인

• 테이블은 외래 키 하나로 두 테이블이 연관관계를 맺음

• 객체 양방향 관계는 A->B, B->A 처럼 참조가 2군데

• 객체 양방향 관계는 참조가 2군데 있음. 둘중 테이블의 외래 키 를 관리할 곳을 지정해야함

• 연관관계의 주인: 외래 키를 관리하는 참조

• 주인의 반대편: 외래 키에 영향을 주지 않음, 단순 조회만 가능



# 다대일 [N:1]



## 다대일 단방향

![image-20210806120211322](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210806120211322.png)

항상 다 쪽에 외래키가 있어야한다.



## 다대일 단방향 정리

• 가장 많이 사용하는 연관관계

• 다대일의 반대는 일대다



## 다대일 양방향

![image-20210806120323189](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210806120323189.png)

반대쪽 사이드에 추가하면 된다.

반대쪽 사이드는 추가를 한다고 해서 아무런 영향을 주지 않는다.

반대쪽은 그냥 읽기만 하기 때문이다.



## 다대일 양방향 정리

• 외래 키가 있는 쪽이 연관관계의 주인

• 양쪽을 서로 참조하도록 개발



## 일대다 [1:N]

여기선은 1이 연관관계 주인이다.

1의 관점에서 연관관계를 관리하겠다.



## 일대다 단방향

이 모델은 권장하지 않는다.

![image-20210806120811641](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210806120811641.png)

현재 팀이 1이고 멤버가 N

팀은 멤버를 알고픈데 멤버는 팀을 알고싶지 않는 상태

무조건 N쪽에 외래키가 들어간다.



## 일대다 단방향 정리

• 일대다 단방향은 일대다(1:N)에서 일(1)이 연관관계의 주인

• 테이블 일대다 관계는 항상 다(N) 쪽에 외래 키가 있음

• 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하 는 특이한 구조

• @JoinColumn을 꼭 사용해야 함. 그렇지 않으면 조인 테이블 방식을 사용함(중간에 테이블을 하나 추가함)



## 일대다 단방향 정리

• 일대다 단방향 매핑의 단점

- 엔티티가 관리하는 외래 키가 다른 테이블에 있음

- 연관관계 관리를 위해 추가로 UPDATE SQL 실행

• 일대다 단방향 매핑보다는 다대일 양방향 매핑을 사용하자



## 일대다 양방향

![image-20210806122200033](https://raw.githubusercontent.com/heeseonyang/hello-spring/master/img/image-20210806122200033.png)

반대쪽에서도 보고싶으면

@ManyToOne

@JoinColumn(insertable=false, updatable=false)  를 넣어준다

## 일대다 양방향 정리

• 이런 매핑은 공식적으로 존재X

• @JoinColumn(insertable=false, updatable=false)

• 읽기 전용 필드를 사용해서 양방향 처럼 사용하는 방법

• 다대일 양방향을 사용하자