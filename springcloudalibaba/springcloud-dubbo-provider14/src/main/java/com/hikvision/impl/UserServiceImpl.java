package com.hikvision.impl;

import java.util.Arrays;
import java.util.List;

import com.hikvision.api.UserService;
import com.hikvision.entity.UserAddress;

public class UserServiceImpl implements UserService {

    //@Override
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress address1 = new UserAddress(1, "北京市昌平区", "1", "李老师", "010-56253825", "Y");
        UserAddress address2 = new UserAddress(2, "深圳市宝安区", "1", "王老师", "010-56253825", "N");
        return Arrays.asList(address1,address2);
    }

}