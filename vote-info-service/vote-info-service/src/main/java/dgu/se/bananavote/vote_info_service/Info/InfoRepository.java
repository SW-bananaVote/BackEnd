package dgu.se.bananavote.vote_info_service.Info;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoRepository extends JpaRepository<Info, Integer> {
    // 기본적으로 제공되는 findAll() 메소드가 있음
    //List<Info> findAll();
}
