package com.wedoqa.testrail.entities;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author wedoqa
 *
 * The TestRail top-level entity is actually called a "Test", but that collides with the @Test annotation we're
 * using in the unit tests. Calling it a TestInstance will avoid ambiguity
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestInstance extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
    @JsonProperty("id")
    private Integer id;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @JsonProperty("title")
    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @JsonProperty("status_id")
    private Integer statusId;
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }

    @JsonProperty("type_id")
    private Integer typeId;
    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    @JsonProperty("priority_id")
    private Integer priorityId;
    public Integer getPriorityId() { return priorityId; }
    public void setPriorityId(Integer priorityId) { this.priorityId = priorityId; }

    @JsonProperty("estimate")
    private String estimate;
    public String getEstimate() { return estimate; }
    public void setEstimate(String estimate) { this.estimate = estimate; }

    @JsonProperty("estimate_forecast")
    private String estimateForecast;
    public String getEstimateForecast() { return estimateForecast; }
    public void setEstimateForecast(String estimate_forecast) { this.estimateForecast = estimate_forecast; }

    @JsonProperty("assignedto_id")
    private Integer assignedtoId;
    public Integer getAssignedtoId() { return assignedtoId; }
    public void setAssignedtoId(Integer assignedtoId) { this.assignedtoId = assignedtoId; }

    @JsonProperty("run_id")
    private Integer runId;
    public Integer getRunId() { return runId; }
    public void setRunId(Integer runId) { this.runId = runId; }

    @JsonProperty("case_id")
    private Integer caseId;
    public Integer getCaseId() { return caseId; }
    public void setCaseId(Integer caseId) { this.caseId = caseId; }
    
    @JsonProperty("area")
    private Integer area;
    public Integer getArea() { return area; }
    public void setArea(Integer area) { this.caseId = area; }

    /**
     * Returns the list of test results (most recent first) associated with this TestInstance
     * @param limit A limit to the number of results you want to see (1 will give you the single most recent)
     * @return The number of TestResults specified by the limit, in descending chron order (i.e. newest to oldest)
     */
    public List<TestResult> getResults(int limit) {
        return getTestRailService().getTestResults(this.getId(), limit);
    }

    /**
     * Returns the single most recent TestResult for this TestInstance
     * @return The single most recent TestResult for this TestInstance
     */
    public List<TestResult> getMostRecentResult() {
        return getResults(1);
    }
}