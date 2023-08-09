package com.wedoqa.testrail.entities;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by wedoqa
 */
public class TestPlanCreator extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
    @JsonProperty("name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty("milestone_id")
    private Integer milestoneId;
    public Integer getMilestoneId() { return milestoneId; }
    public void setMilestoneId(Integer milestoneId) { this.milestoneId = milestoneId; }

    @JsonProperty("assignedTo_id")
    private Integer assignedToId;
    public Integer getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Integer assignedToId) { this.assignedToId = assignedToId; }

    @JsonProperty("entries")
    private List<PlanEntry> entries;
    public void setEntries(List<PlanEntry> entries) { this.entries = entries; }
}