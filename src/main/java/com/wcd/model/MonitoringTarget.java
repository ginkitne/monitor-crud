package com.wcd.model;

import java.time.LocalDateTime;

/**
 * Represents a website monitoring target.
 */
public class MonitoringTarget {

    private Long id;
    private String url;
    private String checkInterval;
    private String cssSelector;
    private boolean active;
    private LocalDateTime createdAt;

    public MonitoringTarget() {
    }

    public MonitoringTarget(Long id, String url, String checkInterval,
                            String cssSelector, boolean active,
                            LocalDateTime createdAt) {
        this.id = id;
        this.url = url;
        this.checkInterval = checkInterval;
        this.cssSelector = cssSelector;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getCheckInterval() { return checkInterval; }
    public void setCheckInterval(String checkInterval) { this.checkInterval = checkInterval; }

    public String getCssSelector() { return cssSelector; }
    public void setCssSelector(String cssSelector) { this.cssSelector = cssSelector; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
