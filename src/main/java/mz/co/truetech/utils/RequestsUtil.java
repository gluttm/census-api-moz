package mz.co.truetech.utils;

import mz.co.truetech.dto.PermissionDTO;
import mz.co.truetech.dto.RoleRequest;
import mz.co.truetech.dto.UserDTO;
import mz.co.truetech.pojos.UserRoleRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestsUtil {

    public static Set<Long> idsChecker(Set<PermissionDTO> permissionDTOSet) {
        Set<Long> ids = new HashSet<>();
        permissionDTOSet.stream().map(p -> {
            if (p.getId() != null) {
                ids.add(p.getId());
            }
            return  ids;
        }).collect(Collectors.toSet());
        return  ids;
    }

    public static Set<Long> idsRoleChecker(Set<UserRoleRequest> roleRequests) {
        Set<Long> roleIds = new HashSet<>();
        roleRequests.stream().map(r -> {
            if (r.getId() != null) {
                roleIds.add(r.getId());
            }
            return  roleIds;
        }).collect(Collectors.toSet());
        return roleIds;
    }
}
