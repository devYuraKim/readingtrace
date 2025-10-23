package com.yurakim.readingtrace.shelf.service.serviceImpl;

import com.yurakim.readingtrace.shelf.dto.ShelfDto;
import com.yurakim.readingtrace.shelf.entity.DefaultShelf;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import com.yurakim.readingtrace.shelf.mapper.ShelfMapper;
import com.yurakim.readingtrace.shelf.repository.DefaultShelfRepository;
import com.yurakim.readingtrace.shelf.repository.ShelfRepository;
import com.yurakim.readingtrace.shelf.service.ShelfService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShelfServiceImpl implements ShelfService {

    private final DefaultShelfRepository defaultShelfRepository;
    private final ShelfRepository shelfRepository;
    private final ShelfMapper shelfMapper;

    @Override
    @Transactional
    public void createDefaultShelves(Long userId) {
        List<DefaultShelf> defaultShelves = defaultShelfRepository.findAll();

        List<Shelf> userDefaultShelves = defaultShelves.stream().map( defaultShelf -> {
            Shelf shelf  = new Shelf();
            shelf.setUserId(userId);
            shelf.setName(defaultShelf.getName());
            shelf.setSlug(defaultShelf.getSlug());
            shelf.setIsDefault(true);
            shelf.setDefaultShelfId(defaultShelf.getId());
            shelf.setOrderIndex(defaultShelf.getOrderIndex());
            return shelf;
        }).toList();
        shelfRepository.saveAll(userDefaultShelves);
    }

    @Override
    public List<ShelfDto> getUserShelves(Long userId) {
        List<Shelf> userShelves = shelfRepository.findByUserId(userId);
        return userShelves.stream().map(shelfMapper::mapToDto).toList();
    }

    @Override
    public ShelfDto createUserShelf(Long userId, String shelfName) {
        Shelf newShelf = new Shelf();
        newShelf.setUserId(userId);
        newShelf.setName(shelfName);
        newShelf.setSlug(shelfName.toLowerCase().replace(" ", "-"));
        newShelf.setIsDefault(false);
        shelfRepository.save(newShelf);

        return shelfMapper.mapToDto(newShelf);
    }

}
