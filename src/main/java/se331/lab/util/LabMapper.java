package se331.lab.util;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import se331.lab.entity.AuctionItem;
import se331.lab.entity.AuctionItemDTO;
import se331.lab.entity.Event;
import se331.lab.entity.EventDTO;
import se331.lab.entity.Organizer;
import se331.lab.entity.OrganizerDTO;
import se331.lab.entity.Participant;
import se331.lab.entity.ParticipantDTO;
import se331.lab.entity.ParticipantWithEventsDTO;
import se331.lab.security.auth.OrganizerAuthDTO;

import java.util.List;

@Mapper
public interface LabMapper {
    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);
    AuctionItemDTO getAuctionItemDTO(AuctionItem auctionItem);
    List<AuctionItemDTO> getAuctionItemDTO(List<AuctionItem> auctionItems);
    EventDTO getEventDto(Event event);
    List<EventDTO> getEventDto(List<Event> events);
    OrganizerDTO getOrganizerDTO(Organizer organizer);
    List<OrganizerDTO> getOrganizerDTO(List<Organizer> organizers);
    @Mapping(target = "roles", source = "user.roles")
    OrganizerAuthDTO getOrganizerAuthDTO(Organizer organizer);
    ParticipantDTO getParticipantDTO(Participant participant);
    List<ParticipantDTO> getParticipantDTO(List<Participant> participants);
    ParticipantWithEventsDTO getParticipantWithEventsDTO(Participant participant);
    List<ParticipantWithEventsDTO> getParticipantWithEventsDTO(List<Participant> participants);
}
