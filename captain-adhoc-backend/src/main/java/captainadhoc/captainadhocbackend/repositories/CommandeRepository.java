package captainadhoc.captainadhocbackend.repositories;

import captainadhoc.captainadhocbackend.domain.Commande;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends CrudRepository<Commande, Long> {
}
