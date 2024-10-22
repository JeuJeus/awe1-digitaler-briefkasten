//Autor: JF
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Idea;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdeaRepository<T extends Idea> extends JpaRepository<T, Long> {
    List<T> findByTitle(String title);

    List<T> findBySpecialistIdAndStatus(Long specialistId, Status status);

    List<T> findByStatus(Status status);

    Long countByspecialistIdAndStatus(Long specialistId, Status status);
}
