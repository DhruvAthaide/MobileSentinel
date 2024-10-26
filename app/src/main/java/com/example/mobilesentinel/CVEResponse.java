package com.example.mobilesentinel;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CVEResponse {
    @SerializedName("vulnerabilities")
    private List<Vulnerability> vulnerabilities;

    public List<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public static class Vulnerability {
        @SerializedName("cve_id")
        private String cveId;

        @SerializedName("description")
        private String description;

        public String getCveId() {
            return cveId;
        }

        public String getDescription() {
            return description;
        }
    }
}