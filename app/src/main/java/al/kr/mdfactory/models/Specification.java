package al.kr.mdfactory.models;

import java.util.ArrayList;
import java.util.List;

import al.kr.mdfactory.models.util.ManyToMany;

public final class Specification {

    private long id;
    private String name;

    @ManyToMany
    private List<Operation> operations = new ArrayList<>();

    public Specification() {
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public long getId() {
        return id;
    }

    public void ListId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void ListName(String name) {
        this.name = name;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void ListOperations(List<Operation> operations) {
        this.operations = operations;
    }


}
