package com.hikvision.selector;

import com.hikvision.dto.User;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;

public class MyPartitionSelector implements PartitionSelectorStrategy {
    @Override
    public int selectPartition(Object key, int partitionCount) {
        if(key instanceof User) {
            return ((User) key).getId();
        }
        return 0;
    }
}
