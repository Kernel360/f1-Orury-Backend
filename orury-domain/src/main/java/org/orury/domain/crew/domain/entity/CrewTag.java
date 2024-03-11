package org.orury.domain.crew.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Entity(name = "crew_tag")
public class CrewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Column(name = "tag", nullable = false)
    private String tag;

    private CrewTag(
            Long id,
            Crew crew,
            String tag
    ) {
        this.id = id;
        this.crew = crew;
        this.tag = tag;
    }

    public static CrewTag of(
            Long id,
            Crew crew,
            String tag
    ) {
        return new CrewTag(
                id,
                crew,
                tag
        );
    }
}
