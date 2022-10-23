package board_example.board_01.domain.repository;
// 데이터의 조작을 담당하며 JPA 상속을 받음
import board_example.board_01.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
