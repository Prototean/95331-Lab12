package se331.lab.config;

import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.AbstractPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se331.lab.entity.AuctionItem;
import se331.lab.entity.Bid;
import se331.lab.entity.Event;
import se331.lab.entity.Organizer;
import se331.lab.entity.Participant;
import se331.lab.repository.AuctionItemRepository;
import se331.lab.repository.BidRepository;
import se331.lab.repository.EventRepository;
import se331.lab.repository.OrganizerRepository;
import se331.lab.repository.ParticipantRepository;
import se331.lab.security.user.Role;
import se331.lab.security.user.User;
import se331.lab.security.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    final EventRepository eventRepostiory;
    final OrganizerRepository organizerRepository;
    final ParticipantRepository participantRepository;
    final AuctionItemRepository auctionItemRepository;
    final BidRepository bidRepository;
    final UserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Organizer org1, org2, org3;
        org1 = organizerRepository.save(Organizer.builder()
                .name("CAMT").build());
        org2 = organizerRepository.save(Organizer.builder()
                .name("CMU").build());
        org3 = organizerRepository.save(Organizer.builder()
                .name("ChiangMai").build());
        Event tempEvent;
        tempEvent = eventRepostiory.save(Event.builder()
                .category("Academic")
                .title("Midterm Exam")
                .description("A time fore taking the exam")
                .location("CAMT Building")
                .date("3rd Sept")
                .time("3.00-4.00 pm.")
                .petsAllowed(false)
                .build());
        tempEvent.setOrganizer(org1);
        org1.getOwnEvents().add(tempEvent);
        tempEvent = eventRepostiory.save(Event.builder()
                .category("Academic")
                .title("Commencement Day")
                .description("A time for celebration")
                .location("CAMT Convention hall")
                .date("21th Jan")
                .time("8.00am - 4.00 pm.")
                .petsAllowed(false)
                .build());
        tempEvent.setOrganizer(org1);
        org1.getOwnEvents().add(tempEvent);
        tempEvent = eventRepostiory.save(Event.builder()
                .category("Cultural")
                .title("Loy Krathong")
                .description("A time for Krathong")
                .location("Ping River")
                .date("21th Nov")
                .time("8.00-10.00 pm.")
                .petsAllowed(false)
                .build());
        tempEvent.setOrganizer(org2);
        org2.getOwnEvents().add(tempEvent);
        tempEvent = eventRepostiory.save(Event.builder()
                .category("Cultural")
                .title("Songkran")
                .description("Let's Play Water")
                .location("Ching Mai Moat")
                .date("13th April")
                .time("10.00am - 6.00 pm.")
                .petsAllowed(true)
                .build());
        tempEvent.setOrganizer(org3);
        org3.getOwnEvents().add(tempEvent);
        addUser();
        org1.setUser(user1);
        user1.setOrganizer(org1);
        org2.setUser(user2);
        user2.setOrganizer(org2);
        org3.setUser(user3);
        user3.setOrganizer(org3);
    }
    User user1,user2,user3;
    private void addUser() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user1 = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .firstname("admin")
                .lastname("admin")
                .email("admin@admin.com")
                .enabled(true)
                .build();

        user2 = User.builder()
                .username("user")
                .password(encoder.encode("user"))
                .firstname("user")
                .lastname("user")
                .email("enable@user.com")
                .enabled(true)
                .build();

        user3 = User.builder()
                .username("disableUser")
                .password(encoder.encode("disableUser"))
                .firstname("disableUser")
                .lastname("disableUser")
                .email("disableUser@user.com")
                .enabled(false)
                .build();

        user1.getRoles().add(Role.ROLE_USER);
        user1.getRoles().add(Role.ROLE_ADMIN);

        user2.getRoles().add(Role.ROLE_USER);
        user3.getRoles().add(Role.ROLE_USER);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<Event> events = eventRepostiory.findAll();
        Event event1 = events.get(0);
        Event event2 = events.get(1);
        Event event3 = events.get(2);
        Event event4 = events.get(3);

        Participant par1,par2,par3,par4,par5;
        par1 = participantRepository.save(Participant.builder()
                .name("Thanachai Srisawat")
                .telNo("081-111-1111")
                .build());
        par2 = participantRepository.save(Participant.builder()
                .name("Nattapong Wongchai")
                .telNo("082-222-2222")
                .build());
        par3 = participantRepository.save(Participant.builder()
                .name("Suda Chaiyaporn")
                .telNo("083-333-3333")
                .build());
        par4 = participantRepository.save(Participant.builder()
                .name("Malee Jaidee")
                .telNo("084-444-4444")
                .build());
        par5 = participantRepository.save(Participant.builder()
                .name("Anan Boonmee")
                .telNo("085-555-5555")
                .build());

        par1.getEventHistories().addAll(List.of(event1, event2, event3));
        par2.getEventHistories().addAll(List.of(event2, event3, event4));
        par3.getEventHistories().addAll(List.of(event1, event3, event4));
        par4.getEventHistories().addAll(List.of(event1, event2));
        par5.getEventHistories().addAll(List.of(event1, event2, event4));

        AuctionItem auction1 = createAuctionItem("Vintage Rolex Submariner watch", "Watch",
                45000.0, 47500.0, 52000.0);
        AuctionItem auction2 = createAuctionItem("Signed Michael Jordan jersey", "Memorabilia",
                18000.0, 19500.0, 23000.0);
        AuctionItem auction3 = createAuctionItem("Antique teak dining table", "Furniture",
                8000.0, 9200.0, 11000.0);
        AuctionItem auction4 = createAuctionItem("First edition Harry Potter book", "Book",
                30000.0, 34000.0, 41000.0);
        AuctionItem auction5 = createAuctionItem("Fender Stratocaster electric guitar", "Instrument",
                25000.0, 26500.0, 29000.0);

        auction1.setSuccessfulBid(auction1.getBids().get(2));
        auction3.setSuccessfulBid(auction3.getBids().get(1));
        auction5.setSuccessfulBid(auction5.getBids().get(2));
        auctionItemRepository.saveAll(List.of(auction1, auction3, auction5));
    }

    private AuctionItem createAuctionItem(String description, String type,
                                          Double amount1, Double amount2, Double amount3) {
        AuctionItem item = auctionItemRepository.save(AuctionItem.builder()
                .description(description)
                .type(type)
                .build());
        LocalDateTime now = LocalDateTime.now();
        item.getBids().add(bidRepository.save(Bid.builder()
                .amount(amount1)
                .datetime(now.minusDays(3))
                .item(item)
                .build()));
        item.getBids().add(bidRepository.save(Bid.builder()
                .amount(amount2)
                .datetime(now.minusDays(2))
                .item(item)
                .build()));
        item.getBids().add(bidRepository.save(Bid.builder()
                .amount(amount3)
                .datetime(now.minusDays(1))
                .item(item)
                .build()));
        return item;
    }

}
