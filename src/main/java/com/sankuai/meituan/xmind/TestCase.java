package com.sankuai.meituan.xmind;

import java.util.List;

/**
 * Created by guokaiqiang on 2018/5/23.
 */
public class TestCase {
    private List<String> directory;//用例目录
    private String name;//用例名称
    private String requirementId;//需求ID
    private String precondition;//前置条件
    private String step;//用例步骤
    private String expect;//预期结果
    private String type;//用例类型
    private String status;//用例状态
    private String level;//用例等级
    private String creater;//创建人
    private String subproject;//子项目
    private String version;//版本信息
    private String specialDeliverySchedulingStrategy;//专送调度策略
    private String businessType;//业务类型
    private String servicePacksOrOrderSources;//服务包&订单来源
    private String crowdsourcingSchedulingStrategy;//众包调度策略
    private String pmSourceRequirements;//PM来源需求
    private String rdSourceRequirements;//RD来源需求
    private String businessModule;//业务模块
    private String automationCoveredOrRegressionCase;//自动化已覆盖&回归用例
    private String description;//用例描述

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDirectory() {
        return directory;
    }

    public void setDirectory(List<String> directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getSubproject() {
        return subproject;
    }

    public void setSubproject(String subproject) {
        this.subproject = subproject;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSpecialDeliverySchedulingStrategy() {
        return specialDeliverySchedulingStrategy;
    }

    public void setSpecialDeliverySchedulingStrategy(String specialDeliverySchedulingStrategy) {
        this.specialDeliverySchedulingStrategy = specialDeliverySchedulingStrategy;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getServicePacksOrOrderSources() {
        return servicePacksOrOrderSources;
    }

    public void setServicePacksOrOrderSources(String servicePacksOrOrderSources) {
        this.servicePacksOrOrderSources = servicePacksOrOrderSources;
    }

    public String getCrowdsourcingSchedulingStrategy() {
        return crowdsourcingSchedulingStrategy;
    }

    public void setCrowdsourcingSchedulingStrategy(String crowdsourcingSchedulingStrategy) {
        this.crowdsourcingSchedulingStrategy = crowdsourcingSchedulingStrategy;
    }

    public String getPmSourceRequirements() {
        return pmSourceRequirements;
    }

    public void setPmSourceRequirements(String pmSourceRequirements) {
        this.pmSourceRequirements = pmSourceRequirements;
    }

    public String getRdSourceRequirements() {
        return rdSourceRequirements;
    }

    public void setRdSourceRequirements(String rdSourceRequirements) {
        this.rdSourceRequirements = rdSourceRequirements;
    }

    public String getBusinessModule() {
        return businessModule;
    }

    public void setBusinessModule(String businessModule) {
        this.businessModule = businessModule;
    }

    public String getAutomationCoveredOrRegressionCase() {
        return automationCoveredOrRegressionCase;
    }

    public void setAutomationCoveredOrRegressionCase(String automationCoveredOrRegressionCase) {
        this.automationCoveredOrRegressionCase = automationCoveredOrRegressionCase;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "directory=" + directory +
                ", name='" + name + '\'' +
                ", requirementId='" + requirementId + '\'' +
                ", precondition='" + precondition + '\'' +
                ", step='" + step + '\'' +
                ", expect='" + expect + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", level='" + level + '\'' +
                ", creater='" + creater + '\'' +
                ", subproject='" + subproject + '\'' +
                ", version='" + version + '\'' +
                ", specialDeliverySchedulingStrategy='" + specialDeliverySchedulingStrategy + '\'' +
                ", businessType='" + businessType + '\'' +
                ", servicePacksOrOrderSources='" + servicePacksOrOrderSources + '\'' +
                ", crowdsourcingSchedulingStrategy='" + crowdsourcingSchedulingStrategy + '\'' +
                ", pmSourceRequirements='" + pmSourceRequirements + '\'' +
                ", rdSourceRequirements='" + rdSourceRequirements + '\'' +
                ", businessModule='" + businessModule + '\'' +
                ", automationCoveredOrRegressionCase='" + automationCoveredOrRegressionCase + '\'' +
                '}';
    }
}
