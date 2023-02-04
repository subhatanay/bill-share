package com.subadev.billshare.groupbillshare.service;

public interface GroupJoinService {

    public String generateJoinCodeForGroup(String groupId, boolean force);

    String getJoinCodeByGroupId(String groupId);

    String getGroupIdByJoinCode(String joinCode);

    void removeJoinCode(String groupId);

}
