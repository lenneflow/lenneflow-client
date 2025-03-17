package de.lenneflow.lenneflowclient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RunStatistic {

    private long successRate;
    private long failureRate;
    private long runningRate;
    private long deployingRate;
    private long totalRuns;
    private long successRuns;
    private long failureRuns;
    private long runningRuns;
    private long deployingRuns;

}
