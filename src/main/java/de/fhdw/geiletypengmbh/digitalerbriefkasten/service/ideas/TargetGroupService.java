//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.AlreadyExistsException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.TargetGroupNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.TargetGroup;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.TargetGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TargetGroupService {

    @Autowired
    private TargetGroupRepository targetGroupRepository;

    public TargetGroup findById(Long id) {
        return targetGroupRepository.findById(id).orElseThrow(TargetGroupNotFoundException::new);
    }

    public TargetGroup findByTitle(String title) {
        return targetGroupRepository.findByTitle(title);
    }

    public List<TargetGroup> findAll() {
        return targetGroupRepository.findAll();
    }

    public TargetGroup save(TargetGroup targetGroup) {
        if (findByTitle(targetGroup.getTitle()) == null) {
            return targetGroupRepository.save(targetGroup);
        } else {
            throw new AlreadyExistsException("Zielgruppe existiert bereits");
        }
    }
}
