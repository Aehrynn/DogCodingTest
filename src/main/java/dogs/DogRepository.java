package dogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Gilmariand on 25/06/2018.
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    @Query("SELECT d FROM Dog d WHERE d.name = ?1")
    Dog findByName(String name);
}
