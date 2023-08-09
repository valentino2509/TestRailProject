package com.wedoqa.testrail.entities;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * @author wedoqa
 */
public class PlanEntry extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
    @JsonProperty("suite_id")
    private Integer suiteId;
    public Integer getSuiteId() { return suiteId; }
    public void setSuiteId(Integer suiteId) { this.suiteId = suiteId; }

    @JsonProperty("assignedto_id")
    private Integer assignedToId;
    public Integer getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Integer assignedToId) { this.assignedToId = assignedToId; }

    @JsonProperty("include_all")
    private boolean includeAll;
    public boolean getIncludeAll() { return includeAll; }
    public void setIncludeAll(boolean includeAll) { this.includeAll = includeAll; }

    @JsonProperty("config_ids")
    private List<Integer> configIds;
    public List<Integer> getConfigIds() { return configIds; }
    public void setConfigIds(List<Integer> configIds) { this.configIds = configIds; }

    @JsonProperty("runs")
    private List<PlanEntryRun> runs;
    public List<PlanEntryRun> getRuns() { return runs; }
    public void setRuns(List<PlanEntryRun> runs) { this.runs = runs; }

    @JsonProperty("case_ids")
    private List<Integer> caseIds;
    public List<Integer> getCaseIds() { return caseIds; }
    public void setCaseIds(List<Integer> caseIds) { this.caseIds = caseIds; }

    @JsonProperty("name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
