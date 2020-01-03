package hello.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.entity.Song;

/**
 * @author tu.ta1 on 2019-09-09
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

    Page<Song> findAll(Pageable pageable);

    Song findByTitleAndArtist(String title, String artist);

    Song findByFileName(String fileName);
}