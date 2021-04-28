package com.cat.test;

import com.cat.rpc.annotation.Service;
import com.cat.rpc.api.ByeService;

@Service
public class ByeServiceImpl implements ByeService {
    @Override
    public String bye(String name) {
        return "Bye, " + name;
    }
}
