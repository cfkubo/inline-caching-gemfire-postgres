package com.example.service;

import com.example.model.DataItem;
import com.example.repository.DataItemRepository;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Autowired
    private Region<String, DataItem> dataItemRegion;

    @Autowired
    private DataItemRepository dataItemRepository;

    public DataItem getDataById(String id) {
        DataItem dataItem = dataItemRegion.get(id);
        if (dataItem != null) {
            return dataItem;
        }
        dataItem = fetchDataFromDatabase(id);
        if (dataItem != null) {
            dataItemRegion.put(id, dataItem);
        }
        return dataItem;
    }

    private DataItem fetchDataFromDatabase(String id) {
        return dataItemRepository.findById(Long.valueOf(id)).orElse(null);
    }
}