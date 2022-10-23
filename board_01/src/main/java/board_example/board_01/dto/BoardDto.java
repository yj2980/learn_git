package board_example.board_01.dto;
//Controller와 Service 사이에 데이터를 주고받음 (Data Access Object)
import board_example.board_01.domain.entity.Board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private Long fileId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //DTO에서 필요한 부분을 빌더 패턴을 통해 Entitiy로 만드는 일
    // Q) 왜 빌더 패턴을 사용해? 1. 생성자 파라미터가 많을 경우 가독성이 좋지 않음 2. 	2. 어떤 값을 먼저 설정하던 상관이 없음
    // 빌더를 통해 객체를 생성해
    public Board toEntity() {
        Board build = Board.builder()
                .id(id)
                .author(author)
                .title(title)
                .content(content)
                .fileId(fileId)
                .build();
        return build;
    }
    @Builder
    public BoardDto(Long id, String author, String title, String content, Long fileId, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.fileId = fileId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}




