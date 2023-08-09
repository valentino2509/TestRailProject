package com.wedoqa.testrail.entities;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wedoqa
 *
 * Represents a list of TestResults entities
 */
public class TestResults extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("results")
    private List<TestResult> results = new ArrayList<TestResult>();
    public List<TestResult> getResults() { return results; }
    public void setResults(List<TestResult> results) { this.results = results; }

    /**
     * Allows you to add a test result to the list of results that will be posted
     * @param result
     */
    public void addResult(TestResult result) {
        results.add(result);
    }
}