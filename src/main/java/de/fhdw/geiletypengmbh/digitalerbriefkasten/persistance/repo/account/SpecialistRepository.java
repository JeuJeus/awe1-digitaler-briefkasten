//Autor: JB
package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.account;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.account.Specialist;

import java.util.List;

public interface SpecialistRepository extends UserRepository<Specialist> {

    public List<Specialist> findByProductLinesId(Long productLineId);
}
