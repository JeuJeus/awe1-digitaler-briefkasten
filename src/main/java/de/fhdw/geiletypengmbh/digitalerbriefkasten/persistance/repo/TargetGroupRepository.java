package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Field;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.TargetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetGroupRepository extends JpaRepository<TargetGroup, Long> {
    TargetGroup findByTitle(String title);
}
