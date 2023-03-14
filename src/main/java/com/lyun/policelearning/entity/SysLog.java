package com.lyun.policelearning.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysLog {

    @JSONField(name = "Operate_Time")
    private String operateTime;

    @JSONField(name = "Reg_ID")
    private String redId;

    @JSONField(name = "Operate_Result")
    private Integer operateResult;

    @JSONField(name = "Operate_Type")
    private Integer operateType;

    @JSONField(name = "Num_ID")
    private Long numId;

    @JSONField(name = "User_Name")
    private String username;

    @JSONField(name = "User_Type")
    private String userType;

    @JSONField(name = "User_ID")
    private String userId;

    @JSONField(name = "Organization")
    private String organization;

    @JSONField(name = "organization_ID")
    private String organizationId;

    @JSONField(name = "Terminal_Type")
    private String terminalType;

    @JSONField(name = "Terminal_Ip")
    private String terminalIp;

    @JSONField(name = "Inquire_Type")
    private String inquireType;

    @JSONField(name = "Inquire_Content")
    private String inquireContent;

    @JSONField(name = "Record_Type")
    private String recordType;
}
