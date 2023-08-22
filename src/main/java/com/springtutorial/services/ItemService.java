package com.springtutorial.services;

import com.springtutorial.models.Item;
import com.springtutorial.models.Person;
import com.springtutorial.repositories.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemsRepository itemsRepository;

    @Autowired
    public ItemService(ItemsRepository itemRepository) {
        this.itemsRepository = itemRepository;
    }

    public List<Item> findByItemName(String itemName){
        return itemsRepository.findByItemName(itemName);
    }

    public List<Item> findByOwner(Person owner){
        return itemsRepository.findByOwner(owner);
    }

}
