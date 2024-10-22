//Autor: PR
package de.fhdw.geiletypengmbh.digitalerbriefkasten.service.ideas;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.AlreadyExistsException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.exceptions.DistributionChannelNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.ideas.DistributionChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributionChannelService {

    @Autowired
    private DistributionChannelRepository distributionChannelRepository;

    public DistributionChannel findById(Long id) {
        return distributionChannelRepository.findById(id).orElseThrow(DistributionChannelNotFoundException::new);
    }

    public DistributionChannel findByTitle(String title) {
        return distributionChannelRepository.findByTitle(title);
    }

    public List<DistributionChannel> findAll() {
        return distributionChannelRepository.findAll();
    }

    public DistributionChannel save(DistributionChannel distributionChannel) {
        if (findByTitle(distributionChannel.getTitle()) == null) {
            return distributionChannelRepository.save(distributionChannel);
        } else {
            throw new AlreadyExistsException("Vertriebskanal existiert bereits.");
        }
    }
}
