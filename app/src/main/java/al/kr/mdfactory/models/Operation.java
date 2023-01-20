package al.kr.mdfactory.models;

import java.util.ArrayList;
import java.util.List;

import al.kr.mdfactory.models.util.ManyToMany;
import al.kr.mdfactory.models.util.ManyToOne;
import al.kr.mdfactory.models.util.OneToMany;

public final class Operation {

    private Long id;
    private String name;
    @ManyToOne
    private OperationGroup operationGroup;
    @ManyToMany
    private List<Specification> specifications = new ArrayList<>();

    public Operation() {
    }

    public Long getId() {
        return id;
    }

    public void ListId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void ListName(String name) {
        this.name = name;
    }

    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void ListSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public void ListOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
    }
}
