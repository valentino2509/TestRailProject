package com.wedoqa.testrail.service;

/**
 * TestRails end-points
 * @author wedoqa
 */
public enum TestRailCommand {
    GET_PROJECTS("get_projects", "projects"),
    GET_PROJECT("get_project"),
    GET_SUITES("get_suites", "suites"),
    GET_SUITE("get_suite"),
    GET_PLANS("get_plans", "plans"),
    GET_PLAN("get_plan"),
    GET_SECTION("get_section"),
    GET_SECTIONS("get_sections", "sections"),
    GET_RUNS("get_runs", "runs"),
    GET_RUN("get_run"),
    GET_CASES("get_cases", "cases"),
    GET_CASE("get_case"),
    UPDATE_CASE("update_case"),
    GET_TESTS("get_tests", "tests"),
    GET_RESULTS("get_results", "results"),
    GET_ATTACHMENT("get_attachments_for_case"),
    ADD_ATTACHMENT_TO_RESULT("add_attachment_to_result"),
    ADD_ATTACHMENT_TO_RUN("add_attachment_to_run"),
    ADD_RESULT("add_result"),
    ADD_RESULTS("add_results", "results"),
    ADD_RUN("add_run"),
    CLOSE_RUN("close_run"),
    ADD_PLAN("add_plan"),
    ADD_PLAN_ENTRY("add_plan_entry"),
    ADD_MILESTONE("add_milestone"),
    GET_MILESTONE("get_milestone"),
    GET_MILESTONES("get_milestones", "milestones"),
    GET_USERS("get_users", "users"),
    GET_USER_BY_ID("get_user"),
    GET_USER_BY_EMAIL("get_user_by_email");

    private String command;
    private String jsonFieldName;
    
    private TestRailCommand(String command) {
        this.command = command;
    }
    
    private TestRailCommand(String command, String jsonFieldName) {
        this.command = command;
        this.jsonFieldName = jsonFieldName;
    }
    
    public String getCommand() { 
        return command; 
    }
    
    public String getJsonFieldName() {
        return jsonFieldName;
    }
}