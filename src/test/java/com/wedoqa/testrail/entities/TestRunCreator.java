package com.wedoqa.testrail.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wedoqa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRunCreator extends BaseEntity {
	private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty("description")
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @JsonProperty("suite_id")
    private Integer suiteId;
    public Integer getSuiteId() { return suiteId; }
    public void setSuiteId(Integer suiteId) { this.suiteId = suiteId; }

    @JsonProperty("milestone_id")
    private Integer milestoneId;
    public Integer getMilestoneId() { return milestoneId; }
    public void setMilestoneId(Integer milestoneId) { this.milestoneId = milestoneId; }

    @JsonProperty("include_all")
    private Boolean includeAll;
    public Boolean isIncludeAll() { return includeAll; }
    public void setIncludeAll(Boolean includeAll) { this.includeAll = includeAll; }

    @JsonProperty("case_ids")
    private List<Integer> caseIds;
    public List<Integer> getCaseIds() { return caseIds; }
    public void setCaseIds(List<Integer> caseIds) { this.caseIds = caseIds; }
    public void addConfigId(Integer id){
        if(caseIds == null){
            caseIds = new ArrayList<Integer>();
        }
        if(!caseIds.contains(id)){
            caseIds.add(id);
        }
    }
}