package de.fhdw.geiletypengmbh.digitalerbriefkasten.auth.service;

import de.fhdw.geiletypengmbh.digitalerbriefkasten.controller.exceptions.DistributionChannelNotFoundException;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas.DistributionChannel;
import de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.repo.DistributionChannelRepository;
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
        return distributionChannelRepository.save(distributionChannel);
    }
}
