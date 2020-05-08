package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "productIdea")
public class ProductIdea extends Idea {

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("productIdeas")
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "productIdea_targetGroup",
            joinColumns = @JoinColumn(name = "productIdea_id"),
            inverseJoinColumns = @JoinColumn(name = "targetGroup_id"))
    private Set<TargetGroup> targetGroups;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("productIdeas")
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(
            name = "productIdea_distributionChannel",
            joinColumns = @JoinColumn(name = "productIdea_id"),
            inverseJoinColumns = @JoinColumn(name = "distributionChannel_id"))
    private Set<DistributionChannel> distributionChannels;

    public Set<TargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(Set<TargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public Set<DistributionChannel> getDistributionChannels() {
        return distributionChannels;
    }

    public void setDistributionChannels(Set<DistributionChannel> distributionChannels) {
        this.distributionChannels = distributionChannels;
    }

    public boolean containsTargetGroup(TargetGroup targetGroupToFind) {
        return targetGroups.stream().anyMatch(targetGroup -> targetGroup.getId() == targetGroupToFind.getId());
    }

    public boolean containsDistributionChannel(DistributionChannel distributionChannelToFind) {
        return distributionChannels.stream().anyMatch(distributionChannel -> distributionChannel.getId() == distributionChannelToFind.getId());
    }
}
