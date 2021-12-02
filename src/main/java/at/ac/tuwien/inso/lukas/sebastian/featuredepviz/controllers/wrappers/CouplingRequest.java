package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers.wrappers;

import java.util.List;

public class CouplingRequest {

    private int minCountFilter;
    private double minLCFilter;
    private List<String> sourceEntityIds;

    public List<String> getSourceEntityIds() {
        return sourceEntityIds;
    }

    public CouplingRequest setSourceEntityIds(List<String> sourceEntityIds) {
        this.sourceEntityIds = sourceEntityIds;
        return this;
    }

    public int getMinCountFilter() {
        return minCountFilter;
    }

    public CouplingRequest setMinCountFilter(int minCountFilter) {
        this.minCountFilter = minCountFilter;
        return this;
    }

    public double getMinLCFilter() {
        return minLCFilter;
    }

    public CouplingRequest setMinLCFilter(double minLCFilter) {
        this.minLCFilter = minLCFilter;
        return this;
    }
}
