package board_example.board_01.domain.entity;
// 데이터베이스 테이블과 매핑되는 객체
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

//인스턴스 반환하는 함수네?
//사용 이유: 데이터를 보호하기 위해 Q. 무엇으로부터? getter은 인스턴스 변수를 반환하고, setter은 인스턴스 변수를 대입하거나 수정함
//데이터에 할당될 값이 어떤 기준안에 있을 때 setter을 사용해 그 기준에 맞게 강제할 수 있다.
@Getter
//JPA가 관리하는 클래스 (JAP를 사용해서 테이블과 매핑할 클래스는 반드시 붙여야함)
@Entity
//파라미터가 없는 기본 생성자를 생성 접근자 레벨을 protected로  Q1. private로 설정하면 왜 에러가 날까요?
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//JPA에게 해당 entity는 auditiong기능을 사용함을 알림
// 데이터가 업데이트, 생성됨에 따라 createdAt, updatedAt를 업데이트 하는 것은 대부분의 DB에서 사용하게 된다.
//그래서 JPA는 이러한 기능을 기본적으로 제공하고 있다.
@EntityListeners(AuditingEntityListener.class)
public class Board {


    // == 'id' auto_Increment pk;
    // 기본키로 설정하겠다. = pk
    @Id
    //JAP가 기본키 값을 자동으로 생성  = Auto_Increment
    @GeneratedValue
    private Long id;

    // == 'author' valchar(10) NULL NULL;
    @Column(length = 10, nullable = false)
    private String author;

    // == 'title' varchar(100) NOT NULL;
    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

   // @OneToOne
    //@JoinColumn(name = "file_id") Q) 외래키로 설정하고 싶은데 왜 안될까? A) 굳이 외래키로 설정해야 될까?
    @Column
    private Long fileId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Builder
    public Board(Long id, String author, String title, String content, Long fileId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.fileId = fileId;
    }
}
