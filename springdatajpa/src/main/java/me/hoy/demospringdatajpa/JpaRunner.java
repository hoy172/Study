package me.hoy.demospringdatajpa;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class JpaRunner implements ApplicationRunner {

    @PersistenceContext
    EntityManager entityManager;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Insert();
        query();
        JpaRepository();
    }

    @Autowired
    PostRepository postRepository;

    public void JpaRepository(){
        postRepository.findAll().forEach(System.out::println);
    }

    // query 작성시
    // hql, spql 디비에 독립적
    @Transactional
    public void query(){
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post AS p", Post.class);
        List<Post> resultList = query.getResultList();
        resultList.forEach(System.out::println);
    }
    public void typeSafeQuery(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = builder.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);
        query.select(root);

        List<Post> resultList = entityManager.createQuery(query).getResultList();
        resultList.forEach(System.out::println);
    }
    public void nativeQuery(){
        List<Post> resultList = entityManager.createNativeQuery("SELECT * FROM Post", Post.class).getResultList();
    }

    @Transactional
    public void Insert(){
        Account account = new Account();
        account.setUsername("hoy");
        account.setPassword("hibernate");


        Study study = new Study();
        study.setName("Spring data JPA");
        account.addStudy(study);
        Session session = entityManager.unwrap(Session.class);
        session.save(account);
        session.save(study);

        //1차캔시에 저장된 상태 == persistence context 에 저장된 상태
        // 객체의 변경사항을 모니터링 하고 있는 상태
        // 객체의 변경이 일어나면 변경사항을 반영해준다.
        // 이러한 점에서 성능상의 장점이 있다고 말 할 수있다.
        Account hoy = session.load(Account.class, account.getId());
        //dirty checking , Write Behind
        //업데이트 반영된다
        hoy.setUsername("hoy2");
        // 업데이트 쿼리가 일어나지 않는다
        hoy.setUsername("hoy");
        System.out.println("==================================");
        System.out.println(hoy.getUsername());


        //====================================================================
        Post post = new Post();
        post.setTitle("Spring Data JPA 언제보나....");

        Comment comment = new Comment();
        comment.setComment("빨리보고싶어요.");
        post.addComment(comment);

        Comment comment1 = new Comment();
        comment1.setComment("곧 보여드릴께요");
        post.addComment(comment1);

        session.save(post);
        //====================================================================

        Post post1 = session.get(Post.class, 3l);
        System.out.println(post1.getTitle());
    }
}
