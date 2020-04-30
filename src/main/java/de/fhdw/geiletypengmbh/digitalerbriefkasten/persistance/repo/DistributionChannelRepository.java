package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.TargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionChannelRepository extends JpaRepository<DistributionChannel, Long> {
    DistributionChannel findByTitle(String title);
}
