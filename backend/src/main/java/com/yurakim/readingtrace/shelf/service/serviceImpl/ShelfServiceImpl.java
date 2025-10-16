package com.yurakim.readingtrace.shelf.service.serviceImpl;

import com.yurakim.readingtrace.shelf.dto.ShelfDto;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import com.yurakim.readingtrace.shelf.repository.ShelfRepository;
import com.yurakim.readingtrace.shelf.service.ShelfService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;

    private static final List<String> DEFAULT_SHELF_NAMES = List.of(
            "Want to Read",
            "Currently Reading",
            "Already Read",
            "Never Finished"
    );

    @Override
    @Transactional
    public void createDefaultShelves(Long userId) {
        List<Shelf> defaultShelves = DEFAULT_SHELF_NAMES.stream().map( name -> {
            Shelf shelf  = new Shelf();
            shelf.setUserId(userId);
            shelf.setName(name);
            shelf.setSlug(name.toLowerCase().replace(" ", "-"));
            shelf.setIsDefault(true);
            return shelf;
        }).toList();
        shelfRepository.saveAll(defaultShelves);
    }

    @Override
    public List<ShelfDto> getUserShelves(Long userId) {
        List<Shelf> userShelves = shelfRepository.findByUserId(userId);
        return userShelves.stream().map(shelf -> {
            ShelfDto shelfDto = new ShelfDto();
            shelfDto.setShelfId(shelf.getId());
            shelfDto.setUserId(shelf.getUserId());
            shelfDto.setName(shelf.getName());
            shelfDto.setSlug(shelf.getSlug());
            shelfDto.setDescription(shelf.getDescription());
            shelfDto.setBookCount(shelf.getBookCount());
            shelfDto.setIsDefault(shelf.getIsDefault());
            return shelfDto;
        }).toList();
    }

    @Override
    public List<ShelfDto> createUserShelf(Long userId, String shelfName) {
        Shelf userShelf = new Shelf();
        userShelf.setUserId(userId);
        userShelf.setName(shelfName);
        userShelf.setSlug(shelfName.toLowerCase().replace(" ", "-"));
        userShelf.setIsDefault(false);
        shelfRepository.save(userShelf);
        return getUserShelves(userId);
    }
}
