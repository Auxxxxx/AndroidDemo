package al.kr.mdfactory.models;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

import al.kr.mdfactory.models.util.ManyToMany;
import al.kr.mdfactory.models.util.OneToMany;

public final class OperationGroup {

    private long id;
    private String name;
    @ManyToMany
    private List<Employee> employees = new ArrayList<>();
    @OneToMany
    private List<Operation> operations = new ArrayList<>();

    public OperationGroup() {
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

    public List getOperations() {
        return operations;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public void ListOperations(List operations) {
        this.operations = operations;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationGroup that = (OperationGroup) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
