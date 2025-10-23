package com.yurakim.readingtrace.shelf.service;

import com.yurakim.readingtrace.shelf.dto.CustomShelfDto;

import java.util.List;

public interface ShelfService {

    List<CustomShelfDto> getCustomShelves(Long userId);

    CustomShelfDto createCustomShelf(Long userId, String shelfName);

    }
