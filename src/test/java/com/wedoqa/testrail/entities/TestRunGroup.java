package com.wedoqa.testrail.entities;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * @author wedoqa
 *
 * This object represents the entity you get in a TestRun under the "entries" element. It's not there when you query lists of TestRuns, only when you query individual TestRuns
 */
public class TestRunGroup extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
    @JsonProperty("id")
    private String id;
    public String getId() { return id; }
    public void setId( String id ) { this.id = id; }

    @JsonProperty("name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty("suite_id")
    private Integer suiteId;
    public Integer getSuiteId() { return suiteId; }
    public void setSuiteId(Integer suiteId) { this.suiteId = suiteId; }

    @JsonProperty("runs")
    private List<TestRun> runs;
    public List<TestRun> getRuns() { return runs; }
    public void setRuns(List<TestRun> runs) { this.runs = runs; }
}