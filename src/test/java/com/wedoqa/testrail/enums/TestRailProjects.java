package com.wedoqa.testrail.enums;

/**
 * Created by wedoqa
 *
 * You can add here the id and name of TestRail projects
 */
public enum TestRailProjects {

    SC(1, "wedoqa - Web"),	//FIXME add project name
	;

    private int id;
    private String name;

    private TestRailProjects(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public static TestRailProjects getById(int id) {
    	for (TestRailProjects project: TestRailProjects.values()) {
    		if (project.getId() == id) {
    			return project;
    		}
    	}
    	return null;
    }
}
