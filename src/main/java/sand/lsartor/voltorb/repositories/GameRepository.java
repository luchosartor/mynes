package sand.lsartor.voltorb.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sand.lsartor.voltorb.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

}
