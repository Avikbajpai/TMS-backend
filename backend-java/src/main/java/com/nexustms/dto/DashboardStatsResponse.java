package com.nexustms.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DashboardStatsResponse {
    private long total;
    private long open;
    private long inProgress;
    private long resolved;
    private Map<String, Long> byPriority;
}
