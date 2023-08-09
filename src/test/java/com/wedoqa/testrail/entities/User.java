package com.wedoqa.testrail.entities;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author wedoqa
 */
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
    private Integer id;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @JsonProperty("name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonProperty("email")
    private String email;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @JsonProperty("is_active")
    private boolean is_active;
    public boolean isActive() { return is_active; }
    public void setIsActive(boolean is_active) { this.is_active = is_active; }
}