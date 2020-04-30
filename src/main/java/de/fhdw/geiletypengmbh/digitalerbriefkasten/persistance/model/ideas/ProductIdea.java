package de.fhdw.geiletypengmbh.digitalerbriefkasten.persistance.model.ideas;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "productIdea")
public class ProductIdea extends Idea {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "targetGroup_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TargetGroup targetGroup;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "distributionChannel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DistributionChannel distributionChannel;

}
