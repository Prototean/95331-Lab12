package se331.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se331.lab.entity.Organizer;
import se331.lab.service.OrganizerService;
import se331.lab.util.LabMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrganizerController {
    final OrganizerService organizerService;
    @GetMapping("/organizers")
    ResponseEntity<?> getOrganizers(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page) {
        // without the paging params the whole list is returned, the event form
        // needs every organizer for its dropdown
        if (perPage == null && page == null) {
            List<Organizer> output = organizerService.getAllOrganizer();
            HttpHeaders responseHeader = new HttpHeaders();
            responseHeader.set("x-total-count", String.valueOf(output.size()));
            return new ResponseEntity<>(LabMapper.INSTANCE.getOrganizerDTO(output),
                    responseHeader, HttpStatus.OK);
        }
        perPage = perPage == null ? 3 : perPage;
        page = page == null ? 1 : page;
        Page<Organizer> pageOutput = organizerService.getOrganizer(page - 1, perPage);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count",
                String.valueOf(pageOutput.getTotalElements()));
        return new ResponseEntity<>(LabMapper.INSTANCE.getOrganizerDTO(pageOutput.getContent()),
                responseHeader, HttpStatus.OK);
    }

    @GetMapping("/organizers/{id}")
    ResponseEntity<?> getOrganizer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(LabMapper.INSTANCE.getOrganizerDTO(organizerService.getOrganizer(id)));
    }

    @PostMapping("/organizers")
    ResponseEntity<?> addOrganizer(@RequestBody Organizer organizer) {
        Organizer output = organizerService.save(organizer);
        return ResponseEntity.ok(LabMapper.INSTANCE.getOrganizerDTO(output));
    }
}
