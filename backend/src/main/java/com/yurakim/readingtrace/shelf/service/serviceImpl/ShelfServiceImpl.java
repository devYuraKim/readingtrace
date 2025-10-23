package com.yurakim.readingtrace.shelf.service.serviceImpl;

import com.yurakim.readingtrace.shelf.dto.CustomShelfDto;
import com.yurakim.readingtrace.shelf.entity.Shelf;
import com.yurakim.readingtrace.shelf.mapper.ShelfMapper;
import com.yurakim.readingtrace.shelf.repository.ShelfRepository;
import com.yurakim.readingtrace.shelf.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;
    private final ShelfMapper shelfMapper;

    @Override
    public List<CustomShelfDto> getCustomShelves(Long userId) {
        List<Shelf> userShelves = shelfRepository.findByUserId(userId);
        return userShelves.stream().map(shelfMapper::mapToDto).toList();
    }

    @Override
    public CustomShelfDto createCustomShelf(Long userId, String shelfName) {
        Shelf newShelf = new Shelf();
        newShelf.setUserId(userId);
        newShelf.setName(shelfName);
        newShelf.setSlug(shelfName.toLowerCase().replace(" ", "-"));
        shelfRepository.save(newShelf);

        return shelfMapper.mapToDto(newShelf);
    }

}
