package com.zdz.service.impl;

import com.zdz.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    public boolean hasPermissions(String permission){
        if(SecurityUtils.isSuperAdmin()){
            return true;
        }
        List<String> perms = SecurityUtils.getLoginUser().getPermissions();
        return perms.contains(permission);
    }
}
