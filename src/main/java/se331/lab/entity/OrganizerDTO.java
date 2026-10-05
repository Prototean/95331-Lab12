package se331.lab.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OrganizerDTO {
    Long id;
    String name;
    List<String> images;
    @Builder.Default
    List<OrganizerOwnEventsDTO> ownEvents = new ArrayList<>();
}
