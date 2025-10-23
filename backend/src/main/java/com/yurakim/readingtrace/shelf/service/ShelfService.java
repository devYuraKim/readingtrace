package com.yurakim.readingtrace.shelf.service;

import com.yurakim.readingtrace.shelf.dto.ShelfDto;

import java.util.List;

public interface ShelfService {

    List<ShelfDto> getUserShelves(Long userId);

    ShelfDto createUserShelf(Long userId, String shelfName);

    }
