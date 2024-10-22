//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionChannelRepository extends JpaRepository<DistributionChannel, Long> {
    DistributionChannel findByTitle(String title);
}
