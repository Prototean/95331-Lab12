package se331.lab.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import se331.lab.dao.OrganizerDao;
import se331.lab.entity.Organizer;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {
    final OrganizerDao organizerDao;
    @Override
    public List<Organizer> getAllOrganizer() {
        return organizerDao.getOrganizer(Pageable.unpaged()).getContent();
    }

    @Override
    public Page<Organizer> getOrganizer(Integer page, Integer pageSize) {
        return organizerDao.getOrganizer(PageRequest.of(page,pageSize));
    }

    @Override
    public Organizer getOrganizer(Long id) {
        return organizerDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "The given id is not found"));
    }

    @Override
    @Transactional
    public Organizer save(Organizer organizer) {
        if (organizer.getName() == null || organizer.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The organizer name is required");
        }
        List<String> images = organizer.getImages() == null ? new ArrayList<>() : organizer.getImages();
        images.removeIf(image -> image == null || image.isBlank());
        if (images.size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only 1 image is allowed for an organizer");
        }
        organizer.setImages(images);
        organizer.setOwnEvents(new ArrayList<>());
        return organizerDao.save(organizer);
    }
}
