package com.example.mobilesentinel;

import java.util.List;

public class CVEResponse {
    private List<Vulnerability> vulnerabilities;

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(List<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public static class Vulnerability {
        private String cveId;
        private String description;
        private String publishedDate;
        private String lastModifiedDate;
        private String severity; // New field for severity
        private String cvssMetrics; // New field for CVSS metrics

        // Getters and setters
        public String getCveId() {
            return cveId;
        }

        public void setCveId(String cveId) {
            this.cveId = cveId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
        }

        public String getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(String lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getCvssMetrics() {
            return cvssMetrics;
        }

        public void setCvssMetrics(String cvssMetrics) {
            this.cvssMetrics = cvssMetrics;
        }
    }
}
